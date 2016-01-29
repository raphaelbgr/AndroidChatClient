package net.sytes.surfael.api;

import java.io.IOException;
import java.util.Calendar;

import net.sytes.surfael.api.control.classes.MD5;
import net.sytes.surfael.api.control.serverinteraction.Connect;
import net.sytes.surfael.api.control.serverinteraction.Send;
import net.sytes.surfael.api.control.services.ApiReceiveFromServerThread;
import net.sytes.surfael.api.model.clients.Client;
import net.sytes.surfael.api.model.exceptions.LocalException;
import net.sytes.surfael.api.control.sync.Status;
import net.sytes.surfael.api.model.messages.Message;
import net.sytes.surfael.api.model.messages.NormalMessage;
import net.sytes.surfael.data.Session;

public class ApiSendFacade {

	private static Thread t1;
	private static ApiReceiveFromServerThread apiReceiver;

	public static boolean send(Object o) {
		try {
			new Send(o);
		} catch (IOException | LocalException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean sendNormalMessage(String message) {
		try {
			NormalMessage nm = new NormalMessage();
			nm = (NormalMessage) populateMessage(nm, message);
			new Send(nm);
		} catch (IOException | LocalException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void connect(String ip, int port, ApiReceiveInterface apiBridge, String mEmail, String mPassword) throws LocalException, IOException {
		if (!Status.getInstance().isConnected()) {
			new Connect(ip, port);
			Status.getInstance().setConnected(true);
		}
		apiReceiver = new ApiReceiveFromServerThread(apiBridge);
		t1 = new Thread(apiReceiver);
		t1.start();
		ApiSendFacade.login(mEmail, mPassword);
	}

	public static void overwriteListener(ApiReceiveInterface newListener) {
		if (apiReceiver == null) {
			apiReceiver = new ApiReceiveFromServerThread();
		}
		apiReceiver.overwriteListener(newListener);
	}

	public static boolean connect(String ip, int port, ApiReceiveInterface apiBridge) throws LocalException {
		try {
			new Connect(ip, port);
			Thread t1 = new Thread(new ApiReceiveFromServerThread(apiBridge));
			t1.start();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Client login(String login, String password) {
		Client client = new Client(login, password, true);
		client.setVersion(Status.VERSION);
		client.setConnect(true);
		client.setPlatform(2);
		ApiSendFacade.send(client);
		return null;
	}

	private static Message populateMessage(Message m, String string) {
		m.setOwnerLogin(Session.currentUser.getLogin());
		m.setCreationtime(Calendar.getInstance().getTimeInMillis());
		m.setDateString();
		m.setText(string);
		return m;
	}
	
}
