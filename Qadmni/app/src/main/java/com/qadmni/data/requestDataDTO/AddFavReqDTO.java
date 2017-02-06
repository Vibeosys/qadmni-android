package com.qadmni.data.requestDataDTO;

import com.qadmni.data.BaseDTO;

/**
 * Created by akshay on 06-02-2017.
 */
public class AddFavReqDTO extends BaseDTO {

    private long productId;
    private int favFlag;

    public AddFavReqDTO(long productId, int favFlag) {
        this.productId = productId;
        this.favFlag = favFlag;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getFavFlag() {
        return favFlag;
    }

    public void setFavFlag(int favFlag) {
        this.favFlag = favFlag;
    }
}
