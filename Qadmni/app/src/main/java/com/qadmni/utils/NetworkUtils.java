package com.qadmni.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by akshay on 17-01-2017.
 */
public class NetworkUtils {


    private static final String TAG = NetworkUtils.class.getSimpleName();

    /* Method to check network availability
        * */
    public static boolean isActiveNetworkAvailable(Context aContext) {

        boolean theStatus = false;
        try {
            ConnectivityManager theConManager = (ConnectivityManager) aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo theNetInfo = theConManager.getActiveNetworkInfo();
            if (theNetInfo != null) {
                theStatus = theNetInfo.isConnected();
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "## Network utils error" + e.getMessage());
            FirebaseCrash.log("## Network utils error catch block" + e.getMessage());
            theStatus = false;
        }

        return theStatus;

    }
}
