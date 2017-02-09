package com.qadmni.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.qadmni.R;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.OrderDetailsReqDTO;
import com.qadmni.data.responseDataDTO.OrderDetailsResDTO;
import com.qadmni.data.responseDataDTO.OrderItems;
import com.qadmni.utils.DateUtils;
import com.qadmni.utils.NetworkUtils;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

import java.util.ArrayList;

public class OrderDetailsActivity extends BaseActivity implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived {

    private static final Object TAG = OrderDetailsActivity.class.getSimpleName();
    private TextView txtOrderDate, txtOrderNo, txtTotalAmount, txtTaxDetails;
    private LinearLayout orderDetails;
    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        setTitle(getString(R.string.str_track_order_details));
        txtOrderDate = (TextView) findViewById(R.id.txt_order_date);
        txtOrderNo = (TextView) findViewById(R.id.txt_order_no);
        txtTotalAmount = (TextView) findViewById(R.id.txt_total_amount);
        txtTaxDetails = (TextView) findViewById(R.id.txt_total_tax);

        orderDetails = (LinearLayout) findViewById(R.id.orderDetails);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderId = bundle.getLong("orderId");
            getOrderDetails();
        }
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);
    }

    public void getOrderDetails() {
        if (!NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
            createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                    getResources().getString(R.string.str_err_net_msg));
        } else {
            progressDialog.show();
            OrderDetailsReqDTO orderDetailsReqDTO = new OrderDetailsReqDTO(orderId);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(orderDetailsReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_GET_ORDER_DETAILS,
                    mSessionManager.getOrderDetails(), baseRequestDTO);
        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_GET_ORDER_DETAILS:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;

        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_GET_ORDER_DETAILS:
                customAlterDialog(getString(R.string.str_server_err_title), errorMessage);
                break;

        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_GET_ORDER_DETAILS:
                OrderDetailsResDTO orderDetailsResDTO = OrderDetailsResDTO.deserializeJson(data);
                try {
                    txtOrderDate.setText(DateUtils.convertRegisterTimeToDate(orderDetailsResDTO.getOrderDate()));
                    txtOrderNo.setText(String.valueOf(orderDetailsResDTO.getOrderId()));
                    txtTotalAmount.setText(String.format("%.2f", orderDetailsResDTO.getTotalAmountInSAR()));
                    txtTaxDetails.setText(String.format("%.2f", orderDetailsResDTO.getTotalTaxesAndSurcharges()));
                    ArrayList<OrderItems> orderItems = orderDetailsResDTO.getItems();
                    for (OrderItems order : orderItems) {
                        LayoutInflater theLayoutInflator = (LayoutInflater) getApplicationContext().getSystemService
                                (Context.LAYOUT_INFLATER_SERVICE);
                        View row = theLayoutInflator.inflate(R.layout.row_order_details, null);
                        TextView txtProductName = (TextView) row.findViewById(R.id.txtProductName);
                        TextView txtQty = (TextView) row.findViewById(R.id.txtQty);
                        TextView price = (TextView) row.findViewById(R.id.price);
                        txtProductName.setText(order.getName());
                        txtQty.setText(String.valueOf(order.getQty()));
                        price.setText(String.format("%.2f", order.getPrice()));
                        orderDetails.addView(row);
                    }
                } catch (NullPointerException e) {
                    FirebaseCrash.log(TAG + " Null pointer when access the order details" + e.getMessage());
                }

                break;
        }
    }
}
