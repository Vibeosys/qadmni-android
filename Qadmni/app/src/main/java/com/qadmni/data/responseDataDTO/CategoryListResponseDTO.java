package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.ArrayList;

/**
 * Created by shrinivas on 13-01-2017.
 */
public class CategoryListResponseDTO {
    public static final String TAG = CategoryListResponseDTO.class.getSimpleName();
    private int categoryId;
    private String category;

    public CategoryListResponseDTO(int categoryId, String category) {
        this.categoryId = categoryId;
        this.category = category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static ArrayList<CategoryListResponseDTO> deSerializedToJson(String serializedString) {
        Gson gson = new Gson();
        ArrayList<CategoryListResponseDTO> categoryListResponseDTOs = null;
        try {

            CategoryListResponseDTO[] deSerializedObject = gson.fromJson(serializedString,CategoryListResponseDTO[].class);
            categoryListResponseDTOs = new ArrayList<>();
            for(CategoryListResponseDTO listResponseDTO:deSerializedObject)
            {
                categoryListResponseDTOs.add(listResponseDTO);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
            Log.d(TAG, "Exception while deSerializing Category List" + e.toString());
        }
        return categoryListResponseDTOs;
    }

}
