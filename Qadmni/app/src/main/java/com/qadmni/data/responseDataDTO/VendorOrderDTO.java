package com.qadmni.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.qadmni.data.BaseDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 23-01-2017.
 */
public class VendorOrderDTO extends BaseDTO {
    private static final String TAG = VendorOrderDTO.class.getSimpleName();
    private long orderId;
    private String orderDate;
    private String scheduleDate;
    private String paymentMode;
    private String paymentMethod;
    private String deliveryMethod;
    private String deliveryType;
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private double deliveryLat;
    private double deliveryLong;
    private double amountInSAR;
    private int stageNo;
    private String currentStatusCode;
    private int deliveryStatusId;
    // private ArrayList<String> UpdatableStatusCodesDTO;
    private boolean canUpdateStatus;
    private UpdatableStatusCodesDTO[] updatableStatusCodes;
    private boolean isGiftWrap;
    private String giftMessage;


    public VendorOrderDTO() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public double getDeliveryLat() {
        return deliveryLat;
    }

    public void setDeliveryLat(double deliveryLat) {
        this.deliveryLat = deliveryLat;
    }

    public double getDeliveryLong() {
        return deliveryLong;
    }

    public void setDeliveryLong(double deliveryLong) {
        this.deliveryLong = deliveryLong;
    }

    public double getAmountInSAR() {
        return amountInSAR;
    }

    public void setAmountInSAR(double amountInSAR) {
        this.amountInSAR = amountInSAR;
    }

    public int getStageNo() {
        return stageNo;
    }

    public void setStageNo(int stageNo) {
        this.stageNo = stageNo;
    }

    public String getCurrentStatusCode() {
        return currentStatusCode;
    }

    public void setCurrentStatusCode(String currentStatusCode) {
        this.currentStatusCode = currentStatusCode;
    }

    public int getDeliveryStatusId() {
        return deliveryStatusId;
    }

    public void setDeliveryStatusId(int deliveryStatusId) {
        this.deliveryStatusId = deliveryStatusId;
    }

   /* public ArrayList<String> getUpdatableStatusCodes() {
        return UpdatableStatusCodesDTO;
    }

    public void setUpdatableStatusCodes(ArrayList<String> UpdatableStatusCodesDTO) {
        this.UpdatableStatusCodesDTO = UpdatableStatusCodesDTO;
    }*/

    public boolean isGitWrap() {
        return isGiftWrap;
    }

    public void setGitWrap(boolean gitWrap) {
        isGiftWrap = gitWrap;
    }

    public String getGiftMessage() {
        return giftMessage;
    }

    public void setGiftMessage(String giftMessage) {
        this.giftMessage = giftMessage;
    }


    public boolean isCanUpdateStatus() {
        return canUpdateStatus;
    }

    public void setCanUpdateStatus(boolean canUpdateStatus) {
        this.canUpdateStatus = canUpdateStatus;
    }

    public static VendorOrderDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        VendorOrderDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, VendorOrderDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization VendorOrderDTO" + e.toString());
        }
        return responseDTO;
    }

    public UpdatableStatusCodesDTO[] getUpdatableStatusCodesDTOs() {
        return updatableStatusCodes;
    }

    public void setUpdatableStatusCodesDTOs(UpdatableStatusCodesDTO[] updatableStatusCodesDTOs) {
        this.updatableStatusCodes = updatableStatusCodesDTOs;
    }

    public static ArrayList<VendorOrderDTO> deSerializeToArray(String serializedString) {
        Gson gson = new Gson();
        ArrayList<VendorOrderDTO> vendorItemResDTOs = null;
        try {

            VendorOrderDTO[] deSerializedObject = gson.fromJson(serializedString, VendorOrderDTO[].class);
            vendorItemResDTOs = new ArrayList<>();
            for (VendorOrderDTO vendorOrderDTO : deSerializedObject) {
                vendorItemResDTOs.add(vendorOrderDTO);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
            Log.d(TAG, "Exception while deSerializing VendorOrderDTO list" + e.toString());
        }
        return vendorItemResDTOs;
    }
}
