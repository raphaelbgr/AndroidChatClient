package net.sytes.surfael.api.control.serverinteraction;

import java.io.IOException;
import java.net.UnknownHostException;

import net.sytes.surfael.api.control.sync.ClientStream;
import net.sytes.surfael.api.control.sync.Status;
import net.sytes.surfael.api.model.clients.Client;
import net.sytes.surfael.api.model.clients.NewClient;
import net.sytes.surfael.api.model.exceptions.LocalException;

public class Register {
	public Register(Object o) throws IOException, LocalException {
		if (Status.getInstance().isConnected()) {
			if (o instanceof Client) {
				if(Status.getInstance().isConnected()) {
					ClientStream.getInstance().sendObject((NewClient)o);	//SENDS THE MESSAGE
				}
			} else throw new LocalException("Must be an instance of Client class, to send this object.");
		} else {
			throw new LocalException("Not connected.");
		}
	}
	
	public boolean register(Object o) throws UnknownHostException, IOException, LocalException {	
		if (Status.getInstance().isConnected()) {
			if (o instanceof Client) {
				if(Status.getInstance().isConnected()) {
					ClientStream.getInstance().sendObject((NewClient)o);	//SENDS THE MESSAGE
					return true;
				} else {
					return false;
				}
			} else throw new LocalException("Must be an instance of Client class, to send this object.");
		} else throw new LocalException("Not connected.");
	}
}
