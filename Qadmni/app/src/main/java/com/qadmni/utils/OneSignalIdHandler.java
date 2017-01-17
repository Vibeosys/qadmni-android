package com.qadmni.utils;

import com.onesignal.OneSignal;

/**
 * Created by akshay on 17-01-2017.
 */
public class OneSignalIdHandler {
    private static String userId;
    private static String googleId;

    public OneSignalIdHandler() {
        setOneSignalIds(this);
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public void setOneSignalIds(final OneSignalIdHandler one) {

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                one.userId = userId;
                one.googleId = registrationId;
            }
        });
    }
}
