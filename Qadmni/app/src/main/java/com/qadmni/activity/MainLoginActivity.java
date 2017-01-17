package com.qadmni.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qadmni.MainActivity;
import com.qadmni.R;
import com.qadmni.utils.UserType;

public class MainLoginActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout layQuick, layLogin;
    TextView txtVendorLogin;
    private int userType = UserType.USER_OTHER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        /** Making this activity, full screen */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        getSupportActionBar().hide();
        layQuick = (LinearLayout) findViewById(R.id.lay_quick);
        layLogin = (LinearLayout) findViewById(R.id.lay_login);
        txtVendorLogin = (TextView) findViewById(R.id.txt_vender_login);

        layQuick.setOnClickListener(this);
        layLogin.setOnClickListener(this);
        txtVendorLogin.setOnClickListener(this);
        userType = mSessionManager.getUserType();

        if (userType == UserType.USER_VENDOR) {
            startActivity(new Intent(getApplicationContext(), VendorMainActivity.class));
            finish();
        } else if (userType == UserType.USER_CUSTOMER) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else if (userType == UserType.USER_OTHER) {

        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.lay_quick:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.lay_login:
                startActivity(new Intent(getApplicationContext(), CustomerLoginActivity.class));
                finish();
                break;
            case R.id.txt_vender_login:
                startActivity(new Intent(getApplicationContext(), VendorLoginActivity.class));
                finish();
                break;
        }
    }
}
