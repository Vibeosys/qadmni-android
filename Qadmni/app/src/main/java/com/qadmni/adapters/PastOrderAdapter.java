package com.qadmni.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qadmni.R;
import com.qadmni.data.OrderStatusString;
import com.qadmni.data.responseDataDTO.PastOrderResponseDTO;
import com.qadmni.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shrinivas on 23-01-2017.
 */
public class PastOrderAdapter extends BaseAdapter {
    ArrayList<PastOrderResponseDTO> pastOrderResponseDTOs;
    Context context;
    private OrderStatusString orderStatusString;

    public PastOrderAdapter(ArrayList<PastOrderResponseDTO> pastOrderResponseDTOs, Context context) {
        this.pastOrderResponseDTOs = pastOrderResponseDTOs;
        this.context = context;
        this.orderStatusString = new OrderStatusString(context);
    }

    @Override
    public int getCount() {
        return pastOrderResponseDTOs.size();
    }

    @Override
    public Object getItem(int i) {
        return pastOrderResponseDTOs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return pastOrderResponseDTOs.indexOf(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder viewHolder = null;
        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.past_order_child_element, null);
            viewHolder = new ViewHolder();
            viewHolder.pastOrderId = (TextView) row.findViewById(R.id.pastOrderId);
            viewHolder.pastOrderDate = (TextView) row.findViewById(R.id.orderDate_past_order);
            viewHolder.sarAmount = (TextView) row.findViewById(R.id.sar_amount);
            viewHolder.txtStatus = (TextView) row.findViewById(R.id.txtStatus);
            row.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        PastOrderResponseDTO pastOrderResponseDTO = pastOrderResponseDTOs.get(i);
        viewHolder.pastOrderId.setText("" + pastOrderResponseDTO.getOrderId());
        viewHolder.sarAmount.setText("" + pastOrderResponseDTO.getAmountInSAR());
        DateUtils dateUtils = new DateUtils();
        Date tempDate = dateUtils.getFormattedDate(pastOrderResponseDTO.getOrderDate());
        String stringYear = (String) android.text.format.DateFormat.format("dd-MM-yyyy", tempDate);
        String timeStr = (String) android.text.format.DateFormat.format("hh:mm a", tempDate);
        viewHolder.pastOrderDate.setText("" + stringYear + "\t" + timeStr);
        viewHolder.txtStatus.setText(this.orderStatusString.getValueOrderStatus(pastOrderResponseDTO.getDeliveryStatus()));
        return row;
    }

    class ViewHolder {
        TextView pastOrderId, pastOrderDate, sarAmount, txtStatus;
    }
}
