package com.qadmni.data.responseDataDTO;

/**
 * Created by shrinivas on 08-02-2017.
 */
public class OrderItems {
    private String name;
    private int qty;
    private double price;

    public OrderItems() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
