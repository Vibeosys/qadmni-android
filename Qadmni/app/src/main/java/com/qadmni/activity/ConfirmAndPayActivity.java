package com.qadmni.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.qadmni.MainActivity;
import com.qadmni.R;
import com.qadmni.adapters.ConfirmChargeAdapter;
import com.qadmni.adapters.OrderHistoryAdapter;
import com.qadmni.adapters.UserConfirmListAdapter;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.ConfirmOrderReqDTO;
import com.qadmni.data.requestDataDTO.ProcessOrderReqDTO;
import com.qadmni.data.requestDataDTO.VendorLoginReqDTO;
import com.qadmni.data.responseDataDTO.InitOrderResDTO;
import com.qadmni.data.responseDataDTO.OrderChargesResDTO;
import com.qadmni.data.responseDataDTO.OrderItemResDTO;
import com.qadmni.data.responseDataDTO.OrderProcessResDTO;
import com.qadmni.utils.OneSignalIdHandler;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ConfirmAndPayActivity extends BaseActivity implements
        ServerSyncManager.OnSuccessResultReceived, ServerSyncManager.OnErrorResultReceived,
        View.OnClickListener {

    public static final java.lang.String ORDER_INIT_DETAILS = "orderInitDetails";
    private RecyclerView itemList /*chargeList*/;
    private LinearLayout payLay;
    private TextView txtAmountSar, txtAmountUsd;
    private ConfirmChargeAdapter confirmListAdapter;
    private UserConfirmListAdapter userConfirmListAdapter;
    private String data;
    private ArrayList<OrderItemResDTO> orderedItems = new ArrayList<>();
   // private ArrayList<OrderChargesResDTO> orderCharges = new ArrayList<>();
    private long orderId;
    private double amountInSAR, amountInUSD;
    private static PayPalConfiguration config;
    private OrderProcessResDTO orderProcessResDTO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_and_pay);

        itemList = (RecyclerView) findViewById(R.id.item_list);
        //chargeList = (RecyclerView) findViewById(R.id.charge_list);
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
            //orderCharges = orderResDTO.getChargeBreakup();
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
        //chargeList.setLayoutManager(chargeLayManager);

        //confirmListAdapter = new ConfirmChargeAdapter(getApplicationContext(), orderCharges);
        //chargeList.setAdapter(confirmListAdapter);

        userConfirmListAdapter = new UserConfirmListAdapter(getApplicationContext(), orderedItems);
        itemList.setAdapter(userConfirmListAdapter);

        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);

    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_PROCESS_ORDER:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
            case ServerRequestConstants.REQUEST_CONFIRM_ORDER:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_PROCESS_ORDER:
                customAlterDialog(getString(R.string.str_err_order_confirm), errorMessage);
                //FirebaseCrash.report(new Exception("Order Process is not work on server side"));
                break;
            case ServerRequestConstants.REQUEST_CONFIRM_ORDER:
                customAlterDialog(getString(R.string.str_err_order_confirm), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_PROCESS_ORDER:
                orderProcessResDTO = OrderProcessResDTO.deserializeJson(data);
                if (orderProcessResDTO.isTransactionRequired()) {
                    payPalCall(orderProcessResDTO);
                } else {
                    cashCall(orderProcessResDTO);
                }
                break;
            case ServerRequestConstants.REQUEST_CONFIRM_ORDER:
                Toast.makeText(getApplicationContext(), getResources().
                        getString(R.string.str_order_confirm_success), Toast.LENGTH_SHORT).show();
                //clear database
                qadmniHelper.deleteMyCartDetails();
                mSessionManager.setProducerId(0);
                startActivity(new Intent(getApplicationContext(), UserOrderHistoryActivity.class));
                finish();
                break;
        }
    }

    private void cashCall(OrderProcessResDTO orderProcessResDTO) {
        progressDialog.show();
        ConfirmOrderReqDTO confirmOrderReqDTO = new ConfirmOrderReqDTO(orderProcessResDTO.getOrderId(),
                orderProcessResDTO.getTransactionId(), amountInSAR, amountInUSD, "");
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(confirmOrderReqDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_CONFIRM_ORDER,
                mSessionManager.confirmOrderUrl(), baseRequestDTO);
    }

    private void payPalCall(OrderProcessResDTO orderProcessResDTO) {
        config = new PayPalConfiguration()
                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                // or live (ENVIRONMENT_PRODUCTION)
                .environment(orderProcessResDTO.getPaypalEnvValues().getEnvironment())
                .clientId(orderProcessResDTO.getPaypalEnvValues().getClientId());

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        PayPalPayment payment = new PayPalPayment(new BigDecimal(orderProcessResDTO.getAmount()),
                orderProcessResDTO.getCurrency(), orderProcessResDTO.getPaypalEnvValues().getPaypalDesc(),
                PayPalPayment.PAYMENT_INTENT_SALE);
        payment.invoiceNumber(orderProcessResDTO.getTransactionId());
        Intent intentPay = new Intent(this, PaymentActivity.class);
        // send the same configuration for restart resiliency
        intentPay.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intentPay.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intentPay, 0);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.payLay:
                processOrder();
                break;
        }
    }

    private void processOrder() {
        progressDialog.show();
        ProcessOrderReqDTO processOrderReqDTO = new ProcessOrderReqDTO(orderId, amountInSAR, amountInUSD);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(processOrderReqDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_PROCESS_ORDER,
                mSessionManager.processOrderUrl(), baseRequestDTO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    progressDialog.show();
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    JSONObject confirmJson = confirm.toJSONObject();
                    JSONObject response = confirmJson.getJSONObject("response");
                    String paypalId = response.getString("id");
                    ConfirmOrderReqDTO confirmOrderReqDTO = new ConfirmOrderReqDTO(orderProcessResDTO.getOrderId(),
                            orderProcessResDTO.getTransactionId(), amountInSAR, amountInUSD, paypalId);
                    Gson gson = new Gson();
                    String serializedJsonString = gson.toJson(confirmOrderReqDTO);
                    BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
                    baseRequestDTO.setData(serializedJsonString);
                    mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_CONFIRM_ORDER,
                            mSessionManager.confirmOrderUrl(), baseRequestDTO);
                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }
}
