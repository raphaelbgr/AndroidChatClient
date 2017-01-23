package net.sytes.surfael.data;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

import net.sytes.surfael.api.model.clients.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raphael on 10/12/2015.
 */
public class Session {
//    public static final String SERVER_IP = "ec2-52-67-226-170.sa-east-1.compute.amazonaws.com";
    public static final String SERVER_IP = "10.10.1.76";
//    public static final String SERVER_IP = "10.5.5.132";
//    public static final String SERVER_IP = "192.168.1.11";

    public static final int SERVER_PORT = 2001;

    private static Client currentUser;
    public static List<MessageProxy> messageList;

    public static void startHawk(Context context) {
        Hawk.init(context).build();
    }

    public static void setCurrentUser(Client client) {
        ClientProxy clientProxy = new ClientProxy(client);
        currentUser = client;

        Hawk.delete("currentUser");
        Hawk.put("currentUser", clientProxy);
    }

    public static Client getCurrentUser() {
        if (currentUser == null) {
            if (Hawk.contains("currentUser")) {
                ClientProxy clientProxy = Hawk.get("currentUser");
                if (clientProxy != null)
                    currentUser = clientProxy.buildClient(clientProxy);
            }
        }
        return currentUser;
    }

    public static Client mergeFacebookClient(Client client, Client fbClient) {
        if (fbClient.getBirthDate() != null) {
            client.setBirthDate(fbClient.getBirthDate());
        }
        if (fbClient.getFbToken() != null) {
            client.setFbToken(fbClient.getFbToken());
        }
        if (fbClient.getPhotoUrl() != null) {
            client.setPhotoUrl(client.getPhotoUrl());
        }
        if (fbClient.getEmail() != null) {
            client.setEmail(fbClient.getEmail());
        }
        if (fbClient.getSex() != null) {
            client.setSex(fbClient.getSex());
        }
        return client;
    }

    public static synchronized void storeHistory(List<MessageProxy> messageList) {
        synchronized (messageList) {
            if (Hawk.contains("messageList")) {
                Hawk.delete("messageList");
            }
            Hawk.put("messageList", messageList);
        }
    }

    public static synchronized List<MessageProxy> getHistory() {
        if (Hawk.contains("messageList")) {
            return Hawk.get("messageList");
        }
        return new ArrayList<>();
    }

    public static boolean logout() {
        currentUser = null;
        return Hawk.deleteAll();
    }
}
