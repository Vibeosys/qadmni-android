package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.ArrayList;

/**
 * Created by shrinivas on 04-02-2017.
 */
public class UpdatableStatusCodesDTO {
    private int statusId;
    private String statusCode;

    public UpdatableStatusCodesDTO(int statusId, String statusCode) {
        this.statusId = statusId;
        this.statusCode = statusCode;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public static ArrayList<UpdatableStatusCodesDTO> deserializeJson(UpdatableStatusCodesDTO[] getUpdatableStatusCodesDTOs) {

        ArrayList<UpdatableStatusCodesDTO> updatableStatusCodesDTOs = null;
        UpdatableStatusCodesDTO updatableStatusCodesDTO = null;
        try {
            updatableStatusCodesDTOs = new ArrayList<>();
            for (UpdatableStatusCodesDTO updatableStatusCodesDTO1 : getUpdatableStatusCodesDTOs) {
                updatableStatusCodesDTOs.add(updatableStatusCodesDTO1);
            }
        } catch (JsonParseException e) {
            Log.d("TAG", "Exception in deserialization VendorUpdateStatus" + e.toString());
        } catch (Exception e) {
            Log.d("TAG", "Exception in  VendorUpdateStatus" + e.toString());
        }
        return updatableStatusCodesDTOs;
    }
}
