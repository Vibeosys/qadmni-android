package com.qadmni.data;

import java.util.Comparator;

/**
 * Created by shrinivas on 17-01-2017.
 */
public class ItemListDetailsDTO {
    private long itemId;
    private String itemDesc;
    private String itemName;
    private double unitPrice;
    private String offerText;
    private long reviews;

    private double rating;
    private String imageUrl;
    private long producerId;
    private String businessName;
    private double businessLat;
    private double businessLong;
    private double userLat;
    private double userLon;
    private String userDistance;
    private String userTime;
    private int quantity;
    private double doubleDistance;

    public ItemListDetailsDTO(long itemId, String itemDesc, String itemName, double unitPrice,
                              String offerText, double rating, String imageUrl, long producerId,
                              String businessName, double businessLat, double businessLong,
                              double userLat, double userLon, String userDistance, String userTime, long reviews) {
        this.itemId = itemId;
        this.itemDesc = itemDesc;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.offerText = offerText;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.producerId = producerId;
        this.businessName = businessName;
        this.businessLat = businessLat;
        this.businessLong = businessLong;
        this.userLat = userLat;
        this.userLon = userLon;
        this.userDistance = userDistance;
        this.userTime = userTime;
        this.reviews = reviews;
        this.quantity = 0;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getOfferText() {
        return offerText;
    }

    public void setOfferText(String offerText) {
        this.offerText = offerText;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public double getBusinessLat() {
        return businessLat;
    }

    public void setBusinessLat(double businessLat) {
        this.businessLat = businessLat;
    }

    public double getBusinessLong() {
        return businessLong;
    }

    public void setBusinessLong(double businessLong) {
        this.businessLong = businessLong;
    }

    public double getUserLat() {
        return userLat;
    }

    public void setUserLat(double userLat) {
        this.userLat = userLat;
    }

    public double getUserLon() {
        return userLon;
    }

    public void setUserLon(double userLon) {
        this.userLon = userLon;
    }

    public String getUserDistance() {
        return userDistance;
    }

    public void setUserDistance(String userDistance) {
        this.userDistance = userDistance;
    }

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

    public long getReviews() {
        return reviews;
    }

    public void setReviews(long reviews) {
        this.reviews = reviews;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getDoubleDistance() {
        return doubleDistance;
    }

    public void setDoubleDistance(double doubleDistance) {
        this.doubleDistance = doubleDistance;
    }

    public static class PriceComparator implements Comparator<ItemListDetailsDTO> {
        @Override
        public int compare(ItemListDetailsDTO p1, ItemListDetailsDTO p2) {
            double minPrice1 = p1.getUnitPrice();
            double minPrice2 = p2.getUnitPrice();

            if (minPrice1 == minPrice2)
                return 0;
            else if (minPrice1 > minPrice2)
                return 1;
            else
                return -1;
        }
    }

    public static class DistanceComparator implements Comparator<ItemListDetailsDTO> {
        @Override
        public int compare(ItemListDetailsDTO p1, ItemListDetailsDTO p2) {
            double doubleDistance1 = p1.getDoubleDistance();
            double doubleDistance2 = p2.getDoubleDistance();

            if (doubleDistance1 == doubleDistance2)
                return 0;
            else if (doubleDistance1 > doubleDistance2)
                return 1;
            else
                return -1;
        }
    }

    public static class RatingComparator implements Comparator<ItemListDetailsDTO> {
        @Override
        public int compare(ItemListDetailsDTO p1, ItemListDetailsDTO p2) {
            double rating1 = p1.getRating();
            double rating2 = p2.getRating();

            if (rating1 == rating2)
                return 0;
            else if (rating1 > rating2)
                return 1;
            else
                return -1;
        }
    }
}
