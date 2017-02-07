package com.qadmni.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qadmni.R;
import com.qadmni.activity.BaseActivity;
import com.qadmni.data.responseDataDTO.LiveOrdersResponseDTO;
import com.qadmni.data.responseDataDTO.PastOrderResponseDTO;
import com.qadmni.utils.DateUtils;
import com.qadmni.utils.DeliveryMethods;
import com.qadmni.utils.PaymentMode;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shrinivas on 23-01-2017.
 */
public class LiveOrdersAdapter extends BaseAdapter {
    ArrayList<LiveOrdersResponseDTO> liveOrdersResponseDTO = null;
    Context context;
    private OnFeedbackClickListener listener;

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
            viewHolder.orderStatus = (ImageView) row.findViewById(R.id.order_status);
            viewHolder.paymentMode = (TextView) row.findViewById(R.id.paymentMode);
            viewHolder.layFeedback = (LinearLayout) row.findViewById(R.id.lay_feedback);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        final LiveOrdersResponseDTO liveOrdersResponseDTOS = liveOrdersResponseDTO.get(i);
        String deliveryMethod = liveOrdersResponseDTOS.getDeliveryMode();
        String paymentMode = liveOrdersResponseDTOS.getPaymentMode();
        if (deliveryMethod.equals(DeliveryMethods.PICK_UP)) {
            viewHolder.deliveryMode.setText(context.getResources().getString(R.string.str_delivery_mode));
        }
        if (deliveryMethod.equals(DeliveryMethods.HOME_DELIVERY)) {
            viewHolder.deliveryMode.setText(context.getResources().getString(R.string.str_home_delivery));
        }
        if (paymentMode.equals(PaymentMode.ONLINE_PAYPAL)) {
            viewHolder.paymentMode.setText(context.getResources().getString(R.string.str_order_demo_delivery_type));
        }
        if (paymentMode.equals(PaymentMode.CASH_MODE)) {
            viewHolder.paymentMode.setText(context.getResources().getString(R.string.str_cash_small));
        }
        if (liveOrdersResponseDTOS.getStageNo() == 1) {
            viewHolder.orderStatus.setImageDrawable(context.getDrawable(R.drawable.form_wiz_1));
        }
        if (liveOrdersResponseDTOS.getStageNo() == 2) {
            viewHolder.orderStatus.setImageDrawable(context.getDrawable(R.drawable.form_wiz_2));
        }
        if (liveOrdersResponseDTOS.getStageNo() == 3) {
            viewHolder.orderStatus.setImageDrawable(context.getDrawable(R.drawable.form_wiz_3));
        }
        viewHolder.orderId.setText("" + liveOrdersResponseDTOS.getOrderId());
        viewHolder.producerName.setText("" + liveOrdersResponseDTOS.getProducerBusinessName());
        viewHolder.amountInSAR.setText("" + liveOrdersResponseDTOS.getAmountInSAR());

        DateUtils dateUtils = new DateUtils();
        Date tempDate = dateUtils.getFormattedDate(liveOrdersResponseDTOS.getOrderDate());
        String stringYear = (String) android.text.format.DateFormat.format("dd-MM-yyyy", tempDate);
        String timeStr = (String) android.text.format.DateFormat.format("hh:mm a", tempDate);
        viewHolder.orderDate.setText("" + stringYear + "\t" + timeStr);
        viewHolder.layFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onFeedBackClick(liveOrdersResponseDTOS);
                }
            }
        });
        return row;
    }

    class ViewHolder {
        TextView orderId, orderDate, producerName, paymentMode, deliveryMode, amountInSAR, stageNo, currentStatusCode, deliveryStatus;
        ImageView orderStatus;
        LinearLayout layFeedback;
    }

    public interface OnFeedbackClickListener {
        void onFeedBackClick(LiveOrdersResponseDTO liveOrdersResponseDTO);
    }

    public void setOnFeedbackClickListener(OnFeedbackClickListener listener) {
        this.listener = listener;
    }
}
