package ar.gob.gcaba.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ar.gob.gcaba.constants.RegistrosQueries;
import ar.gob.gcaba.dao.RegistroLUEDAO;


@Repository ("registroLueDAO")
public class RegistroLUEDAOImpl extends NamedParameterJdbcTemplate implements RegistroLUEDAO {

	
	@Autowired 
	public RegistroLUEDAOImpl(@Qualifier("dataSourceLUE") DataSource dataSource) {
		super(dataSource);
	}

	
	@Override
	public List<Integer> getListaRegistrosId(Long cuil) {
		
		Map<String, Object> args = new HashMap<String, Object>();
		
		args.put(RegistrosQueries.CUIL, cuil);
		
		return this.queryForList(RegistrosQueries.SELECT_REGISTROS_LUE, args,Integer.class);
	}

	@Override
	public Integer agregarDocumentoVinculado(Integer idRegistro, String numeroDocumento, Integer tipoDocumento, String ruta, 
	            Date fechaVencimiento, String acronimo, String referencia) {
	    MapSqlParameterSource paramsMap = new MapSqlParameterSource();
        
        paramsMap.addValue(RegistrosQueries.NUMERO, numeroDocumento);
        paramsMap.addValue(RegistrosQueries.RUTA, ruta);
        paramsMap.addValue(RegistrosQueries.TIPO_DOCUMENTO, tipoDocumento);
        paramsMap.addValue(RegistrosQueries.FECHA_VENCIMIENTO, fechaVencimiento);
        paramsMap.addValue(RegistrosQueries.REGISTRO_DOCUMENTO, idRegistro);
        paramsMap.addValue(RegistrosQueries.REFERENCIA, referencia);
        paramsMap.addValue(RegistrosQueries.ACRONIMO, acronimo);
        
        String keyColumn = "ID_DOCUMENTO_VINCULADO"; 
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        
        
        this.update(RegistrosQueries.INSERT_DOCUMENTO_VICULADO_LUE, paramsMap , keyHolder, new String[] {keyColumn});
        
        return ((BigDecimal)keyHolder.getKeys().get(keyColumn)).intValue();
		
	}


	@Override
	public void insertarDocumentoProcesado(String idDocumento, String filename, String estado, String mensajeError) {
	    MapSqlParameterSource paramsMap = new MapSqlParameterSource();
	    
	    paramsMap.addValue(RegistrosQueries.DOCUMENTOS_ID, idDocumento);
        paramsMap.addValue(RegistrosQueries.NOMBRE_ARCHIVO, filename);
        paramsMap.addValue(RegistrosQueries.ESTADO, estado);
        paramsMap.addValue(RegistrosQueries.MENSAJE_ERROR, mensajeError);
//        paramsMap.addValue(RegistrosQueries.FECHA_ACTUAL, new java.sql.Date((new Date()).getTime()));
        this.update(RegistrosQueries.INSERT_AUDITORIA_DOCUMENTOS_LUE, paramsMap );
		
	}

}
