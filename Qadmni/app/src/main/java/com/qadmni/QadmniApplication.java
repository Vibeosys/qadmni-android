package com.qadmni;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.onesignal.OneSignal;

/**
 * Created by akshay on 14-01-2017.
 */
public class QadmniApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this).init();

        // Call syncHashedEmail anywhere in your app if you have the user's email.
        // This improves the effectiveness of OneSignal's "best-time" notification scheduling feature.
        // OneSignal.syncHashedEmail(userEmail);
    }
}
