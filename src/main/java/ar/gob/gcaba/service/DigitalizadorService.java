package ar.gob.gcaba.service;

import java.io.File;
import java.util.List;

public interface DigitalizadorService {

	void procesarLecturaArchivos(List<File> pdfFiles, String tipoRegistro);
	
	List<File> obtenerListaPDFFiles(File folder);
}