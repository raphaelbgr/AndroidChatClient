package net.sytes.surfael.api;

import net.sytes.surfael.api.model.clients.Client;
import net.sytes.surfael.api.model.exceptions.ServerException;
import net.sytes.surfael.api.model.messages.DisconnectionMessage;
import net.sytes.surfael.api.model.messages.History;
import net.sytes.surfael.api.model.messages.Message;
import net.sytes.surfael.api.model.messages.NormalMessage;
import net.sytes.surfael.api.model.messages.ServerMessage;

public interface ApiReceiveInterface {
	
	public void onReceive(Object o);

	public void onReceiveNormalMessage(NormalMessage o);

	public void onReceiveDisconnectionMessage(DisconnectionMessage o);

	public void onReceiveServerMessage(ServerMessage o);

	public void onReceiveClient(Client o);
	
	public void onReceiveServerException(ServerException o);

	public void onConnectionError(Exception e);

	public void onUserMessageReceived(Message m);

	public void onReceiveServerHistory(History h);
}
