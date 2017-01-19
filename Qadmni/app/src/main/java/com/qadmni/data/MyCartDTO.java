package com.qadmni.data;

/**
 * Created by shrinivas on 19-01-2017.
 */
public class MyCartDTO {
    private long producerId;
    private int itemQuantity;
    private String unitPrice;
    private String ItemName;

    public MyCartDTO(long producerId, int itemQuantity, String unitPrice, String itemName) {
        this.producerId = producerId;
        this.itemQuantity = itemQuantity;
        this.unitPrice = unitPrice;
        ItemName = itemName;
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
    }


    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }
}
