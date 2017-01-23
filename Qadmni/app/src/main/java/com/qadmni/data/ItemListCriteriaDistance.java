package com.qadmni.data;

import java.util.ArrayList;

/**
 * Created by akshay on 23-01-2017.
 */
public class ItemListCriteriaDistance implements ItemFilterCriteria {
    @Override
    public ArrayList<ItemListDetailsDTO> meetCriteria(ArrayList<ItemListDetailsDTO> items, int value) {
        ArrayList<ItemListDetailsDTO> distanceCriteria = new ArrayList<>();
        for (ItemListDetailsDTO item : items) {
            if (item.getUnitPrice() < value) {
                distanceCriteria.add(item);
            }
        }
        return distanceCriteria;
    }
}
