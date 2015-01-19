package ar.gob.gcaba.dao;

import java.util.Date;
import java.util.List;


public interface RegistroLUEDAO {
	List<Integer> getListaRegistrosId(Long cuil);

	Integer agregarDocumentoVinculado(Integer idRegistro, String numeroDocumento, Integer tipoDocumento, 
	        String ruta, Date fechaVencimiento, String acronimo, String referencia);
	
	void insertarDocumentoProcesado(String idDocumento, String filename, String estado, String error);
}
