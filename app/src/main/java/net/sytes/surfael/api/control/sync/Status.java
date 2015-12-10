package net.sytes.surfael.api.control.sync;

public class Status {

	private boolean connected;
	
	private boolean aux1;
//	private boolean aux2;
//	private boolean aux3;
//	private boolean aux4;
//	
//	private String aux5;
//	private String aux6;
//	private String aux7;
//	private String aux8;
//	
//	private int aux9;
//	private int aux10;
//	private int aux11;
//	private int aux12;
	
	public static final String VERSION = "0.9.21";
	
	private static Status instance;
	public static Status getInstance() {
		if (instance == null) {
			instance = new Status();
		}
		return instance;
	}
	
	public boolean isConnected() {
		return connected;
	}
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	public boolean isAux1() {
		return aux1;
	}
	public void setAux1(boolean aux1) {
		this.aux1 = aux1;
	}
}
