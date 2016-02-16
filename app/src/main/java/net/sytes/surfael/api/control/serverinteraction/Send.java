package net.sytes.surfael.api.control.serverinteraction;

import java.io.IOException;
import java.net.UnknownHostException;

import net.sytes.surfael.api.control.sync.ClientStream;
import net.sytes.surfael.api.control.sync.Status;
import net.sytes.surfael.api.model.exceptions.LocalException;

public class Send {

	ClientStream stream = ClientStream.getInstance();

	public Send(Object o) throws IOException, LocalException {
		if (Status.getInstance().isConnected()) {
			if(Status.getInstance().isConnected()) {
				stream.sendObject(o);	//SENDS THE MESSAGE
			}
		} else {
			throw new LocalException("Not connected.");
		}
	}

	public boolean send(Object o) throws UnknownHostException, IOException, LocalException {
		if (Status.getInstance().isConnected()) {
			if(Status.getInstance().isConnected()) {
				stream.sendObject(o);	//SENDS THE MESSAGE
				return true;
			} else {
				return false;
			}
		} else {
			throw new LocalException("Not connected.");
		}
	}

}
