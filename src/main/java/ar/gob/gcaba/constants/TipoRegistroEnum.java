package ar.gob.gcaba.constants;

public enum TipoRegistroEnum {
	
	RIB("RIB"),
	LUE("LUE");
	
	private String id;
	TipoRegistroEnum(String tipo){
		this.id = tipo;
	}
	public String getTipo() {
		return id;
	}
	public void setTipo(String id) {
		this.id = id;
	}
}


