package com.qadmni.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 21-01-2017.
 */
public interface ItemFilterCriteria {
    public ArrayList<ItemListDetailsDTO> meetCriteria(ArrayList<ItemListDetailsDTO> persons, int value);
}
