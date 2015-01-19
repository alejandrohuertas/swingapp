package ar.gob.gcaba.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.gob.gcaba.constants.TipoRegistroEnum;
import ar.gob.gcaba.exceptions.RegistroNoEncontradoException;
import ar.gob.gcaba.exceptions.VinculacionDocumentoException;
import ar.gob.gcaba.model.DocVinculado;
import ar.gob.gcaba.service.DigitalizadorService;
import ar.gob.gcaba.service.ProcesamientoStrategy;
import ar.gob.gcaba.service.remoting.UploadGEDODocument;

@Service ("digitalizadorService")
public class DigitalizadorServiceImpl implements DigitalizadorService {

	@Resource (name= "procesamientoLUE")
	ProcesamientoLUE procesamientoLUE;
	
	@Resource (name= "procesamientoRIB")
	ProcesamientoRIB procesamientoRIB;
	
    @Autowired
    UploadGEDODocument uploadGEDODocument;
    
    @Autowired
    Logger logger; 
    
    ProcesamientoStrategy procesamientoStrategy;
	int count = 0;
	@Override
	public void procesarLecturaArchivos(List<File> pdfFiles, String sistemaOrigen) {
        Integer count =0, totalArchivos = pdfFiles.size();
        logger.info("Se procesaran un total de "+totalArchivos + " archivos PDF");
        logger.info("-------------------------------------------------");
        logger.info("INICIA el procesamiento");
        logger.info("-------------------------------------------------");
        String currentFilename = null;
        String estado;
        String mensajeError;
        for (File file : pdfFiles) {
		    currentFilename=file.getName();
		    count++;
            mensajeError= null; 
            estado = null;
			logger.info("Procesando archivo "+ count +" restan "+ (totalArchivos-count));
			logger.info("Path:  "+ file.getAbsolutePath());
			currentFilename = currentFilename.substring(0, currentFilename.lastIndexOf(".pdf"));
			List<Integer> registrosIdList= null;
			
			
            procesamientoStrategy = null;
			if (sistemaOrigen.equals(TipoRegistroEnum.RIB.getTipo())){
				procesamientoStrategy = procesamientoRIB;
			} else if (sistemaOrigen.equals(TipoRegistroEnum.LUE.getTipo())){
				procesamientoStrategy = procesamientoLUE;
			}
			procesamientoStrategy.setFileName(currentFilename);
			registrosIdList = conseguirListadoRegistros(procesamientoStrategy);
			
			procesamientoStrategy.setRegistrosId(registrosIdList);
           //Solamente si se encuentran coincidencias en el sitema a partir 
           //de los PDFs se procesan en GEDO
            if (registrosIdList!=null && !registrosIdList.isEmpty()){
                byte[] fileb = readFile(file);

                DocVinculado docVinculado=enviarArchivoGEDO(fileb, currentFilename, sistemaOrigen);
                //si el documento fue subido correctamente a gedo
                if (docVinculado!=null){
                    procesamientoStrategy.setDocumentoVinculado(docVinculado);
                    vincularRegistrosConDocumento(procesamientoStrategy);
                    estado = "VINCULADO";
                    logger.info("Archivo vinculado SATISFACTORIAMENTE");
                } else{
                    estado = "FALLA";
                    mensajeError = "El archivo no pudo ser vinculado a GEDO";
                }                    
            }
            else{
                estado= "FALLA";
                mensajeError = "No se encontraron coincidencias el el sistema para el archivo "+currentFilename+".pdf";
            }
            registrarDocumentoProcesado(estado, mensajeError);
            logger.info("-------------------------------------------------");
		}
        logger.info("FIN del proceso de vinculacion");

	}

	
    private void registrarDocumentoProcesado(String estado, String mensajeError) {
        procesamientoStrategy.auditarDocumentoProcesado(estado, mensajeError);
        
    }


    private DocVinculado enviarArchivoGEDO(byte[] fileb, String fileName, String sistemaOrigen) {
        DocVinculado docVinculado = null;
        try {
            docVinculado = uploadGEDODocument.importarDocumentoGEDO(procesamientoStrategy.getAcronimo(), 
                    fileb, null, procesamientoStrategy.getReferencia(), "pdf", 
                    procesamientoStrategy.getUsuarioUploader(), null ,sistemaOrigen);
        } catch (VinculacionDocumentoException vde) {
            logger.error("error al subir el archivo "+fileName+ "en gedo");
        }

    return docVinculado;
    }

	private byte[] readFile(File pdfFile) {

		byte[] pdfByteArray = null;
		try {
			FileInputStream fis = new FileInputStream(pdfFile);

			pdfByteArray = IOUtils.toByteArray(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return pdfByteArray;
	}
	
	
	//TODO: no es necesaria pero la uso para probar que leyo bn
	@SuppressWarnings("unused")
	private void writeFile(String filename, byte[] fileb) {
		try {
			FileOutputStream fos = new FileOutputStream("C:\\Users\\ahuertaa\\PDFs Lue y Rib\\"+filename+"_new.pdf");
			count ++;
			
			IOUtils.write(fileb, fos);
		} catch (FileNotFoundException e) {
		    System.out.println("El archivo destino no se puede establecer para: "+filename);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private List<Integer> conseguirListadoRegistros (ProcesamientoStrategy strategy){
		
		try {
			return strategy.getRegistrosId();
		} catch (RegistroNoEncontradoException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	private void vincularRegistrosConDocumento(ProcesamientoStrategy strategy){
		 strategy.vincularDocumentoARegistros();
		
	}
	
    public List<File> obtenerListaPDFFiles(File folder){
        
        List<File> pfFileList = new ArrayList<File>();
        if (folder.isDirectory()){
            
            FileFilter pdfFilter = new FileFilter() {
                
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().toLowerCase().endsWith(".pdf");
                }
            };
            File filesArray[] = folder.listFiles(pdfFilter);
            pfFileList = new ArrayList<File>(Arrays.asList(filesArray));
        }
        return pfFileList;
    }
}
