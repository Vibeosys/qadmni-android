package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 04-02-2017.
 */
public class UpdateProductReqDTO extends BaseDTO {

    private long productId;
    private boolean isActive;
    private String itemNameEn;
    private String itemNameAr;
    private String itemDescEn;
    private String itemDescAr;
    private long categoryId;
    private double price;
    private String offerText;

    public UpdateProductReqDTO(long productId, boolean isActive, String itemNameEn,
                               String itemNameAr, String itemDescEn, String itemDescAr,
                               long categoryId, double price, String offerText) {
        this.productId = productId;
        this.isActive = isActive;
        this.itemNameEn = itemNameEn;
        this.itemNameAr = itemNameAr;
        this.itemDescEn = itemDescEn;
        this.itemDescAr = itemDescAr;
        this.categoryId = categoryId;
        this.price = price;
        this.offerText = offerText;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getItemNameEn() {
        return itemNameEn;
    }

    public void setItemNameEn(String itemNameEn) {
        this.itemNameEn = itemNameEn;
    }

    public String getItemNameAr() {
        return itemNameAr;
    }

    public void setItemNameAr(String itemNameAr) {
        this.itemNameAr = itemNameAr;
    }

    public String getItemDescEn() {
        return itemDescEn;
    }

    public void setItemDescEn(String itemDescEn) {
        this.itemDescEn = itemDescEn;
    }

    public String getItemDescAr() {
        return itemDescAr;
    }

    public void setItemDescAr(String itemDescAr) {
        this.itemDescAr = itemDescAr;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOfferText() {
        return offerText;
    }

    public void setOfferText(String offerText) {
        this.offerText = offerText;
    }
}
