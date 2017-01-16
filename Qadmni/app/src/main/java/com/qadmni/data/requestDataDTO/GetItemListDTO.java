package com.qadmni.data.requestDataDTO;

/**
 * Created by shrinivas on 14-01-2017.
 */
public class GetItemListDTO {
    private String categoryId;

    public GetItemListDTO(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
