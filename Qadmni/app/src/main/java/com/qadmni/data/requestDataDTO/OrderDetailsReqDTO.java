package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 08-02-2017.
 */
public class OrderDetailsReqDTO extends BaseDTO {

    private long orderId;

    public OrderDetailsReqDTO(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
