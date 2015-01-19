package ar.gob.gcaba.dao;

import java.util.Date;
import java.util.List;

public interface RegistroRIBDAO {
	
	List<Integer> getListaRegistrosIdByHogar(Integer idHogar);
	
	List<Integer> getListaRegistrosId(Long cuil);

    Integer agregarDocumentoVinculado(Integer idRegistro, String numDocumento, Integer tipoDocumento, 
            String rutaDocumento, Date fechaVencimiento);

    void insertarDocumentoProcesado(String documentoId, String filename, String estado, String mensajeError);
	
}
