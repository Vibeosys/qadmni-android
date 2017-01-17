package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.rest.Post;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shrinivas on 16-01-2017.
 */
public class ItemListResponseDTO {


    private ProducerLocations[] producerLocations;
    private ItemInfoList[] itemInfoList;

    public ItemListResponseDTO() {
    }

    public ProducerLocations[] getProducerLocations() {
        return producerLocations;
    }

    public void setProducerLocations(ProducerLocations[] producerLocations) {
        this.producerLocations = producerLocations;
    }

    public ItemInfoList[] getItemInfoLists() {
        return itemInfoList;
    }

    public void setItemInfoLists(ItemInfoList[] itemInfoList) {
        this.itemInfoList = itemInfoList;
    }

    public static ItemListResponseDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        ItemListResponseDTO itemListResponseDTO = null;
        try {
            itemListResponseDTO = gson.fromJson(serializedString, ItemListResponseDTO.class);
        } catch (JsonParseException e) {
            Log.d("TAG", "Exception in deserialization VendorLoginResDTO" + e.toString());
        }
        return itemListResponseDTO;
    }
}
