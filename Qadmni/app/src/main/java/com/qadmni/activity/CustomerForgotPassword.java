package com.qadmni.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qadmni.R;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.CustomerForgotPasswordDTO;
import com.qadmni.utils.NetworkUtils;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;
import com.qadmni.utils.Validator;

public class CustomerForgotPassword extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {
    private Button mForgotPassword;
    private EditText mUserEmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_forgot_password);
        mForgotPassword = (Button) findViewById(R.id.forgotPassword);
        mUserEmailId = (EditText) findViewById(R.id.edt_user_email);
        mForgotPassword.setOnClickListener(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.forgotPassword:
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
        String strEmailId = mUserEmailId.getText().toString();
        View focusView = null;
        boolean check = false;
        if (strEmailId.isEmpty()) {
            focusView = mUserEmailId;
            check = true;
            mUserEmailId.setError(getString(R.string.str_err_pass_emty));
        } else if (!Validator.isValidMail(strEmailId)) {
            focusView = mUserEmailId;
            check = true;
            mUserEmailId.setError(getString(R.string.str_err_email_empty));
        }
        if (check) {
            focusView.requestFocus();
        } else {
            progressDialog.show();
            CustomerForgotPasswordDTO customerForgotPasswordDTO = new CustomerForgotPasswordDTO(strEmailId);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(customerForgotPasswordDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_CUSTOMER_FORGOT_PASSWORD,
                    mSessionManager.customerForgotPwUrl(), baseRequestDTO);
        }

    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_CUSTOMER_FORGOT_PASSWORD:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }
        Log.d("TAG", "TAG");
        Log.d("TAG", "TAG");
        Log.d("TAG", "TAG");


    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_CUSTOMER_FORGOT_PASSWORD:
                customAlterDialog(getString(R.string.str_register_err_title), errorMessage);
                break;
        }
        Log.d("TAG", "TAG");
        Log.d("TAG", "TAG");
        Log.d("TAG", "TAG");

    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_CUSTOMER_FORGOT_PASSWORD:
                Toast.makeText(getApplicationContext(), "Email is send"
                        , Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
