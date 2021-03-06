package net.sytes.surfael.api;

import android.app.Activity;
import android.widget.EditText;

import net.sytes.surfael.api.control.serverinteraction.Connect;
import net.sytes.surfael.api.control.serverinteraction.Disconnect;
import net.sytes.surfael.api.control.serverinteraction.Send;
import net.sytes.surfael.api.control.sync.Status;
import net.sytes.surfael.api.interfaces.DisconnectCallback;
import net.sytes.surfael.api.model.clients.Client;
import net.sytes.surfael.api.model.exceptions.LocalException;
import net.sytes.surfael.api.model.messages.Message;
import net.sytes.surfael.api.model.messages.NormalMessage;
import net.sytes.surfael.api.model.messages.ServerMessage;
import net.sytes.surfael.data.Session;

import java.io.IOException;
import java.util.Calendar;

public class ApiSendFacade {

	private static Thread t1;
	private static ApiReceiveFromServerThread apiReceiver;
	private static ApiReceiveInterface apiBridge;
	private static Activity context;

	public static void setContext(Activity context) {
		ApiSendFacade.context = context;
	}

	public static boolean send(Object o) {
		try {
			new Send(o);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void sendNormalMessageAsync(ApiReceiveInterface apiri,
											  final EditText editText, final String message) {
//		apiBridge = apiri;

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						NormalMessage nm = new NormalMessage();
						nm = (NormalMessage) populateMessage(nm, message);
						new Send(nm);
						context.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								editText.setText("");
							}
						});
						break;
					} catch (Exception e) {
						e.printStackTrace();
						if (apiBridge != null)
							apiBridge.onConnectionError(e);
						if (!Status.getInstance().isConnected())
							reconnectAsync();
					} finally {
						threadSpleep(5000);
					}
				}
			}
		});
		t1.start();
	}

	public static void reconnectAsync() {
		if (Status.getInstance().isConnected())
			killService();
		if (apiReceiver != null) {
			Thread t2 = new Thread(new Runnable() {
				@Override
				public void run() {
					while (Status.getInstance().isConnected() == false) {
						try {
							new Connect(Session.SERVER_IP, Session.SERVER_PORT);
							t1 = new Thread(apiReceiver);
							t1.start();
							Status.getInstance().setConnected(true);
							requestHistory(5000);
							if (apiBridge != null)
								apiBridge.onConnected();
						} catch (Exception e) {
							e.printStackTrace();
							if (apiBridge != null)
								apiBridge.onConnectionError(e);
						}
					}
				}
			});
			t2.start();
		}
	}

	public static void connect(String ip, int port, ApiReceiveInterface apiBridge,
							   String mEmail, String mPassword, boolean crypt) throws LocalException, IOException {
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

	public static void connectFacebookAsync(final String ip, final int port,
											final ApiReceiveInterface apiBridge,
											final Client client, final Activity context)
			throws LocalException, IOException {
		ApiSendFacade.apiBridge = apiBridge;

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
				} catch (final Exception e) {
					e.printStackTrace();
					apiBridge.onConnectionError(e);
				}
			}
		});
		t2.start();
	}

	public static void aSyncConnect(final String ip, final int port,
									final ApiReceiveInterface apiBridge, final String mEmail,
									final String mPassword, final boolean crypt) throws LocalException, IOException {
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					connect(ip, port, apiBridge, mEmail, mPassword, crypt);
					requestHistory(5000);
				} catch (Exception e) {
					e.printStackTrace();
					apiBridge.onConnectionError(e);
				}
			}
		});
		t1.start();

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

	public static void disconnectAsync(final DisconnectCallback callback) {
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					new Disconnect();
					callback.onSuccess();
				} catch (IOException e) {
					e.printStackTrace();
					callback.onFailure();
				} finally {
					ApiSendFacade.killService();
				}
			}
		});
		t1.start();
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

	private static void threadSpleep(int milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
