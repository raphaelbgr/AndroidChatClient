package net.sytes.surfael.data;

import net.sytes.surfael.api.model.clients.Client;

/**
 * Created by raphaelb.rocha on 29/01/2016.
 */
public class ClientProxy {
    private String login;
    private String name;
    private String md5Password;

    public ClientProxy(Client client) {
        login = client.getLogin();
        name = client.getName();
        md5Password = client.getMD5Password();
    }

    public Client buildClient(ClientProxy clientProxy) {
        Client client = new Client();

        client.setLogin(clientProxy.login);
        client.setMD5Password(clientProxy.md5Password);
        client.setName(clientProxy.name);

        return client;
    }
}
