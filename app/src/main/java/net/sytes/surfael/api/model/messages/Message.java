package net.sytes.surfael.api.model.messages;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class Message implements Serializable, Comparable<Message> {

	/**
	 * Object Serial Version to send via TCP and WriteFile
	 */
	private static final long serialVersionUID = -8905875007093416665L;

	private Set<String> receivedby = new HashSet<String>();

	private Date msg_DateCreated;
	private Timestamp serverReceivedTimeSQLDate;

	private String version;
	private int messageServerCount;
	private int messageOwnerCount;

	private long ownerID;
	private long creationTime;

	private Date serverReceivedTimeDate;
	private String serverReceivedTimeString;

	//	private ClientSeenTime [] cst;
	private Vector<String> onlineUserList;

	private boolean disconnect = false;
	private boolean error = false;
	private boolean connect = false;

	private String compilationKey = null;

	private String addUser;
	private String delUser;
	private String ownerLogin;
	private String text;
	private String timestamp;

	private String date;
	private String ownerName;

	private String ip;
	private String port;
	private String pcname;
	private String delay;
	private String network;
	private String type;
	private String servresponse;
	private String dnsHostName;

	private String senderPhotoUrl;

	private Object msg_DateCreatedSQL;

	private Long serverReceivedTimeLong;

	private int senderId;

	public Set<String> getSeen() {
		return receivedby;
	}
	public void addSeen(String name) {
		this.receivedby.add(name);
	}
	public String getOwnerLogin() {
		return ownerLogin;
	}
	public void setOwnerLogin(String ownerLogin) {
		this.ownerLogin = ownerLogin;
	}
	public String getText() {
		return text;
	}
	public long getOwnerID() {
		return ownerID;
	}
	public void setOwnerID(long ownerID) {
		this.ownerID = ownerID;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp() {
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		setMsgCreationDate(Calendar.getInstance().getTime());
		String dateFormatted = formatter.format(getMsgCreationDate()); //Extrair para metodo na Mensagem
		this.timestamp = dateFormatted;
	}
	public String getDateString() {
		return date;
	}
	public void setDateString() {
		DateFormat formatter = new SimpleDateFormat("dd/M/yyyy");
		setMsgCreationDate(Calendar.getInstance().getTime());
		String dateFormatted = formatter.format(getMsgCreationDate()); //Extrair para metodo na Mensagem
		this.date = dateFormatted;
	}
	public String getVersion() {
		return version;
	}
	public Vector<String> getOnlineUserList() {
		return onlineUserList;
	}
	public boolean isConnect() {
		return connect;
	}
	public void setConnect(boolean connect) {
		this.connect = connect;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public void setOnlineUserList(Vector<String> listData) {
		this.onlineUserList = listData;
	}
	public void setVersion(String VERSION) {
		this.version = VERSION;
	}
	public String getIp() {
		return ip;
	}
	public void setAddUser(String addUser) {
		this.addUser = addUser;
	}
	public void setDelUser(String s) {
		this.delUser = s;
	}
	public String getAddUser() {
		return this.addUser;
	}
	public String getDelUser() {
		return this.delUser;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getPcname() {
		return pcname;
	}
	public void setPcname(String pcname) {
		this.pcname = pcname;
	}
	public String getDelay() {
		return delay;
	}
	public boolean isDisconnect() {
		return disconnect;
	}
	public void setDisconnect(boolean disconnect) {
		this.disconnect = disconnect;
	}
	public void setDelay(String delay) {
		this.delay = delay;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(int i) {
		if ( i == 0 ) {
			this.network = "Loopback";
		} else if (i == 1){
			this.network = "Local";
		} else {
			this.network = "Internet";
		}
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getServresponse() {
		return servresponse;
	}
	public void setServresponse(String servresponse) {
		this.servresponse = servresponse;
	}
	public long getCreationtime() {
		return creationTime;
	}
	public void setCreationtime(long creationtime) {
		this.creationTime = creationtime;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 		: ip.hashCode());
		result = prime * result + ((network == null) ? 0 	: network.hashCode());
		result = prime * result + ((ownerLogin == null) ? 0 : ownerLogin.hashCode());
		result = prime * result + ((pcname == null) ? 0 	: pcname.hashCode());
		return result;
	}

	/**
	 * Sugestao do Daniel Oliveira, de comparacao de objeto mensagem, excelente.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Message))
			return false;
		Message other = (Message) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (network == null) {
			if (other.network != null)
				return false;
		} else if (!network.equals(other.network))
			return false;
		if (ownerLogin == null) {
			if (other.ownerLogin != null)
				return false;
		} else if (!ownerLogin.equals(other.ownerLogin))
			return false;
		if (pcname == null) {
			if (other.pcname != null)
				return false;
		} else if (!pcname.equals(other.pcname))
			return false;
		return true;
	}

	/**
	 * Sugestao do Daniel Olivera (Muito boa) de impressao direta do objeto via toString();
	 */
	@Override
	public String toString() {
		return "[" + this.getTimestamp() + "] " + this.getOwnerName() + " -> " + this.getText();
	}

	@Override
	public int compareTo(Message m) {
		if (this.getCreationtime() > m.getCreationtime()) {
			return 1;
		} else if (this.getCreationtime() < m.getCreationtime()){
			return -1;
		} else {
			return 0;
		}
	}
	//AUTOMATICALLY SETS ITS CREATION TIME
	public Message () {
		this.setCreationtime(Calendar.getInstance().getTimeInMillis());
		setTimestamp();
	}
	public Date getMsgCreationDate() {
		return this.msg_DateCreated;
	}
	public void setMsgCreationDate(Date msg_Date) {
		this.msg_DateCreated = msg_Date;
		this.msg_DateCreatedSQL = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
	}

	public Message buildDisconnectMessage() {
		this.setDisconnect(true);
		this.setOwnerLogin(getOwnerLogin());
		this.setCreationtime(Calendar.getInstance().getTimeInMillis());
		this.setIp(getIp());
		this.setType("disconnect");
		this.setPcname(getPcname());
		this.setTimestamp();
		this.setDateString();
		return this;
	}

	public Message buildConnectMessage() {
		this.setOwnerLogin(getOwnerLogin());
		this.setCreationtime(Calendar.getInstance().getTimeInMillis());
		this.setType("connectreq");
		this.setPcname(getPcname());
		this.setTimestamp();
		this.setDateString();
		return this;
	}

	public Message (String owner, String ip, String Message) {
		setOwnerLogin(owner);
		setText(Message);
		setIp(ip);
		setCreationtime(Calendar.getInstance().getTimeInMillis());
	}

	public Message (String owner, String Message) {
		setText(Message);
		setOwnerLogin(owner);
		setIp("No IP");
		setCreationtime(Calendar.getInstance().getTimeInMillis());
	}

	public Message (String Message) {
		setOwnerLogin("anonymous");
		setOwnerName("Anonymous Owner");
		setIp("No IP");
		setText(Message);
		setCreationtime(Calendar.getInstance().getTimeInMillis());
	}
	public String getServerReceivedtimeString() {
		return serverReceivedTimeString;
	}
	public Date getServerReceivedtimeDate() {
		return serverReceivedTimeDate;
	}
	public void setServerReceivedtime() {
		this.serverReceivedTimeSQLDate = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
		this.serverReceivedTimeDate = Calendar.getInstance().getTime();
		this.serverReceivedTimeString = Calendar.getInstance().getTime().toString();
		this.serverReceivedTimeLong = Calendar.getInstance().getTimeInMillis();
	}
	public int getMessageServerCount() {
		return messageServerCount;
	}
	public void setMessageServerCount(int messageServerCount) {
		this.messageServerCount = messageServerCount;
	}
	public int getMessageOwnerCount() {
		return messageOwnerCount;
	}
	public void setMessageOwnerCount(int messageOwnerCount) {
		this.messageOwnerCount = messageOwnerCount;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public Date getServerReceivedTimeDate() {
		return serverReceivedTimeDate;
	}
	public void setServerReceivedTimeDate(Date serverReceivedTimeDate) {
		this.serverReceivedTimeDate = serverReceivedTimeDate;
	}
	public java.sql.Timestamp getServerReceivedTimeSQLDate() {
		return serverReceivedTimeSQLDate;
	}
	public void setServerReceivedTimeSQLDate(java.sql.Timestamp serverReceivedTimeSQLDate) {
		this.serverReceivedTimeSQLDate = serverReceivedTimeSQLDate;
	}
	public Object getMsg_DateCreatedSQL() {
		return msg_DateCreatedSQL;
	}
	public String getCompilationKey() {
		return compilationKey;
	}
	public void setCompilationKey(String compilationKey) {
		this.compilationKey = compilationKey;
	}
	public Long getServerReceivedTimeLong() {
		return serverReceivedTimeLong;
	}
	public String getDnsHostName() {
		return dnsHostName;
	}
	public void setDnsHostName(String dnsHostName) {
		this.dnsHostName = dnsHostName;
	}
	public void setSenderId(int id) {
		this.senderId = id;
	}
	public int getSenderId() {
		return this.senderId;
	}
	public String getSenderPhotoUrl() {
		return senderPhotoUrl;
	}

	public void setSenderPhotoUrl(String senderPhotoUrl) {
		this.senderPhotoUrl = senderPhotoUrl;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}