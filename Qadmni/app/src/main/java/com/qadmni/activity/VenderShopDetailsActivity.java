package com.qadmni.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.qadmni.R;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.RegisterVenderReqDTO;
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
    private RegisterVenderReqDTO registerVenderReqDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender_shop_details);
        getSupportActionBar().hide();
        registerVenderReqDTO = (RegisterVenderReqDTO) getIntent().getExtras().getSerializable(SHOP_DETAILS);
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
                registerBusiness();

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
        }

        if (check) {
            focusView.requestFocus();
        } else {
            //Request to server
            progressDialog.show();
            registerVenderReqDTO.setBusinessNameEn(strBusinessName);
            registerVenderReqDTO.setBusinessNameAr(strBusinessNameAr);
            registerVenderReqDTO.setBusinessAddress(shopAddress);
            registerVenderReqDTO.setBusinessLat(shopLatLng.latitude);
            registerVenderReqDTO.setBusinessLong(shopLatLng.longitude);
            registerVenderReqDTO.setPushNotificationId("asdjasdjo");
            registerVenderReqDTO.setOsVersionType("AR");
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(registerVenderReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_VENDER_REG,
                    mSessionManager.registerBusiness(), baseRequestDTO);
        }
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
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_VENDER_REG:
                Toast.makeText(getApplicationContext(), getString(R.string.str_register_murchent_success), Toast.LENGTH_SHORT).show();
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
            }

        }
    }
}
