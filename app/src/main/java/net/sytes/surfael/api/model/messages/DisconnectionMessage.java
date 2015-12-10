package net.sytes.surfael.api.model.messages;



public class DisconnectionMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5000337873561587678L;

	@Override
	public String toString() {
		return "[" + this.getTimestamp() + "] " + this.getOwnerLogin() + " -> " + "Disconnected";
	}
	
	public DisconnectionMessage(boolean toDisconnect) {
		setDisconnect(toDisconnect);
	}
	
	public DisconnectionMessage() {
		
	}
	
}
