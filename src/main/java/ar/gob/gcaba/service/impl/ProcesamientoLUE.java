package ar.gob.gcaba.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ar.gob.gcaba.dao.RegistroLUEDAO;
import ar.gob.gcaba.exceptions.RegistroNoEncontradoException;
import ar.gob.gcaba.model.DocVinculado;
import ar.gob.gcaba.service.ProcesamientoStrategy;

@Component("procesamientoLUE")
public class ProcesamientoLUE implements ProcesamientoStrategy {

    @Resource(name = "registroLueDAO")
    RegistroLUEDAO registroLUEDAO;

    @Autowired
    Logger logger;
    
    String filename;
    List<Integer> registrosId;
    DocVinculado documentoVinculado;
    String docsVinculadosString;
    final String ACRONIMO = "DOCPE";
    final String REFERENCIA = "Archivo vinculado por Migracion";
    
    @Value("#{appProperties['gedo.uploader.username.lue']}")
    private String usuarioUploader;

    @Override
    public List<Integer> getRegistrosId() throws RegistroNoEncontradoException {
        List<Integer> registrosIdList = new ArrayList<Integer>();
        docsVinculadosString = null;
        try {
            Long cuil = Long.valueOf(filename);
            registrosIdList = registroLUEDAO.getListaRegistrosId(cuil);

            if (registrosIdList.isEmpty()) {
                throw new RegistroNoEncontradoException("El registro con cuil " + cuil
                        + " No se encuentra en el sistema");
            }
        } catch (NumberFormatException nfe) {
            logger.warn("El nombre de archivo " + filename + " no tiene un formato esperado para registro LUE");
        }
        return registrosIdList;
    }

    @Override
    public void vincularDocumentoARegistros() {
        Integer idRegistro = registrosId.get(0);
        Integer docId = registroLUEDAO.agregarDocumentoVinculado(idRegistro, documentoVinculado.getNumero(), 99,
                documentoVinculado.getRuta(), documentoVinculado.getFechaVencimiento(), ACRONIMO,
                REFERENCIA);
        docsVinculadosString = docId.toString();

    }

    @Override
    public void auditarDocumentoProcesado(String estado, String mensajeError) {
        registroLUEDAO.insertarDocumentoProcesado(docsVinculadosString, filename,estado, mensajeError );
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
    public String getAcronimo() {
        return ACRONIMO;
    }

    @Override
    public String getReferencia() {
        return REFERENCIA;
    }

    @Override
    public String getUsuarioUploader() {
        return usuarioUploader;
    }

}
