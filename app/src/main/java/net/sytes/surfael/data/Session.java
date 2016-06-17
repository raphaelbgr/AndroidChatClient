package net.sytes.surfael.data;

import android.content.Context;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import net.sytes.surfael.androidchat.adapters.MessagesRecycleAdapter;
import net.sytes.surfael.api.model.clients.Client;
import net.sytes.surfael.api.model.messages.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Raphael on 10/12/2015.
 */
public class Session {
//    public static final String SERVER_IP = "54.232.241.237";
    public static final String SERVER_IP = "10.5.5.132";
//    public static final String SERVER_IP = "192.168.2.11";
    public static final int SERVER_PORT = 2001;
    private static Client currentUser;

    public static void startHawk(Context context) {
        Hawk.init(context)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
//                .setStorage(HawkBuilder.newSharedPrefStorage(context))
                .setStorage(HawkBuilder.newSqliteStorage(context))
                .setLogLevel(LogLevel.FULL)
                .build();
    }

    public static void setCurrentUser(Client client) {
        ClientProxy clientProxy = new ClientProxy(client);
        currentUser = client;

        Hawk.remove("currentUser");
        Hawk.put("currentUser", clientProxy);
    }

    public static Client getCurrentUser() {
        if (currentUser == null) {
            if (Hawk.contains("currentUser")) {
                ClientProxy clientProxy = Hawk.get("currentUser");
                Client client = null;
                if (clientProxy != null) {
                    client = clientProxy.buildClient(clientProxy);
                }
                return client;
            } else return null;
        } else return currentUser;
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
                Hawk.remove("messageList");
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
        return Hawk.remove("messageList") && Hawk.remove("currentUser") && Hawk.clear();
    }
}
