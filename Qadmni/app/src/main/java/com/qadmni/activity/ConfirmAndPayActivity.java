package com.qadmni.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.qadmni.R;
import com.qadmni.adapters.ConfirmChargeAdapter;
import com.qadmni.adapters.UserConfirmListAdapter;
import com.qadmni.data.responseDataDTO.InitOrderResDTO;
import com.qadmni.data.responseDataDTO.OrderChargesResDTO;
import com.qadmni.data.responseDataDTO.OrderItemResDTO;
import com.qadmni.utils.ServerSyncManager;

import java.util.ArrayList;

public class ConfirmAndPayActivity extends BaseActivity implements
        ServerSyncManager.OnSuccessResultReceived, ServerSyncManager.OnErrorResultReceived,
        View.OnClickListener {

    public static final java.lang.String ORDER_INIT_DETAILS = "orderInitDetails";
    private RecyclerView itemList, chargeList;
    private LinearLayout payLay;
    private TextView txtAmountSar, txtAmountUsd;
    private ConfirmChargeAdapter confirmListAdapter;
    private UserConfirmListAdapter userConfirmListAdapter;
    private String data;
    private ArrayList<OrderItemResDTO> orderedItems = new ArrayList<>();
    private ArrayList<OrderChargesResDTO> orderCharges = new ArrayList<>();
    private long orderId;
    private double amountInSAR, amountInUSD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_and_pay);

        itemList = (RecyclerView) findViewById(R.id.item_list);
        chargeList = (RecyclerView) findViewById(R.id.charge_list);
        payLay = (LinearLayout) findViewById(R.id.payLay);
        txtAmountSar = (TextView) findViewById(R.id.txt_total_sar);
        txtAmountUsd = (TextView) findViewById(R.id.txt_total_amount_usd);
        setTitle(getString(R.string.str_confirm_n_pay));
        payLay.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            data = bundle.getString(ORDER_INIT_DETAILS);
            InitOrderResDTO orderResDTO = InitOrderResDTO.deserializeJson(data);
            orderedItems = orderResDTO.getOrderedItems();
            orderCharges = orderResDTO.getChargeBreakup();
            orderId = orderResDTO.getOrderId();
            amountInSAR = orderResDTO.getTotalAmountInSAR();
            amountInUSD = orderResDTO.getTotalAmountInUSD();

            txtAmountSar.setText(String.format("%.2f", amountInSAR));
            txtAmountUsd.setText(String.format("%.2f", amountInUSD));
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.str_err_order_init),
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        LinearLayoutManager itemLayManager = new LinearLayoutManager(this);
        itemLayManager.setOrientation(LinearLayoutManager.VERTICAL);
        itemList.setLayoutManager(itemLayManager);

        LinearLayoutManager chargeLayManager = new LinearLayoutManager(this);
        chargeLayManager.setOrientation(LinearLayoutManager.VERTICAL);
        chargeList.setLayoutManager(chargeLayManager);

        confirmListAdapter = new ConfirmChargeAdapter(getApplicationContext(), orderCharges);
        chargeList.setAdapter(confirmListAdapter);

        userConfirmListAdapter = new UserConfirmListAdapter(getApplicationContext(), orderedItems);
        itemList.setAdapter(userConfirmListAdapter);

        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);

    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.payLay:
                break;
        }
    }
}
