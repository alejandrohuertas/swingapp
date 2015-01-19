package ar.gob.gcaba.service;

import java.util.List;

import ar.gob.gcaba.exceptions.RegistroNoEncontradoException;
import ar.gob.gcaba.model.DocVinculado;

public interface ProcesamientoStrategy {
	
	
	List<Integer> getRegistrosId()  throws RegistroNoEncontradoException ;

	void vincularDocumentoARegistros();

	void setRegistrosId(List<Integer> registrosIdList);
	
	void auditarDocumentoProcesado(String estado,String mensajeError);

	void setFileName(String fileName);

    void setDocumentoVinculado(DocVinculado documentoVInculado);

    String getAcronimo();

    String getReferencia();

    String getUsuarioUploader();
	
}
