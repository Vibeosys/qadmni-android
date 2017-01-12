package com.qadmni.data;

import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by shrinivas on 12-01-2017.
 */
public class BaseDTO {
    public String serializeString() {
        Gson gson = new Gson();

        String serializedString = gson.toJson(this);
        Log.i("object Serialized ", serializedString);
        return serializedString;
    }

}
