package net.sytes.surfael.api;

import java.io.IOException;

import net.sytes.surfael.api.ApiReceiveInterface;
import net.sytes.surfael.api.ApiSendFacade;
import net.sytes.surfael.api.control.sync.ClientStream;
import net.sytes.surfael.api.control.sync.Status;
import net.sytes.surfael.api.model.clients.Client;
import net.sytes.surfael.api.model.exceptions.ServerException;
import net.sytes.surfael.api.model.messages.DisconnectionMessage;
import net.sytes.surfael.api.model.messages.History;
import net.sytes.surfael.api.model.messages.Message;
import net.sytes.surfael.api.model.messages.NormalMessage;
import net.sytes.surfael.api.model.messages.ServerMessage;

public class ApiReceiveFromServerThread implements Runnable {

	private Client fbClient;
	private ClientStream stream = ClientStream.getInstance();
	private ApiReceiveInterface api;
	private boolean suicide = false;
	private String email;
	private String password;
	private boolean crypt;

	@Override
	public void run() {
		Status.getInstance().setConnected(true);

		if (fbClient != null) {
			ApiSendFacade.loginFacebook(fbClient);
		} else {
			ApiSendFacade.login(email, password, crypt);
		}

		while (!suicide) {
			try {
				final Object o = stream.receiveMessage();
				api.onReceive(o);
				if (o instanceof Message) {
					if (o instanceof NormalMessage) {
						api.onReceiveNormalMessage((NormalMessage) o);
					} else if (o instanceof DisconnectionMessage) {
						Status.getInstance().setConnected(false);
						Status.getInstance().setLoggedIn(false);
						api.onReceiveDisconnectionMessage((DisconnectionMessage) o);
						break;
					} else if (o instanceof ServerMessage) {
						api.onReceiveServerMessage((ServerMessage) o);
					}
				} else if (o instanceof Client) {
					Status.getInstance().setLoggedIn(true);
					api.onReceiveClient((Client) o);
				} else if (o instanceof ServerException) {
					api.onReceiveServerException((ServerException) o);
				} else if (o instanceof History) {
					api.onReceiveServerHistory((History) o);
				}
			} catch (ClassNotFoundException | IOException e) {
				Status.getInstance().setConnected(false);
				Status.getInstance().setLoggedIn(false);
				e.printStackTrace();
				suicide = true;
				api.onConnectionError(e);
			}
		}
		suicide = !Status.getInstance().isConnected();
	}
	
	public ApiReceiveFromServerThread(ApiReceiveInterface apiBridge) {
		this.api = apiBridge;
	}

	public ApiReceiveFromServerThread() {
	}

	public void overwriteListener(ApiReceiveInterface apiBridge) {
		this.api = apiBridge;
	}

	public void killThread() {
		suicide = true;
	}

	public void setFacebookClient(Client client) {
		this.fbClient = client;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setCrypt(boolean crypt) {
		this.crypt = crypt;
	}
}
