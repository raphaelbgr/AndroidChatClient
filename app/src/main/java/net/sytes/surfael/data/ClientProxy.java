package net.sytes.surfael.data;

import net.sytes.surfael.api.model.clients.Client;

/**
 * Created by raphaelb.rocha on 29/01/2016.
 */
public class ClientProxy {
    private int id;

    private String email;
    private String login;
    private String name;
    private String md5Password;

    public ClientProxy(Client client) {
        if (client.getLogin() != null && client.getLogin().contains("@")) {
            email = client.getLogin();
        } else if  (client.getEmail() != null && client.getEmail().contains("@")) {
            email = client.getEmail();
        } else {
            login = client.getLogin();
        }
        id = client.getId();
        name = client.getName();
        md5Password = client.getMD5Password();
    }

    public Client buildClient(ClientProxy clientProxy) {
        Client client = new Client();

        if (email != null) {
            client.setEmail(email);
        } else if (login != null) {
            client.setLogin(login);
        }

        client.setId(clientProxy.id);
        client.setLogin(clientProxy.login);
        client.setMD5Password(clientProxy.md5Password);
        client.setName(clientProxy.name);

        return client;
    }
}
