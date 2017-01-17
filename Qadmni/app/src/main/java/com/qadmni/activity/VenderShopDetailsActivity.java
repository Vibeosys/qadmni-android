package com.qadmni.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.qadmni.R;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.RegisterVendorReqDTO;
import com.qadmni.utils.NetworkUtils;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

public class VenderShopDetailsActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnSuccessResultReceived, ServerSyncManager.OnErrorResultReceived {

    public static final int LOCATION_RESULT = 1;
    public static final String SHOP_DETAILS = "shopDetails";
    private EditText edtShopName;
    private Button btnRegister;
    private LatLng shopLatLng;
    private String shopAddress, shopName;
    private TextView txtShopLocation;
    private RegisterVendorReqDTO registerVendorReqDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender_shop_details);
        getSupportActionBar().hide();
        registerVendorReqDTO = (RegisterVendorReqDTO) getIntent().getExtras().getSerializable(SHOP_DETAILS);
        edtShopName = (EditText) findViewById(R.id.edt_shop_name);
        txtShopLocation = (TextView) findViewById(R.id.txt_shop_location);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        txtShopLocation.setOnClickListener(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_register:
                if (!NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                    createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                            getResources().getString(R.string.str_err_net_msg));
                } else {
                    registerBusiness();
                }
                break;
            case R.id.txt_shop_location:
                Intent iRegister = new Intent(getApplicationContext(), SearchAddressActivity.class);
                startActivityForResult(iRegister, LOCATION_RESULT);
                break;
        }
    }

    private void registerBusiness() {
        String strBusinessName = edtShopName.getText().toString();
        String strBusinessNameAr = "d mart";
        View focusView = null;
        boolean check = false;
        if (strBusinessName.isEmpty()) {
            focusView = edtShopName;
            check = true;
            edtShopName.setError(getString(R.string.str_err_shop_name_empty));
        } else if (shopAddress.isEmpty()) {
            focusView = txtShopLocation;
            check = true;
            txtShopLocation.setError(getString(R.string.str_err_shop_address_empty));
        }

        if (check) {
            focusView.requestFocus();
        } else {
            //Request to server
            progressDialog.show();
            registerVendorReqDTO.setBusinessNameEn(strBusinessName);
            registerVendorReqDTO.setBusinessNameAr(strBusinessNameAr);
            registerVendorReqDTO.setBusinessAddress(shopAddress);
            registerVendorReqDTO.setBusinessLat(shopLatLng.latitude);
            registerVendorReqDTO.setBusinessLong(shopLatLng.longitude);
            registerVendorReqDTO.setPushNotificationId("asdjasdjo");
            registerVendorReqDTO.setOsVersionType("AR");
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(registerVendorReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_VENDOR_REG,
                    mSessionManager.registerBusiness(), baseRequestDTO);
        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_VENDOR_REG:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_VENDOR_REG:
                customAlterDialog(getString(R.string.str_register_err_title), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_VENDOR_REG:
                Toast.makeText(getApplicationContext(), getString(R.string.str_register_murchent_success), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), VendorLoginActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                shopLatLng = data.getExtras().getParcelable(SearchAddressActivity.SELECTED_PLACE_LAT_LNG);
                shopAddress = data.getStringExtra(SearchAddressActivity.SELECTED_PLACE_ADDRESS);
                txtShopLocation.setText(shopAddress);
            } else {
                shopLatLng = null;
                shopAddress = "";
            }

        }
    }
}
