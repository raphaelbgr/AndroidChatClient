package net.sytes.surfael.api.control.sync;

public class Status {

	private boolean connected = false;
	
	public static final String VERSION = "0.9.21";
	
	private static Status instance;
	private boolean loggedin;

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
	public void setLoggedIn(boolean loggedin) {
		this.loggedin = loggedin;
	}
	public boolean isLoggedin() {
		return loggedin;
	}
}
