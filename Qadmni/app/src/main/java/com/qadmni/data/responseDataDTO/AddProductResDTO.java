package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 18-01-2017.
 */
public class AddProductResDTO extends BaseDTO {

    private static final String TAG = AddProductResDTO.class.getSimpleName();
    private long productId;

    public AddProductResDTO() {
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public static AddProductResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        AddProductResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, AddProductResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization AddProductResDTO" + e.toString());
        }
        return responseDTO;
    }
}
