package com.qadmni.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qadmni.MainActivity;
import com.qadmni.R;
import com.qadmni.data.CustomerDTO;
import com.qadmni.data.VendorDTO;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.CustomerLoginReqDTO;
import com.qadmni.data.requestDataDTO.VendorLoginReqDTO;
import com.qadmni.data.responseDataDTO.CustomerLoginResDTO;
import com.qadmni.data.responseDataDTO.VendorLoginResDTO;
import com.qadmni.utils.NetworkUtils;
import com.qadmni.utils.OneSignalIdHandler;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;
import com.qadmni.utils.UserAuth;

public class CustomerLoginActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {
    private EditText edtUserName, edtPassword;
    private TextView txtForgotPass, txtNewUser;
    private Button btnLogin;
    private String userName, password;
    public final static String LOGIN_RESULT = "loginResult";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        getSupportActionBar().hide();
        edtUserName = (EditText) findViewById(R.id.edt_user_name);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        txtForgotPass = (TextView) findViewById(R.id.txt_forgot_pass);
        txtNewUser = (TextView) findViewById(R.id.txt_new_acc);
        btnLogin = (Button) findViewById(R.id.btn_login);
        txtForgotPass.setOnClickListener(this);
        txtNewUser.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
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
                    loginCustomer();
                }

                break;
            case R.id.txt_forgot_pass:
                startActivity(new Intent(getApplicationContext(), CustomerForgotPassword.class));
                break;
            case R.id.txt_new_acc:
                startActivity(new Intent(getApplicationContext(), CustomerRegisterActivity.class));
                break;
        }
    }

    private void loginCustomer() {
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
            CustomerLoginReqDTO loginReqDTO = new CustomerLoginReqDTO(userName, password, signalIdHandler.getUserId(), "AN");
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(loginReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_CUSTOMER_LOGIN,
                    mSessionManager.loginCostomerUrl(), baseRequestDTO);

        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_CUSTOMER_LOGIN:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_CUSTOMER_LOGIN:
                customAlterDialog(getString(R.string.str_login_err_title), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_CUSTOMER_LOGIN:
                CustomerLoginResDTO customerLoginResDTO = CustomerLoginResDTO.deserializeJson(data);
                CustomerDTO customerDTO = new CustomerDTO(customerLoginResDTO.getCustomerId(),
                        customerLoginResDTO.getName(), customerLoginResDTO.getPhone());
                customerDTO.setEmailId(userName);
                customerDTO.setPassword(password);
                UserAuth userAuth = new UserAuth();
                userAuth.saveUserInfo(customerDTO, getApplicationContext());
               /* startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();*/
                Intent intent = new Intent();
                intent.putExtra(LOGIN_RESULT, true);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra(LOGIN_RESULT, false);
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
