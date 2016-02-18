package net.sytes.surfael.api;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;

import net.sytes.surfael.api.control.serverinteraction.Connect;
import net.sytes.surfael.api.control.serverinteraction.Send;
import net.sytes.surfael.api.model.clients.Client;
import net.sytes.surfael.api.model.exceptions.LocalException;
import net.sytes.surfael.api.control.sync.Status;
import net.sytes.surfael.api.model.messages.Message;
import net.sytes.surfael.api.model.messages.NormalMessage;
import net.sytes.surfael.api.model.messages.ServerMessage;
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
	
	public static void connect(String ip, int port, ApiReceiveInterface apiBridge, String mEmail, String mPassword, boolean crypt) throws LocalException, IOException {
		if (Status.getInstance().isConnected()) {
			killService();
		}
		new Connect(ip, port);
		Status.getInstance().setConnected(true);

		apiReceiver = new ApiReceiveFromServerThread(apiBridge);
		apiReceiver.setEmail(mEmail);
		apiReceiver.setPassword(mPassword);
		apiReceiver.setCrypt(crypt);

		t1 = new Thread(apiReceiver);
		t1.start();
	}

	public static void connectFacebookAsync(final String ip, final int port, final ApiReceiveInterface apiBridge, final Client client) throws LocalException, IOException {
		if (Status.getInstance().isConnected()) {
			killService();
		}
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new Connect(ip, port);
					apiReceiver = new ApiReceiveFromServerThread(apiBridge);
					apiReceiver.setFacebookClient(client);

					t1 = new Thread(apiReceiver);
					t1.start();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t2.start();
	}

	public static void aSyncConnect(final String ip, final int port,
									final ApiReceiveInterface apiBridge, final String mEmail,
									final String mPassword, final boolean crypt) throws LocalException, IOException {
		if (!Status.getInstance().isConnected()) {
			Thread t1 = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						connect(ip, port, apiBridge, mEmail, mPassword, crypt);
					} catch (LocalException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			t1.start();
		}
	}

	public static void overwriteListener(ApiReceiveInterface newListener) {
		if (apiReceiver == null) {
			apiReceiver = new ApiReceiveFromServerThread();
		}
		apiReceiver.overwriteListener(newListener);
	}

	public static void killService() {
		apiReceiver.killThread();
	}

	private static void startService() throws LocalException {
		if (apiReceiver == null) {
			throw new LocalException("No listener found, use overwriteListener()");
		} else if (Status.getInstance().isConnected()){
			killService();
		}
		t1 = new Thread(apiReceiver);
		t1.start();
	}

	public static boolean connect(String ip, int port, ApiReceiveInterface apiBridge) throws LocalException {
		if (Status.getInstance().isConnected()){
			killService();
		}
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
	
	public static Client login(String login, String password, boolean crypt) {
		Client client = new Client(login, password, crypt);
		client.setVersion(Status.VERSION);
		client.setConnect(true);
		client.setPlatform(2);
		ApiSendFacade.send(client);
		return null;
	}

	public static Client loginFacebook(Client client) {
		client.setVersion(Status.VERSION);
		client.setConnect(true);
		client.setPlatform(2);
		ApiSendFacade.send(client);
		return client;
	}

	private static Message populateMessage(Message m, String string) {
		m.setOwnerLogin(Session.getCurrentUser().getLogin());
		m.setCreationtime(Calendar.getInstance().getTimeInMillis());
		m.setDateString();
		m.setText(string);
		return m;
	}

	public static boolean requestHistory(int limit) {
		ServerMessage sm = new ServerMessage();
		sm.setRequest("androidhistory");
		sm.setRowLimit(limit);

        return ApiSendFacade.send(sm);
	}

}
