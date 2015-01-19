package ar.gob.gcaba.service.remoting.impl;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.gob.gcaba.exceptions.VinculacionDocumentoException;
import ar.gob.gcaba.gedo.satra.services.external.generardocumento.IExternalGenerarDocumentoService;
import ar.gob.gcaba.gedo.satra.services.external.generardocumento.RequestExternalGenerarDocumento;
import ar.gob.gcaba.gedo.satra.services.external.generardocumento.ResponseExternalGenerarDocumento;
import ar.gob.gcaba.model.DocVinculado;
import ar.gob.gcaba.service.remoting.UploadGEDODocument;

@Service
public class UploadGEDODocumentImpl implements UploadGEDODocument {
    
    @Autowired
    Logger logger;
    
    @Autowired
    private IExternalGenerarDocumentoService generarDocumentoService;
    
    @Override
    public DocVinculado importarDocumentoGEDO(String acronimo, byte[] data,
            Map<String, Object> metaDato, String referencia,
            String tipoArchivo, String usuario, Date fechaVencimiento, String sistemaOrigen)
            throws VinculacionDocumentoException {
        RequestExternalGenerarDocumento requestExternal = new RequestExternalGenerarDocumento();
        DocVinculado docVinculado = null;
        requestExternal.setMetaDatos(metaDato);
        requestExternal.setReferencia(referencia);
        requestExternal.setData(data);
        requestExternal.setAcronimoTipoDocumento(acronimo);
        requestExternal
                .setSistemaOrigen(sistemaOrigen);
        requestExternal.setUsuario(usuario);
        requestExternal.setTipoArchivo(tipoArchivo);
        ResponseExternalGenerarDocumento responseExternal = null;
        try {
            responseExternal = generarDocumento(requestExternal);
            docVinculado = new DocVinculado();
            docVinculado.setNumero(responseExternal.getNumero());
            docVinculado.setRuta(responseExternal.getUrlArchivoGenerado());
            docVinculado.setFechaVencimiento(fechaVencimiento);
            return docVinculado;
        } catch (VinculacionDocumentoException e) {
            logger.error("error al subir subir archivo a GEDO. "+e.getLocalizedMessage() );
        }
        return null;
    }

    
    
    @Override
    public ResponseExternalGenerarDocumento generarDocumento(
            RequestExternalGenerarDocumento requestExternal)
            throws VinculacionDocumentoException {
        ResponseExternalGenerarDocumento response = null;
        try {
            response = generarDocumentoService
                    .generarDocumentoGEDO(requestExternal);
        } catch (Exception e) {
            throw new VinculacionDocumentoException(e.getMessage(), e);
        }
        return response;
    }
}
