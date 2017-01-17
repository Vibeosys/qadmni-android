package com.qadmni.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.qadmni.R;
import com.qadmni.utils.ServerSyncManager;

public class AddOrUpdateProductActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {

    private EditText edtProductName, edtProductDetails, edtProductPrice;
    private Button btnCancel, btnSave;
    private RelativeLayout btnUploadImg;
    private long productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_update_product);
        edtProductName = (EditText) findViewById(R.id.edt_product_name);
        edtProductDetails = (EditText) findViewById(R.id.edt_product_details);
        edtProductPrice = (EditText) findViewById(R.id.edt_price);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnUploadImg = (RelativeLayout) findViewById(R.id.btn_upload_img);

        btnUploadImg.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_save:
                updateData();
                break;
            case R.id.btn_cancel:
                break;
            case R.id.btn_upload_img:
                break;
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
    }

    private void updateData() {
        String strProductName = edtProductName.getText().toString();
        String strProductDetails = edtProductDetails.getText().toString();
        double price = Double.parseDouble(edtProductPrice.getText().toString());

        View focusView = null;
        boolean check = false;
        if (strProductName.isEmpty()) {
            check = true;
            focusView = edtProductName;
            edtProductName.setError(getString(R.string.str_err_product_name_empty));
        } else if (strProductDetails.isEmpty()) {
            check = true;
            focusView = edtProductDetails;
            edtProductDetails.setError(getString(R.string.str_err_product_details_empty));
        } else if (price == 0) {
            check = true;
            focusView = edtProductPrice;
            edtProductPrice.setError(getString(R.string.str_err_price_empty));
        }

        if (check) {
            focusView.requestFocus();
        } else {
            //Send data on server
        }
    }
}
