package net.sytes.surfael.api.model.messages;

public class IamAlive extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3393789643684345294L;

	public IamAlive(String login) {
		setOwnerLogin(login);
	}
	
}
