package com.qadmni.data;

import android.content.Context;

import com.qadmni.R;

import java.util.HashMap;

/**
 * Created by akshay on 24-01-2017.
 */
public class OrderStatusString {

    Context mContext;
    HashMap<String, String> orderStatus = new HashMap<>();

    public OrderStatusString(Context mContext) {
        this.mContext = mContext;
        orderStatus.put("ORDER_PLACED", mContext.getResources().getString(R.string.str_order_placed));
        orderStatus.put("DELIVERY_IN_PROGRESS", mContext.getResources().getString(R.string.str_delivery_in_progress));
        orderStatus.put("DELIVERED", mContext.getResources().getString(R.string.str_order_devlivered));
        orderStatus.put("ORDER_PLACED_CODE", mContext.getResources().getString(R.string.str_order_placed_code));
        orderStatus.put("READY_TO_PICKUP", mContext.getResources().getString(R.string.str_order_ready_to_pickup));
        orderStatus.put("PICKUP_COMPLETE", mContext.getResources().getString(R.string.str_order_pickup_complete));
        orderStatus.put("TIME_FOR_PICKUP_OVER", mContext.getResources().getString(R.string.str_order_time_for_pick_over));
    }

    public String getValueOrderStatus(String key) {
        return this.orderStatus.get(key);
    }
}
