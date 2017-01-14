package com.qadmni.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.qadmni.R;
import com.qadmni.utils.ServerSyncManager;

public class VenderShopDetailsActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnSuccessResultReceived, ServerSyncManager.OnErrorResultReceived {

    private EditText edtShopName, edtShopLocation;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender_shop_details);
        getSupportActionBar().hide();
        edtShopName = (EditText) findViewById(R.id.edt_shop_name);
        edtShopLocation = (EditText) findViewById(R.id.edt_shop_location);
        btnRegister = (Button) findViewById(R.id.btn_register);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_register:

                break;
        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {

    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {

    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {

    }
}
