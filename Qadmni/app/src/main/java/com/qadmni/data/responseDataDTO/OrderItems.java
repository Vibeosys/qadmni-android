package com.qadmni.data.responseDataDTO;

/**
 * Created by shrinivas on 08-02-2017.
 */
public class OrderItems {
    private String name;
    private long qty;
    private long price;

    public OrderItems(String name, long qty, long price) {
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
