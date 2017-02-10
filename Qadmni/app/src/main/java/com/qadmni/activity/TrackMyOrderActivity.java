package com.qadmni.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qadmni.R;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.OrderDetailsReqDTO;
import com.qadmni.data.responseDataDTO.OrderTrackResDTO;
import com.qadmni.utils.DeliveryStatusSpinner;
import com.qadmni.utils.NetworkUtils;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

public class TrackMyOrderActivity extends BaseActivity implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived {

    private TextView txtOrderId, txtAddress, txtOrderStatus, txtTempAddress;
    private ImageView imgStatus;
    private LinearLayout layOrderDetails;
    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_my_order);
        txtOrderId = (TextView) findViewById(R.id.txt_order_id);
        txtAddress = (TextView) findViewById(R.id.txt_address);
        txtOrderStatus = (TextView) findViewById(R.id.txt_order_status);
       // txtTempAddress = (TextView) findViewById(R.id.txt_temp_address);
        imgStatus = (ImageView) findViewById(R.id.img_status);
        layOrderDetails = (LinearLayout) findViewById(R.id.layOrderDetails);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderId = bundle.getLong("orderId");
            getOrderDetails();
        }
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);

        layOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("orderId", orderId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
            mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_GET_ORDER_TRACK_DETAILS,
                    mSessionManager.getOrderTrackDetails(), baseRequestDTO);
        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_GET_ORDER_TRACK_DETAILS:
                break;
        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_GET_ORDER_TRACK_DETAILS:
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_GET_ORDER_TRACK_DETAILS:
                OrderTrackResDTO orderTrackResDTO = OrderTrackResDTO.deserializeJson(data);
                txtOrderId.setText(String.valueOf(orderTrackResDTO.getOrderId()));
                txtAddress.setText(orderTrackResDTO.getDeliveryAddress());
                //txtTempAddress.setText(orderTrackResDTO.getTempAddress());
                switch (orderTrackResDTO.getStageNo()) {
                    case 1:
                        imgStatus.setImageDrawable(getDrawable(R.drawable.form_wiz_1));
                        break;
                    case 2:
                        imgStatus.setImageDrawable(getDrawable(R.drawable.form_wiz_2));
                        break;
                    case 3:
                        imgStatus.setImageDrawable(getDrawable(R.drawable.form_wiz_3));
                        break;
                }
                String deliveryStaus = orderTrackResDTO.getCurrentStatusCode();
                if (deliveryStaus.equals(DeliveryStatusSpinner.PICK_UP)) {
                    txtOrderStatus.setText(getString(R.string.str_order_ready_to_pickup));
                }
                if (deliveryStaus.equals(DeliveryStatusSpinner.PICK_UP_TIME)) {
                    txtOrderStatus.setText(getString(R.string.str_order_status_waiting));
                }
                if (deliveryStaus.equals(DeliveryStatusSpinner.PICK_UP_COMPLETED)) {
                    txtOrderStatus.setText(getString(R.string.str_order_pickup_complete));
                }
                if (deliveryStaus.equals(DeliveryStatusSpinner.ORDER_PLACED_CODE)) {
                    txtOrderStatus.setText(getString(R.string.str_order_placed_code));
                }
                if (deliveryStaus.equals(DeliveryStatusSpinner.DELIVERY_IN_PROGRESS)) {
                    txtOrderStatus.setText(getString(R.string.str_order_devliver_progeress));
                }
                if (deliveryStaus.equals(DeliveryStatusSpinner.DELIVERED)) {
                    txtOrderStatus.setText(getString(R.string.str_order_devlivered));
                }
                break;
        }
    }
}
