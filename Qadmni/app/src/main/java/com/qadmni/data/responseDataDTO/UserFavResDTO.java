package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 04-02-2017.
 */
public class UserFavResDTO extends BaseDTO {

    private static final String TAG = UserFavResDTO.class.getSimpleName();
    private ArrayList<ItemInfoList> itemInfoList;
    private ArrayList<ProducerLocations> producerLocations;

    public UserFavResDTO() {
    }

    public ArrayList<ItemInfoList> getItemInfoList() {
        return itemInfoList;
    }

    public void setItemInfoList(ArrayList<ItemInfoList> itemInfoList) {
        this.itemInfoList = itemInfoList;
    }

    public ArrayList<ProducerLocations> getProducerLocations() {
        return producerLocations;
    }

    public void setProducerLocations(ArrayList<ProducerLocations> producerLocations) {
        this.producerLocations = producerLocations;
    }

    public static UserFavResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        UserFavResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, UserFavResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization UserFavResDTO" + e.toString());
        }
        return responseDTO;
    }
}
