package com.qadmni.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qadmni.R;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.CustomerLoginReqDTO;
import com.qadmni.data.requestDataDTO.DuplicateEmailReqDTO;
import com.qadmni.data.requestDataDTO.RegisterVendorReqDTO;
import com.qadmni.utils.NetworkUtils;
import com.qadmni.utils.OneSignalIdHandler;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;
import com.qadmni.utils.Validator;

public class VendorRegistrationActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {

    private EditText edtName, edtPassword, edtConfirmPass, edtEmail, edtPhone;
    private Button btnNext;
    private String strName, strPassword, confirmPass, strEmail, strPh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        /** Making this activity, full screen */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender_registration);
        getSupportActionBar().hide();

        edtName = (EditText) findViewById(R.id.edt_name);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtConfirmPass = (EditText) findViewById(R.id.edt_confirm_pass);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_next:
                if (!NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                    createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                            getResources().getString(R.string.str_err_net_msg));
                } else {
                    checkEmail();
                }
                break;

        }
    }

    private void checkEmail() {
        strName = edtName.getText().toString();
        strPassword = edtPassword.getText().toString();
        confirmPass = edtConfirmPass.getText().toString();
        strEmail = edtEmail.getText().toString();
        strPh = edtPhone.getText().toString();

        View focusView = null;
        boolean check = false;
        if (strName.isEmpty()) {
            focusView = edtName;
            check = true;
            edtName.setError(getString(R.string.str_err_name_empty));
        } else if (strPassword.isEmpty()) {
            focusView = edtPassword;
            check = true;
            edtPassword.setError(getString(R.string.str_err_pass_emty));
        } else if (confirmPass.isEmpty()) {
            focusView = edtConfirmPass;
            check = true;
            edtConfirmPass.setError(getString(R.string.str_err_pass_emty));
        } else if (strEmail.isEmpty()) {
            focusView = edtEmail;
            check = true;
            edtEmail.setError(getString(R.string.str_err_email_empty));
        } else if (strPh.isEmpty()) {
            focusView = edtPhone;
            check = true;
            edtPhone.setError(getString(R.string.str_err_ph_empty));
        } else if (!strPassword.equals(confirmPass)) {
            focusView = edtConfirmPass;
            check = true;
            edtConfirmPass.setError(getString(R.string.str_err_pass_wrong));
        } else if (!Validator.isValidMail(strEmail)) {
            focusView = edtEmail;
            check = true;
            edtEmail.setError(getString(R.string.str_err_email_wrong));
        } else if (!Validator.isValidPhone(strPh)) {
            focusView = edtPhone;
            check = true;
            edtPhone.setError(getString(R.string.str_err_ph_wrong));
        }

        if (check) {
            focusView.requestFocus();
        } else {
            //send request to server for email check
            progressDialog.show();
            DuplicateEmailReqDTO duplicateEmailReqDTO = new DuplicateEmailReqDTO(strEmail);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(duplicateEmailReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_IS_EMAIL_DUPLICATE,
                    mSessionManager.isDuplicateUrl(), baseRequestDTO);
        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_IS_EMAIL_DUPLICATE:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_IS_EMAIL_DUPLICATE:
                customAlterDialog(getString(R.string.str_email_error), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_IS_EMAIL_DUPLICATE:
                RegisterVendorReqDTO registerVendorReqDTO = new RegisterVendorReqDTO();
                registerVendorReqDTO.setProducerName(strName);
                registerVendorReqDTO.setEmailId(strEmail);
                registerVendorReqDTO.setPassword(strPassword);
                Intent iBusinessDetails = new Intent(getApplicationContext(), VenderShopDetailsActivity.class);
                iBusinessDetails.putExtra(VenderShopDetailsActivity.SHOP_DETAILS, registerVendorReqDTO);
                startActivity(iBusinessDetails);
                break;
        }
    }
}
