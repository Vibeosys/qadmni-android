package com.qadmni.data;

import com.qadmni.data.responseDataDTO.ItemInfoList;

import java.util.ArrayList;

/**
 * Created by akshay on 07-02-2017.
 */
public class ItemInfoCategoryWise {

    public static ArrayList<ItemListDetailsDTO> getItems(long categoryId, ArrayList<ItemListDetailsDTO> itemInfoLists) {
        ArrayList<ItemListDetailsDTO> returnItemInfoLists = new ArrayList<>();
        for (ItemListDetailsDTO item : itemInfoLists) {
            if (item.getCategoryId() == categoryId) {
                returnItemInfoLists.add(item);
            }
        }
        return returnItemInfoLists;
    }
}
