package net.sytes.surfael.api.model.messages;


public class RegistrationMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7434112765110210052L;

	private String dbCryptKey = null;
	private String dbPass = null;
	private String dbUser = null;
	private String dbAddr = null;

	public String getDbCryptKey() {
		return dbCryptKey;
	}

	public void setDbCryptKey(String dbCryptKey) {
		this.dbCryptKey = dbCryptKey;
	}

	public String getDbPass() {
		return dbPass;
	}

	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbAddr() {
		return dbAddr;
	}

	public void setDbAddr(String dbAddr) {
		this.dbAddr = dbAddr;
	}
	
}
