package net.sytes.surfael.androidchat.util;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Raphael on 20/06/2016.
 */
public class ChatUtils {
    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
