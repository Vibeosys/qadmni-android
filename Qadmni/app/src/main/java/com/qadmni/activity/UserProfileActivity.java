package com.qadmni.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qadmni.MainActivity;
import com.qadmni.R;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.UserProfileDTO;
import com.qadmni.utils.NetworkUtils;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;
import com.qadmni.utils.UserAuth;
import com.qadmni.utils.Validator;

public class UserProfileActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {

    private TextView mUpdateProfile;
    private EditText mUserFirstName, mUserEmailId, mUserPassword, mUserPhoneNumber;
    private ImageView mUserPhoto;
    String userName;
    String userEmail;
    String userPassword;
    String phone;
    private static final int CALL_TO_LOGIN_FROM_PROFILE = 55;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (!UserAuth.isUserLoggedIn()) {
            startActivityForResult(new Intent(getApplicationContext(), CustomerLoginActivity.class), CALL_TO_LOGIN_FROM_PROFILE);
        }
        mUpdateProfile = (TextView) findViewById(R.id.saveProfile);
        mUserFirstName = (EditText) findViewById(R.id.userName);
        mUserEmailId = (EditText) findViewById(R.id.userEmailId);
        mUserPassword = (EditText) findViewById(R.id.userPassword);
        mUserPhoto = (ImageView) findViewById(R.id.circleViewUser);
        mUserPhoneNumber = (EditText) findViewById(R.id.userPhoneNumber);
        mUserEmailId.setEnabled(false);
        String userName = mSessionManager.getCustomerName();

        mUserFirstName.setText("" + mSessionManager.getCustomerName());
        mUserEmailId.setText("" + mSessionManager.getUserEmail());
        mUserPassword.setText("" + mSessionManager.getUserPass());
        mUserPhoneNumber.setText("" + mSessionManager.getCustomerPh());
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);

        mUpdateProfile.setOnClickListener(this);
        mUserPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mUserPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (!b) {
                    mUserPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_create_black_24dp, 0);
                }
            }
        });
        mUserFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mUserFirstName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (!b) {
                    mUserFirstName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_create_black_24dp, 0);
                }
            }
        });
        mUserPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mUserFirstName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (!b) {
                    mUserFirstName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_create_black_24dp, 0);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.saveProfile:
                boolean result = callToValidation();
                if (result) {
                    if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                        callToWebService();
                    } else {
                        customAlterDialog(getResources().getString(R.string.str_net_err), getResources().getString(R.string.str_err_net_msg));
                    }

                }
        }
    }

    private void callToWebService() {
        progressDialog.show();
        UserProfileDTO userProfileDTO = new UserProfileDTO(userEmail, userPassword, userName, phone);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(userProfileDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_USER_UPDATE_PROFILE,
                mSessionManager.getEditCustomerProfileUrl(), baseRequestDTO);
    }

    private boolean callToValidation() {
        userName = mUserFirstName.getText().toString().trim();
        userEmail = mUserEmailId.getText().toString().trim();
        userPassword = mUserPassword.getText().toString().trim();
        phone = mUserPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            mUserFirstName.requestFocus();
            mUserFirstName.setError(getResources().getString(R.string.str_pro_user));
            return false;
        }
        if (TextUtils.isEmpty(userPassword)) {
            mUserPassword.requestFocus();
            mUserPassword.setError(getResources().getString(R.string.str_pro_password));
            return false;
        }
        if (TextUtils.isEmpty(userEmail)) {
            mUserEmailId.requestFocus();
            mUserPassword.setError(getResources().getString(R.string.str_pro_email));
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            mUserPhoneNumber.requestFocus();
            mUserPhoneNumber.setError(getResources().getString(R.string.str_pro_phone));
            return false;
        }
        if (!Validator.isValidPhone(phone)) {
            mUserPhoneNumber.requestFocus();
            mUserPhoneNumber.setError(getResources().getString(R.string.str_pro_phone_invalid));
            return false;
        }
        return true;
    }


    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_USER_UPDATE_PROFILE:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_USER_UPDATE_PROFILE:
                customAlterDialog(getString(R.string.str_update_profile_err), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_USER_UPDATE_PROFILE:
                mSessionManager.setCustomerName(userName);
                mSessionManager.setUserPass(userPassword);
                mSessionManager.setUserEmail(userEmail);
                mSessionManager.setCustomerPh(phone);
                customAlterDialog(getString(R.string.str_user_profile), getString(R.string.str_profile_success));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CALL_TO_LOGIN_FROM_PROFILE) {
            if (requestCode == Activity.RESULT_OK) {

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
