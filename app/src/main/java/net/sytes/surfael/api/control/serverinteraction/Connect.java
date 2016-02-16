package net.sytes.surfael.api.control.serverinteraction;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import net.sytes.surfael.api.control.sync.ClientStream;
import net.sytes.surfael.api.control.sync.Status;
import net.sytes.surfael.api.model.clients.Client;

public class Connect {

	public Connect(String ip, int port) throws UnknownHostException, IOException {
		ClientStream stream = ClientStream.getInstance();
		Socket socket = new Socket(ip, port);
		stream.setSock(socket);
		Status.getInstance().setConnected(true);
	}
	
	public Connect(Client c) throws UnknownHostException, IOException {
		ClientStream stream = ClientStream.getInstance();
		stream.setSock(new Socket(c.getTargetIp(), c.getTargetPort()));
		Status.getInstance().setConnected(true);

		//CONNECTION MESSAGE
		stream.sendObject(c);
	}
}
