package com.pakodigames.pakodi.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Util {

    public static String getDeviceId(ContentResolver resolver){
        String androidId = Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
