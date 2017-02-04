package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 04-02-2017.
 */
public class ItemDetailsReqDTO extends BaseDTO {

    private long productId;

    public ItemDetailsReqDTO(long productId) {
        this.productId = productId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
