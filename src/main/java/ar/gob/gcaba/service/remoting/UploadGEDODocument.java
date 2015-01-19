package ar.gob.gcaba.service.remoting;

import java.util.Date;
import java.util.Map;

import ar.gob.gcaba.exceptions.VinculacionDocumentoException;
import ar.gob.gcaba.gedo.satra.services.external.generardocumento.RequestExternalGenerarDocumento;
import ar.gob.gcaba.gedo.satra.services.external.generardocumento.ResponseExternalGenerarDocumento;
import ar.gob.gcaba.model.DocVinculado;

public interface UploadGEDODocument {

    DocVinculado importarDocumentoGEDO(String acronimo, byte[] data, Map<String, Object> metaDato, String referencia,
            String tipoArchivo, String usuario, Date fechaVencimiento, String sistemaOrigen) throws VinculacionDocumentoException ;

    ResponseExternalGenerarDocumento generarDocumento(RequestExternalGenerarDocumento requestExternal)
            throws VinculacionDocumentoException;

}
