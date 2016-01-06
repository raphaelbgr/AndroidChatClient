package net.sytes.surfael.data;

import android.content.Context;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;

import net.sytes.surfael.api.model.clients.Client;

/**
 * Created by Raphael on 10/12/2015.
 */
public class Session {
    public static Client currentUser;

    public static void startHawk(Context context) {
        Hawk.init(context)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                .setStorage(HawkBuilder.newSharedPrefStorage(context))
                .build();
    }

    public static void updateClient() {
        Hawk.remove("currentUser");
        Hawk.put("currentUser", Session.currentUser);
    }

    public static Client recoverSession() {
        if (Hawk.contains("currentUser")) {
            return (Client) Hawk.get("currentUser");
        } else return null;
    }
}
