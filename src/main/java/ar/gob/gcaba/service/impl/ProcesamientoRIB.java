package ar.gob.gcaba.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ar.gob.gcaba.dao.RegistroRIBDAO;
import ar.gob.gcaba.exceptions.RegistroNoEncontradoException;
import ar.gob.gcaba.model.DocVinculado;
import ar.gob.gcaba.service.ProcesamientoStrategy;

@Component("procesamientoRIB")
public class ProcesamientoRIB implements ProcesamientoStrategy {

    String filename;
    List<Integer> registrosId;
    DocVinculado documentoVinculado;
    String docsVinculadosString;
    private final String ACRONIMO = "DOCPE";
    private final String REFERENCIA = "Sobre CP Digitalizado";

    @Value("#{appProperties['gedo.uploader.username.rib']}")
    private String usuarioUploader;
    
    @Autowired
    Logger logger;
    
    
    @Resource(name = "registroRibDAO")
    RegistroRIBDAO registroRIBDAO;

    @Override
    public List<Integer> getRegistrosId() throws RegistroNoEncontradoException {
        List<Integer> registrosIdList = new ArrayList<Integer>();
        docsVinculadosString = null;
        try {
            Long cuil = Long.valueOf(filename.substring(0, filename.lastIndexOf("_")));
            Integer idHogar = Integer.valueOf(filename.substring(filename.lastIndexOf("_") + 1, filename.length()));
            registrosIdList = registroRIBDAO.getListaRegistrosIdByHogar(idHogar);
            if (registrosIdList.isEmpty()) {
                // busca por cuil si no encontro ningun registro asociado al
                // codigo de hogar
                registrosIdList = registroRIBDAO.getListaRegistrosId(cuil);
            }

            if (registrosIdList.isEmpty()) {
                throw new RegistroNoEncontradoException("No se hayan asociados registros al id de hogar " + idHogar +" ni al Cuil "+ cuil
                        + ". No se encuentran en el sistema");
            }
        }
        catch (NumberFormatException nfe) {
            logger.warn("El nombre de archivo " + filename + " no tiene un formato esperado para registro RIB");
        } catch (StringIndexOutOfBoundsException siobe) {
            logger.warn("El nombre de archivo " + filename + " no tiene un formato esperado para registro RIB");
        }
        return registrosIdList;
    }

    @Override
    public void vincularDocumentoARegistros() {
        //Vinculo a cada registro extraido a partir del documento PDF 
        List<Integer> idDocumentosVinculados = new ArrayList<Integer>();
        for (Integer registroId : registrosId) {
            Integer idDoc =registroRIBDAO.agregarDocumentoVinculado(registroId, documentoVinculado.getNumero(), 99, 
                    documentoVinculado.getRuta(), documentoVinculado.getFechaVencimiento());
            idDocumentosVinculados.add(idDoc);
        }
        //extraigo los ids de todas las vinculaciones del PDF
        docsVinculadosString = idDocumentosVinculados.toString();

    }

    @Override
    public void setRegistrosId(List<Integer> registrosIdList) {
        this.registrosId = registrosIdList;

    }

    @Override
    public void setFileName(String filename) {
        this.filename = filename;
    }

    @Override
    public void setDocumentoVinculado(DocVinculado documentoVinculado) {
        this.documentoVinculado = documentoVinculado;
        
    }

    @Override
    public void auditarDocumentoProcesado(String estado, String mensajeError) {
        this.registroRIBDAO.insertarDocumentoProcesado(docsVinculadosString, filename, estado,mensajeError);
        
    }

    public String getReferencia() {
        return REFERENCIA;
    }

    public String getUsuarioUploader() {
        return usuarioUploader;
    }

    public String getAcronimo() {
        return ACRONIMO;
    }

}
