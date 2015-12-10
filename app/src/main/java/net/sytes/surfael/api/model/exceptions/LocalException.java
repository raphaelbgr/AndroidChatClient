package net.sytes.surfael.api.model.exceptions;

public class LocalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String s = null;
	
	public LocalException(String s) {
		this.s = s;
	}
	
	public String getMessage() {
		return this.s;
	}
	
}
