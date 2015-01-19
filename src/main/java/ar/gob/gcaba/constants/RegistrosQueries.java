package ar.gob.gcaba.constants;

public class RegistrosQueries {

	public static final String HOGAR = "id_hogar_cp";
	public static final String CUIL = "cuil";
    public static final String NUMERO = "numero";
    public static final String RUTA = "ruta";
    public static final String TIPO_DOCUMENTO = "tipo_documento";
    public static final String REGISTRO_DOCUMENTO = "registroDocumentoId";
    public static final String FECHA_VENCIMIENTO = "fecha_vencimiento";
    public static final String REFERENCIA = "referencia";
    public static final String ACRONIMO = "acronimo";
    public static final String DOCUMENTOS_ID = "documento_id";
    public static final String NOMBRE_ARCHIVO = "nombre_archivo";
    public static final String ESTADO = "estado";
    public static final String MENSAJE_ERROR = "mensaje_error";
    public static final String FECHA_ACTUAL = "fecha";

	public static final String SELECT_REGISTROS_RIB_CUIL = "SELECT r.id_registro FROM Ru_registro r " +
			"INNER JOIN ru_identificacion i" +
			" ON r.fk_identificacion = i.id_identificacion WHERE i.cuil = :"+CUIL;

	public static final String SELECT_REGISTROS_LUE = "SELECT r.id_registro " +
			"FROM Ru_registro r INNER JOIN ru_identificacion i" +
			" ON r.fk_identificacion = i.id_identificacion WHERE i.cuil = :"+CUIL;

	
	//TODO: decidir cual de estas 2 se queda
	public static final String SELECT_REGISTROS_RIB_HOGAR = "SELECT r.id_registro " +
			"FROM Ru_registro r INNER JOIN ru_domicilio d " +
			" ON r.fk_domicilio = d.id_domicilio WHERE d.id_hogar_cp =:"+HOGAR;
	
	public static final String SELECT_REGISTROS_RIB_HOGAR_REGISTRO = "SELECT id_registro " +
			"FROM Ru_registro WHERE id_hogar_cp =:"+HOGAR;


    public static final String INSERT_DOCUMENTO_VICULADO_RIB = "INSERT INTO RU_Documento_Vinculado (Numero, Ruta, FK_tipo_documentacion, FK_Registro, Fecha_vencimiento) " +
            "VALUES (:"+NUMERO+",:"+RUTA+", :"+TIPO_DOCUMENTO+",:"+REGISTRO_DOCUMENTO+", :"+FECHA_VENCIMIENTO+" )";
    
    public static final String INSERT_DOCUMENTO_VICULADO_LUE = "INSERT INTO RU_Documento_Vinculado " +
            "(Numero, Ruta, FK_tipo_documentacion, FK_Registro, Fecha_vencimiento, Referencia, Acronimo) " +
            "VALUES (:"+NUMERO+",:"+RUTA+", :"+TIPO_DOCUMENTO+",:"+REGISTRO_DOCUMENTO+", :"+FECHA_VENCIMIENTO+", :"+REFERENCIA+",:"+ACRONIMO+" )";
    
    public static final String INSERT_AUDITORIA_DOCUMENTOS_RIB = "INSERT INTO RU_Auditoria_Documentos " +
            "(Nombre_documento, id_documentos_vinculados, estado, mensaje_error) " +
            "VALUES (:"+NOMBRE_ARCHIVO+",:"+DOCUMENTOS_ID+", :"+ESTADO+",:"+MENSAJE_ERROR+")";
    
    public static final String INSERT_AUDITORIA_DOCUMENTOS_LUE = INSERT_AUDITORIA_DOCUMENTOS_RIB;
    
}