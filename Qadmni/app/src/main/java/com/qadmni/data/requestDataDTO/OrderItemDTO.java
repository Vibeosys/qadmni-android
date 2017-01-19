package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 19-01-2017.
 */
public class OrderItemDTO extends BaseDTO {

    private long itemId;
    private int itemQty;

    public OrderItemDTO(long itemId, int itemQty) {
        this.itemId = itemId;
        this.itemQty = itemQty;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }
}
