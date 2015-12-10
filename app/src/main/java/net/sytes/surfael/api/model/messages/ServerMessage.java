package net.sytes.surfael.api.model.messages;

import java.io.Serializable;
import java.util.Set;

import net.sytes.surfael.api.model.clients.Client;

public class ServerMessage extends Message implements Serializable {

	/**
	 * For server background communication purposes.
	 */
	private static final long serialVersionUID = 3772354662147009097L;
	
	private Set<Client> clist = null;
	private String request = null;
	private int rowLimit;
	private boolean registerSuccess = false;

	public ServerMessage buildMessage(String s) {
		setServresponse(s);
		setTimestamp();
		return this;
	}
	
	public ServerMessage(String s) {
		super();
		setServresponse(s);
		setTimestamp();
	}
	
	public ServerMessage() {
		super();
	}
	
	public ServerMessage(Set<Client> clist) {
		super();
		setClist(clist);
	}

	@Override
	public String toString() {
		return "[" + this.getTimestamp() + "]" + " " + "SERVER> " + this.getServresponse();
	}

	public Set<Client> getClist() {
		return this.clist;
	}

	public void setClist(Set<Client> clist) {
		this.clist = clist;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public int getRowLimit() {
		return rowLimit;
	}

	public void setRowLimit(int rowLimit) {
		this.rowLimit = rowLimit;
	}

	public boolean isRegisterSuccess() {
		return registerSuccess;
	}

	public void setRegisterSuccess(boolean registerSuccess) {
		this.registerSuccess = registerSuccess;
	}
}
