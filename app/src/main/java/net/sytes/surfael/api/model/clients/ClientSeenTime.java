package net.sytes.surfael.api.model.clients;

import net.sytes.surfael.api.model.messages.Message;


public class ClientSeenTime extends Client {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1931296166164777661L;
	
	private Message message;
	private long seenTime;
	private boolean received = false;
	
	
	public boolean isReceived() {
		return received;
	}
	
	public void setReceived(boolean received) {
		this.received = received;
	}
	public boolean isSeen() {
		return seen;
	}
	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	private boolean seen = false;
	
	public Message getM() {
		return message;
	}
	public void setM(Message m) {
		this.message = m;
	}
	public long getSeenTime() {
		return seenTime;
	}
	public void setSeenTime(long seenTime) {
		this.seenTime = seenTime;
	}
	
}
