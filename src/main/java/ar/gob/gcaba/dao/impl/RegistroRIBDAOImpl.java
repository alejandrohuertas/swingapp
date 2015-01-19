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
import ar.gob.gcaba.dao.RegistroRIBDAO;

@Repository("registroRibDAO")
public class RegistroRIBDAOImpl extends NamedParameterJdbcTemplate implements RegistroRIBDAO {

    @Autowired
    public RegistroRIBDAOImpl(@Qualifier("dataSourceRIB") DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Integer> getListaRegistrosId(Long cuil) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put(RegistrosQueries.CUIL, cuil);
        return this.queryForList(RegistrosQueries.SELECT_REGISTROS_RIB_CUIL, paramsMap, Integer.class);
    }

    @Override
    public List<Integer> getListaRegistrosIdByHogar(Integer idHogar) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put(RegistrosQueries.HOGAR, idHogar);
        return this.queryForList(RegistrosQueries.SELECT_REGISTROS_RIB_HOGAR_REGISTRO, paramsMap, Integer.class);
    }

    @Override
    public Integer agregarDocumentoVinculado(Integer idRegistro, String numeroDocumento, Integer tipoDocumento, String rutaDocumento, Date fechaVencimiento) {

        MapSqlParameterSource paramsMap = new MapSqlParameterSource();

        paramsMap.addValue(RegistrosQueries.NUMERO, numeroDocumento);
        paramsMap.addValue(RegistrosQueries.RUTA, rutaDocumento);
        paramsMap.addValue(RegistrosQueries.TIPO_DOCUMENTO, tipoDocumento);
        paramsMap.addValue(RegistrosQueries.FECHA_VENCIMIENTO, fechaVencimiento);
        paramsMap.addValue(RegistrosQueries.REGISTRO_DOCUMENTO, idRegistro);
        
        String keyColumn = "ID_DOCUMENTO_VINCULADO"; 
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.update(RegistrosQueries.INSERT_DOCUMENTO_VICULADO_RIB, paramsMap , keyHolder, new String[]{keyColumn});
        
        BigDecimal idDocumento = (BigDecimal) keyHolder.getKeys().get(keyColumn);
        
        return idDocumento.intValue();

    }

    @Override
    public void insertarDocumentoProcesado(String docsViculadosIds, String filename, String estado, String mensajeError) {
        
        Map<String, Object> paramsMap = new HashMap<String, Object>();

        paramsMap.put(RegistrosQueries.DOCUMENTOS_ID, docsViculadosIds);
        paramsMap.put(RegistrosQueries.NOMBRE_ARCHIVO, filename);
        paramsMap.put(RegistrosQueries.ESTADO, estado);
        paramsMap.put(RegistrosQueries.MENSAJE_ERROR, mensajeError);
//        paramsMap.put(RegistrosQueries.FECHA_ACTUAL, new java.sql.Date((new Date()).getTime()));
        this.update(RegistrosQueries.INSERT_AUDITORIA_DOCUMENTOS_RIB, paramsMap );
    }


}
