package net.sytes.surfael.api.control.sync;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class is supposed to store the socket of the connection generated with the server.
 * It is intended to be a Singleton.
 * @author raphael.bernardo
 */
public class ClientStream {

	private Socket sock = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;

	public Socket getSock() {
		return sock;
	}
	public void setSock(Socket sock) {
		this.sock = sock;
	}

	//SINGLETON PATTERN BLOCK
	private static ClientStream instance;
	public static ClientStream getInstance() {
		if (instance == null){
			instance = new ClientStream();
		}
		return instance;
	}
	private ClientStream() {
	}

	public void sendObject(Object o) throws IOException {
		if (sock != null) {
			if (this.sock.getOutputStream() != null) {
				oos = new ObjectOutputStream(this.sock.getOutputStream());
				oos.writeObject(o);
				oos.flush();
			}
		}
	}

	public Object receiveMessage() throws IOException, ClassNotFoundException {
		InputStream is = sock.getInputStream();
		ois = new ObjectInputStream(is);
		Object o = ois.readObject();
		return o;
	}

	public boolean checkOnlineStatus() {
		if(this.sock != null && this.sock.isConnected()) {
			Status.getInstance().setConnected(true);
			return true;
		} else {
			Status.getInstance().setConnected(false);
			return false;
		}
	}

}
