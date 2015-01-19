package ar.gob.gcaba.exceptions;

public class VinculacionDocumentoException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 115980067851863508L;
    public static String mensajeError = "Error en vinculación del documento: ";

    public VinculacionDocumentoException(String mensajeError) {
        super(mensajeError);
    }

    public VinculacionDocumentoException(Throwable throwable) {
        super(throwable);
    }

    public VinculacionDocumentoException(String mensaje, Throwable throwable) {
        super(mensaje, throwable);
    }

}