package com.qadmni.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qadmni.MainActivity;
import com.qadmni.R;
import com.qadmni.data.VendorDTO;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.VendorLoginReqDTO;
import com.qadmni.data.responseDataDTO.VendorLoginResDTO;
import com.qadmni.helpers.LocaleHelper;
import com.qadmni.utils.NetworkUtils;
import com.qadmni.utils.OneSignalIdHandler;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;
import com.qadmni.utils.UserAuth;

public class VendorLoginActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {

    private EditText edtUserName, edtPassword;
    private TextView txtForgotPass, txtNewUser;
    private Button btnLogin;
    private String userName, password;
    //private ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender_login);
        getSupportActionBar().hide();
        edtUserName = (EditText) findViewById(R.id.edt_user_name);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        txtForgotPass = (TextView) findViewById(R.id.txt_forgot_pass);
        txtNewUser = (TextView) findViewById(R.id.txt_new_acc);
        //imgLogo = (ImageView) findViewById(R.id.imgLogo);
        btnLogin = (Button) findViewById(R.id.btn_login);

        txtForgotPass.setOnClickListener(this);
        txtNewUser.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        String language = LocaleHelper.getLanguage(getApplicationContext());
       /* if (language.equals("en")) {
            imgLogo.setImageDrawable(getResources().getDrawable(R.drawable.qadmni_img_logo_english, null));
        } else if (language.equals("ar")) {
            imgLogo.setImageDrawable(getResources().getDrawable(R.drawable.qadmni_img_logo_arabic, null));
        }*/

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_login:
                if (!NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                    createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                            getResources().getString(R.string.str_err_net_msg));
                } else {
                    loginVendor();
                }
                break;
            case R.id.txt_forgot_pass:
                Intent intent = new Intent(getApplicationContext(), VenderForgotPassword.class);
                startActivity(intent);
                break;
            case R.id.txt_new_acc:
                startActivity(new Intent(getApplicationContext(), VendorRegistrationActivity.class));
                finish();
                break;
        }
    }

    private void loginVendor() {
        userName = edtUserName.getText().toString();
        password = edtPassword.getText().toString();
        View focusView = null;
        boolean check = false;

        if (userName.isEmpty()) {
            focusView = edtUserName;
            check = true;
            edtUserName.setError(getString(R.string.str_err_username_empty));
        } else if (password.isEmpty()) {
            focusView = edtPassword;
            check = true;
            edtPassword.setError(getString(R.string.str_err_pass_emty));
        }

        if (check) {
            focusView.requestFocus();
        } else {
            progressDialog.show();
            OneSignalIdHandler signalIdHandler = new OneSignalIdHandler();
            VendorLoginReqDTO loginReqDTO = new VendorLoginReqDTO(userName, password, signalIdHandler.getUserId(), "AN");
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(loginReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_VENDOR_LOGIN,
                    mSessionManager.loginVendorUrl(), baseRequestDTO);

        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_VENDOR_LOGIN:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_VENDOR_LOGIN:
                customAlterDialog(getString(R.string.str_login_err_title), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_VENDOR_LOGIN:
                VendorLoginResDTO vendorLoginResDTO = VendorLoginResDTO.deserializeJson(data);
                VendorDTO vendorDTO = new VendorDTO(vendorLoginResDTO.getProducerId(),
                        vendorLoginResDTO.getProducerName(), vendorLoginResDTO.getBusinessNameEn(),
                        vendorLoginResDTO.getBusinessNameAr(), vendorLoginResDTO.getBusinessAddress(),
                        vendorLoginResDTO.getBusinessLat(), vendorLoginResDTO.getBusinessLong());
                vendorDTO.setEmailId(userName);
                vendorDTO.setPassword(password);
                UserAuth userAuth = new UserAuth();
                userAuth.saveVendorInfo(vendorDTO, getApplicationContext());
                startActivity(new Intent(getApplicationContext(), VendorMainActivity.class));
                finish();
                break;
        }
    }
}
