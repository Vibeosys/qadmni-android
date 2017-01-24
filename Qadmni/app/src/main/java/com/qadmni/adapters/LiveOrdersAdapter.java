package com.qadmni.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qadmni.R;
import com.qadmni.activity.BaseActivity;
import com.qadmni.data.responseDataDTO.LiveOrdersResponseDTO;
import com.qadmni.utils.DateUtils;
import com.qadmni.utils.DeliveryMethods;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shrinivas on 23-01-2017.
 */
public class LiveOrdersAdapter extends BaseAdapter {
    ArrayList<LiveOrdersResponseDTO> liveOrdersResponseDTO = null;
    Context context;

    public LiveOrdersAdapter(ArrayList<LiveOrdersResponseDTO> liveOrdersResponseDTO, Context context) {
        this.liveOrdersResponseDTO = liveOrdersResponseDTO;
        this.context = context;
    }

    @Override
    public int getCount() {
        return liveOrdersResponseDTO.size();
    }

    @Override
    public Object getItem(int i) {
        return liveOrdersResponseDTO.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder viewHolder = null;
        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.live_order_child_element, null);
            viewHolder = new ViewHolder();
            viewHolder.orderId = (TextView) row.findViewById(R.id.orderId);
            viewHolder.producerName = (TextView) row.findViewById(R.id.producerName);
            viewHolder.amountInSAR = (TextView) row.findViewById(R.id.sarAmount);
            viewHolder.deliveryMode = (TextView) row.findViewById(R.id.deliveryType);
            viewHolder.orderDate = (TextView) row.findViewById(R.id.orderDate);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        LiveOrdersResponseDTO liveOrdersResponseDTOS = liveOrdersResponseDTO.get(i);
        String deliveryMethod = liveOrdersResponseDTOS.getDeliveryMode();
        if (deliveryMethod.equals(DeliveryMethods.PICK_UP)) {
            viewHolder.deliveryMode.setText(context.getResources().getString(R.string.str_delivery_mode));
        }if(deliveryMethod.equals(DeliveryMethods.HOME_DELIVERY))
        {
            viewHolder.deliveryMode.setText(context.getResources().getString(R.string.str_home_delivery));
        }
        viewHolder.orderId.setText("" + liveOrdersResponseDTOS.getOrderId());
        viewHolder.producerName.setText("" + liveOrdersResponseDTOS.getProducerBusinessName());
        viewHolder.amountInSAR.setText("" + liveOrdersResponseDTOS.getAmountInSAR());

        DateUtils dateUtils = new DateUtils();
        Date tempDate = dateUtils.getFormattedDate(liveOrdersResponseDTOS.getOrderDate());
        String stringYear = (String) android.text.format.DateFormat.format("dd-MM-yyyy", tempDate);
        String timeStr = (String) android.text.format.DateFormat.format("hh:mm a", tempDate);
        viewHolder.orderDate.setText("" + stringYear + "\t" + timeStr);

        return row;
    }

    class ViewHolder {
        TextView orderId, orderDate, producerName, paymentMode, deliveryMode, amountInSAR, stageNo, currentStatusCode, deliveryStatus;
    }
}
