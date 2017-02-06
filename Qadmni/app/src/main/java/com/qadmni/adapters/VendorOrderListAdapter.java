package com.qadmni.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.qadmni.R;
import com.qadmni.activity.DisplayUserAddressMap;
import com.qadmni.activity.VendorOrderListActivity;
import com.qadmni.data.OrderStatusString;
import com.qadmni.data.responseDataDTO.UpdatableStatusCodesDTO;
import com.qadmni.data.responseDataDTO.VendorItemResDTO;
import com.qadmni.data.responseDataDTO.VendorOrderDTO;
import com.qadmni.utils.CustomVolleyRequestQueue;
import com.qadmni.utils.DateUtils;
import com.qadmni.utils.DeliveryMethods;
import com.qadmni.utils.DeliveryStatusSpinner;
import com.qadmni.utils.PaymentMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by akshay on 23-01-2017.
 */
public class VendorOrderListAdapter extends RecyclerView.Adapter<VendorOrderListAdapter.OrderViewHolder> {
    private static Context mContext;
    private ArrayList<VendorOrderDTO> mData = new ArrayList<VendorOrderDTO>();
    private OrderStatusString orderStatusString;
    private ArrayList<UpdatableStatusCodesDTO> updatableStatusCodesDTOs = new ArrayList<>();
    List<String> spinnerData;
    int sendSelectedId;
    UpdateButton updateButton;

    public VendorOrderListAdapter(Context mContext, ArrayList<VendorOrderDTO> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.orderStatusString = new OrderStatusString(mContext);
        this.updatableStatusCodesDTOs = updatableStatusCodesDTOs;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.row_vendor_order, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OrderViewHolder holder, int position) {
        holder.layHide.setVisibility(View.GONE);
        final VendorOrderDTO vendorOrderDTO = mData.get(position);
        holder.txtOrderId.setText(String.valueOf(vendorOrderDTO.getOrderId()));
        holder.txtOrderDate.setText(DateUtils.convertRegisterTimeToDate(vendorOrderDTO.getOrderDate()));
        String payMode = vendorOrderDTO.getPaymentMode();
        if (payMode.equals(PaymentMethods.PAY_PAL)) {
            holder.paymentMode.setText(mContext.getString(R.string.str_pay_pal));
        } else if (payMode.equals(PaymentMethods.CASH)) {
            holder.paymentMode.setText(mContext.getString(R.string.str_cash_small));
        }
        String deliver = vendorOrderDTO.getDeliveryMethod();
        if (deliver.equals(DeliveryMethods.PICK_UP)) {
            holder.txtDeliveryMode.setText(mContext.getString(R.string.str_pick_up));
        } else if (deliver.equals(DeliveryMethods.HOME_DELIVERY)) {
            holder.txtDeliveryMode.setText(mContext.getString(R.string.str_home_delivery));
        }

        holder.customerAddress.setText(vendorOrderDTO.getDeliveryAddress());
        holder.customerName.setText(vendorOrderDTO.getCustomerName());
        String orderCode = vendorOrderDTO.getCurrentStatusCode();
        if (vendorOrderDTO.isCanUpdateStatus() == true) {
            holder.fab.setVisibility(View.VISIBLE);
            updatableStatusCodesDTOs = UpdatableStatusCodesDTO.deserializeJson(vendorOrderDTO.getUpdatableStatusCodesDTOs());
            spinnerData = new ArrayList<>();
            for (int i = 0; i < updatableStatusCodesDTOs.size(); i++) {

                String deliveryStaus = updatableStatusCodesDTOs.get(i).getStatusCode();
                if (deliveryStaus.equals(DeliveryStatusSpinner.PICK_UP)) {
                    spinnerData.add("Ready to pick up");
                }
                if (deliveryStaus.equals(DeliveryStatusSpinner.PICK_UP_TIME)) {
                    spinnerData.add("Order status waiting");
                }
                if (deliveryStaus.equals(DeliveryStatusSpinner.PICK_UP_COMPLETED)) {
                    spinnerData.add("Order completed");
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (mContext, android.R.layout.simple_spinner_item, spinnerData);

                dataAdapter.setDropDownViewResource
                        (android.R.layout.simple_spinner_dropdown_item);
                holder.spinnerStatus.setAdapter(dataAdapter);
                holder.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedVal = adapterView.getItemAtPosition(i).toString();
                        if (selectedVal.equals("Ready to pick up")) {
                            sendSelectedId = 5;
                        }
                        if (selectedVal.equals("Order status waiting")) {
                            sendSelectedId = 6;
                        }
                        if (selectedVal.equals("Order completed")) {
                            sendSelectedId = 7;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
            holder.updateOrderStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(updateButton!=null)
                   {
                       updateButton.SendOrderStatus(vendorOrderDTO.getOrderId(),sendSelectedId);
                   }
                }
            });
        } else {
            holder.fab.setVisibility(View.INVISIBLE);
        }
        holder.orderStatus.setText(this.orderStatusString.getValueOrderStatus(orderCode));
        holder.customerAmount.setText(String.format("SAR %.2f", vendorOrderDTO.getAmountInSAR()));
        holder.showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DisplayUserAddressMap.class);
                intent.putExtra("userLat", vendorOrderDTO.getDeliveryLat());
                intent.putExtra("userLan", vendorOrderDTO.getDeliveryLong());
                intent.putExtra("userAddress", vendorOrderDTO.getDeliveryAddress());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        if (vendorOrderDTO.isCanUpdateStatus()) {
            holder.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.layHide.setVisibility(View.VISIBLE);
                }
            });
        } else {
            holder.fab.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtOrderId, txtOrderDate, paymentMode, txtDeliveryMode,
                customerName, customerAddress, showOnMap, orderStatus, customerAmount;
        protected Button updateOrderStatus;
        protected Spinner spinnerStatus;
        protected ImageView imgTracker;
        protected LinearLayout layHide;
        protected ImageButton fab;

        public OrderViewHolder(View itemView) {
            super(itemView);
            txtOrderId = (TextView) itemView.findViewById(R.id.txt_order_id);
            txtOrderDate = (TextView) itemView.findViewById(R.id.txt_order_date);
            paymentMode = (TextView) itemView.findViewById(R.id.payment_mode);
            txtDeliveryMode = (TextView) itemView.findViewById(R.id.txt_delivery_mode);
            customerAddress = (TextView) itemView.findViewById(R.id.customerAddress);
            showOnMap = (TextView) itemView.findViewById(R.id.show_on_map);
            customerName = (TextView) itemView.findViewById(R.id.customerName);
            orderStatus = (TextView) itemView.findViewById(R.id.orderStatus);
            customerAmount = (TextView) itemView.findViewById(R.id.customerAmount);
            imgTracker = (ImageView) itemView.findViewById(R.id.imgTracker);
            layHide = (LinearLayout) itemView.findViewById(R.id.lay_hide);
            fab = (ImageButton) itemView.findViewById(R.id.fab);
            updateOrderStatus = (Button) itemView.findViewById(R.id.updateOrderStatus);
            spinnerStatus = (Spinner) itemView.findViewById(R.id.spinner_status);
        }
    }
   public interface UpdateButton
    {
        public void SendOrderStatus(long id,int status);
    }
    public void setOrderStatus(UpdateButton orderStatus)
    {
        this.updateButton=orderStatus;
    }
}
