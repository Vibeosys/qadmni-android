package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;
import com.qadmni.data.FeedbackItemDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 06-02-2017.
 */
public class ReviewItemsResDTO extends BaseDTO {

    private static final String TAG = ReviewItemsResDTO.class.getSimpleName();
    private long itemId;
    private String itemName;

    public ReviewItemsResDTO() {
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public static ArrayList<ReviewItemsResDTO> deSerializeToArray(String serializedString) {
        Gson gson = new Gson();
        ArrayList<ReviewItemsResDTO> reviewItemsResDTOs = null;
        try {

            ReviewItemsResDTO[] deSerializedObject = gson.fromJson(serializedString, ReviewItemsResDTO[].class);
            reviewItemsResDTOs = new ArrayList<>();
            for (ReviewItemsResDTO reviewItemsResDTO : deSerializedObject) {
                reviewItemsResDTOs.add(reviewItemsResDTO);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
            Log.d(TAG, "Exception while deSerializing ReviewItemsResDTO list" + e.toString());
        }
        return reviewItemsResDTOs;
    }

    public static ArrayList<FeedbackItemDTO> convertToFeedbackItemDTO(String serializedString) {
        Gson gson = new Gson();
        ArrayList<FeedbackItemDTO> reviewItemsResDTOs = null;
        try {

            ReviewItemsResDTO[] deSerializedObject = gson.fromJson(serializedString, ReviewItemsResDTO[].class);
            reviewItemsResDTOs = new ArrayList<>();
            for (ReviewItemsResDTO reviewItemsResDTO : deSerializedObject) {
                FeedbackItemDTO feedbackItemDTO = new FeedbackItemDTO(reviewItemsResDTO.getItemId(), reviewItemsResDTO.itemName, 0);
                reviewItemsResDTOs.add(feedbackItemDTO);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
            Log.d(TAG, "Exception while deSerializing ReviewItemsResDTO list" + e.toString());
        }
        return reviewItemsResDTOs;
    }
}
