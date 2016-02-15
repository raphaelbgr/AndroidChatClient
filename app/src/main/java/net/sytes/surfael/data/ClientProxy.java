package net.sytes.surfael.data;

import net.sytes.surfael.api.model.clients.Client;

import java.util.Date;

/**
 * Created by raphaelb.rocha on 29/01/2016.
 */
public class ClientProxy {
    private Date birthdate;
    private int id;

    private String email;
    private String login;
    private String name;
    private String md5Password;
    private String sex;
    private String photoUrl;
    private String fbToken;

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
        sex = client.getSex();
        if (client.getBirthDate() != null) {
            birthdate = client.getBirthDate();
        }
        if (client.getPhotoUrl() != null) {
            photoUrl = client.getPhotoUrl();
        }
        if (client.getFbToken() != null) {
            fbToken = client.getFbToken();
        }
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
        if (clientProxy.fbToken != null) {
            client.setFbToken(clientProxy.fbToken);
        }
        if (clientProxy.photoUrl != null) {
            client.setPhotoUrl(clientProxy.photoUrl);
        }
        if (clientProxy.sex != null) {
            client.setSex(clientProxy.sex);
        }

        return client;
    }
}
