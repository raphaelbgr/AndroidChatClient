package net.sytes.surfael.api;

import java.io.IOException;

import net.sytes.surfael.api.control.serverinteraction.Connect;
import net.sytes.surfael.api.control.serverinteraction.Send;
import net.sytes.surfael.api.control.services.ApiReceiveFromServerThread;
import net.sytes.surfael.api.model.clients.Client;
import net.sytes.surfael.api.model.exceptions.LocalException;
import net.sytes.surfael.api.control.sync.Status;

public class ApiSendFacade {
	
	public static boolean send(Object o) {
		try {
			new Send(o);
		} catch (IOException | LocalException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean connect(String ip, int port, ApiReceiveInterface apiBridge, String mEmail, String mPassword) throws LocalException {
		try {
			new Connect(ip, port);
			Thread t1 = new Thread(new ApiReceiveFromServerThread(apiBridge));
			t1.start();
			Status.getInstance().setConnected(true);
			ApiSendFacade.login(mEmail, mPassword);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
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
		Client client = new Client(login, password);
		client.setVersion(Status.VERSION);
		client.setConnect(true);
		client.setPlatform(2);
		ApiSendFacade.send(client);
		return null;
	}
	
}
