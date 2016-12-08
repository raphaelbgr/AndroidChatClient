package net.sytes.surfael.api.model.exceptions;

public class ServerException extends Exception {

	/**
	 * 	Serializable Exception
	 */
	private static final long serialVersionUID = -4931885893331292884L;
	private boolean toDisconnect = false;
	
	String s = null;
	
	public ServerException(String s, boolean toDisconnect) {
		this.s = s;
		this.toDisconnect = toDisconnect;
	}
	
	public ServerException(String s) {
		this.s = s;
	}
	
	public String getMessage() {
		return this.s;
	}
	
	@Override
	public String toString() {
		return this.s;
	}

	public boolean isToDisconnect() {
		return toDisconnect;
	}

	public void setToDisconnect(boolean toDisconnect) {
		this.toDisconnect = toDisconnect;
	}
	
}
