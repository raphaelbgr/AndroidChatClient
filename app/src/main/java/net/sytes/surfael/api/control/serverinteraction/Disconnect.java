package net.sytes.surfael.api.control.serverinteraction;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sytes.surfael.api.control.sync.ClientStream;
import net.sytes.surfael.api.interfaces.DisconnectCallback;
import net.sytes.surfael.api.model.clients.Client;
import net.sytes.surfael.api.model.messages.DisconnectionMessage;

public class Disconnect {

	public Disconnect() throws UnknownHostException, IOException {
		ClientStream stream = ClientStream.getInstance();
		DisconnectionMessage dm = new DisconnectionMessage();
		dm = (DisconnectionMessage) dm.buildDisconnectMessage();
		stream.sendObject(dm);
	}

	public Disconnect(Client c) throws UnknownHostException, IOException {
		ClientStream stream 	= ClientStream.getInstance();
		DisconnectionMessage dm = new DisconnectionMessage();
		dm = (DisconnectionMessage) dm.buildDisconnectMessage();
		dm.setOwnerName(c.getName());
		stream.sendObject(dm);
	}

	@SuppressWarnings("unused")
	private String getTimestamp() {
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String dateFormatted = formatter.format(new Date());
		return "["+dateFormatted+"]" + " ";
	}

}
