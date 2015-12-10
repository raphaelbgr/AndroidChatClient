package net.sytes.surfael.api.model.messages;


public class NormalMessage extends Message {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5676316381020467805L;

	@Override
	public String toString() {
		return "[" + this.getTimestamp() + "]" + " " + this.getOwnerName() + " -> " + this.getText();
	}
	
}
