package net.sytes.surfael.api.model.clients;

import java.io.Serializable;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sytes.surfael.api.control.classes.MD5;
import net.sytes.surfael.api.model.messages.Message;


public class Client implements Serializable {

	private static final long serialVersionUID = 296589332172289191L;

	Date registrationDate 		= null;
	Date lastOnline 			= null;
	Date lastMessageSent		= null;

	private int localPort;
	private String version;

	boolean isConnect			= false;
	boolean isDisconnect		= false;

	String text 				= null;
	String name 				= null;
	String middlename 			= null;
	String lastname 			= null;
	String membertype 			= null;

	String login				= null;
	String password				= null;
	String md5Password			= null;
	String email				= null;
	String lastIp				= null;
	String sex					= null;
	String college				= null;
	String startTrimester		= null;
	String city					= null;
	String state				= null;
	String country				= null;
	String course				= null;
	String infnetMail			= null;
	String whatsapp				= null;
	String facebook				= null;
	String twitter				= null;
	String instagram			= null;
	String googleplus			= null;
	String youtube				= null;
	String msn					= null;
	String platform				= null;

	Message lastMessage 		= null;
	List<Message> unSentMsgs	= null;

	int id						= 0;
	int msgCount				= 0;
	int onlinetime 				= 0;
	int messagesSent 			= 0;

	Socket sock 				= null;

	String ip 					= null;
	Integer port 				= null;

	String aux1 				= null;
	String aux2 				= null;
	String aux3 				= null;
	String aux4 				= null;

	public Client(Socket sock) {
		this.sock = sock;
	}
	public Client() {

	}
	public Client (Socket sock, Message m) {
		this.sock 				= sock;
		this.text 				= m.getText();
		this.name 				= m.getOwnerLogin();
		this.membertype 		= m.getType();
		this.lastMessage 		= m;
	}
	public Client(String name) {
		this.name = name;
	}
	public Client(String login, String password) {
		if (login.contains("@"))
		{
			this.email = login;
		} else {
			this.login = login;
		}
		this.password = password;
	}
	public Client(String login, String password, boolean crypt) {
		if (login.contains("@"))
		{
			this.email = login;
		} else {
			this.login = login;
		}
		if (crypt) {
			this.md5Password = MD5.getMD5(password);
		} else {
			this.md5Password = password;
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMsgCount() {
		return msgCount;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMD5Password() {
		return md5Password;
	}
	public void setMD5Password(String md5Password) {
		this.md5Password = md5Password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount + 1;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getMembertype() {
		return membertype;
	}
	public void setMembertype(String membertype) {
		this.membertype = membertype;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date membersince) {
		this.registrationDate = membersince;
	}
	public String getTargetIp() {
		return ip;
	}
	public void setTargetIp(String ip) {
		this.ip = ip;
	}
	public Integer getTargetPort() {
		return port;
	}
	public void setTargetPort(Integer port) {
		this.port = port;
	}
	public int getOnlinetime() {
		return onlinetime;
	}
	public void setOnlinetime(int onlinetime) {
		this.onlinetime = onlinetime;
	}
	public Date getLastOnline() {
		return lastOnline;
	}
	public void setLastOnline() {
		this.lastOnline = Calendar.getInstance().getTime();
	}
	public Socket getSock() {
		return sock;
	}
	public void setSock(Socket sock) {
		this.sock = sock;
	}
	public Message getLastMessage() {
		return lastMessage;
	}
	public void setLastMessage(Message lastMessage) {
		this.lastMessage = lastMessage;
	}
	public Date getLastMessageSent() {
		return lastMessageSent;
	}
	public void setLastMessageTime(Date lastMessageTime) {
		this.lastMessageSent = lastMessageTime;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		if (this.getCollege().equalsIgnoreCase("infnet")) {
			return this.name + " / " + this.getCourse() + this.getStartTrimester().substring(0, 4);
		} else {
			return this.name + " / " + this.getCollege();
		}
	}
	public int getLocalPort() {
		return localPort;
	}
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}
	public String getLastIp() {
		return lastIp;
	}
	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public List<Message> getUnSentMsgs() {
		return unSentMsgs;
	}
	public void setUnSentMsgs(List<Message> unSentMsgs) {
		this.unSentMsgs = unSentMsgs;
	}
	public int getMessagesSent() {
		return messagesSent;
	}
	public void setMessagesSent(int messagesSent) {
		this.messagesSent = messagesSent;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public void setLastOnline(Date lastOnline) {
		this.lastOnline = lastOnline;
	}
	public void setLastMessageSent(Date lastMessageSent) {
		this.lastMessageSent = lastMessageSent;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public String getStartTrimester() {
		return startTrimester;
	}
	public void setStartTrimester(String startTrimester) {
		this.startTrimester = startTrimester;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getAux1() {
		return aux1;
	}
	public void setAux1(String aux1) {
		this.aux1 = aux1;
	}
	public String getAux2() {
		return aux2;
	}
	public void setAux2(String aux2) {
		this.aux2 = aux2;
	}
	public String getAux3() {
		return aux3;
	}
	public void setAux3(String aux3) {
		this.aux3 = aux3;
	}
	public String getAux4() {
		return aux4;
	}
	public void setAux4(String aux4) {
		this.aux4 = aux4;
	}
	public String getInfnetMail() {
		return infnetMail;
	}
	public void setInfnetMail(String infnetMail) {
		this.infnetMail = infnetMail;
	}
	public String getWhatsapp() {
		return whatsapp;
	}
	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public String getTwitter() {
		return twitter;
	}
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	public String getInstagram() {
		return instagram;
	}
	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}
	public String getGoogleplus() {
		return googleplus;
	}
	public void setGoogleplus(String googleplus) {
		this.googleplus = googleplus;
	}
	public String getYoutube() {
		return youtube;
	}
	public void setYoutube(String youtube) {
		this.youtube = youtube;
	}
	public String getMsn() {
		return msn;
	}
	public void setMsn(String msn) {
		this.msn = msn;
	}
	public boolean isConnect() {
		return isConnect;
	}
	public void setConnect(boolean isConnect) {
		this.isConnect = isConnect;
	}
	public boolean isDisconnect() {
		return isDisconnect;
	}
	public void setDisconnect(boolean isDisconnect) {
		this.isDisconnect = isDisconnect;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(int i) {
		switch (i) {
			case 0:
				platform = "desktop";
				break;
			case 1:
				platform = "web";
				break;
			case 2:
				platform = "android";
				break;
		}
	}

}
