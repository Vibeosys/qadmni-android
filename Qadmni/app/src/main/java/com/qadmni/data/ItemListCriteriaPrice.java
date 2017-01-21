package com.qadmni.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 21-01-2017.
 */
public class ItemListCriteriaPrice implements ItemFilterCriteria {

    @Override
    public ArrayList<ItemListDetailsDTO> meetCriteria(ArrayList<ItemListDetailsDTO> items, int value) {
        ArrayList<ItemListDetailsDTO> meetCriteria = new ArrayList<>();
        for (ItemListDetailsDTO item : items) {
            if (item.getUnitPrice() < value) {
                meetCriteria.add(item);
            }
        }
        return meetCriteria;
    }
}
