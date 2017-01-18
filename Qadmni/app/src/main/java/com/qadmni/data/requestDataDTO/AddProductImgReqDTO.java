package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 18-01-2017.
 */
public class AddProductImgReqDTO extends BaseDTO {

    private long producerId;
    private String password;
    private long productId;

    public AddProductImgReqDTO(long producerId, String password, long productId) {
        this.producerId = producerId;
        this.password = password;
        this.productId = productId;
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
