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
    private ProducerLocationDetailsDTO producerDetails;
    private int quantity;
    private boolean isMyFav;
    private long categoryId;

    public ItemListDetailsDTO(long itemId, String itemDesc, String itemName, double unitPrice,
                              String offerText, long reviews, double rating,
                              String imageUrl, ProducerLocationDetailsDTO producerDetails,
                              int quantity, boolean isMyFav, long categoryId) {
        this.itemId = itemId;
        this.itemDesc = itemDesc;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.offerText = offerText;
        this.reviews = reviews;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.producerDetails = producerDetails;
        this.quantity = quantity;
        this.isMyFav = isMyFav;
        this.categoryId = categoryId;
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

    public long getReviews() {
        return reviews;
    }

    public void setReviews(long reviews) {
        this.reviews = reviews;
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

    public ProducerLocationDetailsDTO getProducerDetails() {
        return producerDetails;
    }

    public void setProducerDetails(ProducerLocationDetailsDTO producerDetails) {
        this.producerDetails = producerDetails;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isMyFav() {
        return isMyFav;
    }

    public void setMyFav(boolean myFav) {
        isMyFav = myFav;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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
            double doubleDistance1 = p1.getProducerDetails().getDoubleDistance();
            double doubleDistance2 = p2.getProducerDetails().getDoubleDistance();

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
