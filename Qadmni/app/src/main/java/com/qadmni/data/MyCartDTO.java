package com.qadmni.data;

/**
 * Created by shrinivas on 19-01-2017.
 */
public class MyCartDTO {
    private long producerId;
    private int itemQuantity;
    private double unitPrice;
    private String ItemName;
    private long productId;

    public MyCartDTO(long producerId, int itemQuantity, double unitPrice,
                     String itemName, long productId) {
        this.producerId = producerId;
        this.itemQuantity = itemQuantity;
        this.unitPrice = unitPrice;
        ItemName = itemName;
        this.productId = productId;
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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
