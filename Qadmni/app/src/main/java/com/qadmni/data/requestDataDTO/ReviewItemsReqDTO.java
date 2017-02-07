package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 06-02-2017.
 */
public class ReviewItemsReqDTO extends BaseDTO {

    private long orderId;

    public ReviewItemsReqDTO(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
