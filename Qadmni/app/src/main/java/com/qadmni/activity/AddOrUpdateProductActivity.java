package com.qadmni.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qadmni.R;
import com.qadmni.data.requestDataDTO.AddProductImgReqDTO;
import com.qadmni.data.requestDataDTO.AddProductReqDTO;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.VendorLoginReqDTO;
import com.qadmni.data.responseDataDTO.AddProductResDTO;
import com.qadmni.data.responseDataDTO.BaseResponseDTO;
import com.qadmni.utils.MultipartUtility;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

import java.io.File;
import java.util.ArrayList;

public class AddOrUpdateProductActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {

    private static final String TAG = AddOrUpdateProductActivity.class.getSimpleName();
    private EditText edtProductName, edtProductDetails, edtProductPrice;
    private Button btnCancel, btnSave;
    private RelativeLayout btnUploadImg;
    private long productId;
    private ImageView imgProduct;
    private int EDIT_PROFILE_MEDIA_PERMISSION_CODE = 19;
    private int EDIT_SELECT_IMAGE = 20;
    private String selectedPath = "No Pic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_update_product);
        edtProductName = (EditText) findViewById(R.id.edt_product_name);
        edtProductDetails = (EditText) findViewById(R.id.edt_product_details);
        edtProductPrice = (EditText) findViewById(R.id.edt_price);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSave = (Button) findViewById(R.id.btn_save);
        imgProduct = (ImageView) findViewById(R.id.img_product);
        btnUploadImg = (RelativeLayout) findViewById(R.id.btn_upload_img);

        btnUploadImg.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_save:
                updateData();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_upload_img:
                requestGrantPermission();
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
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_ADD_PRODUCT:
                AddProductResDTO addProductResDTO = AddProductResDTO.deserializeJson(data);
                AddProductImgReqDTO addProductImgReqDTO = new AddProductImgReqDTO(
                        mSessionManager.getVendorId(), mSessionManager.getVendorPassword(),
                        addProductResDTO.getProductId());
                Gson gson = new Gson();
                String serializedJsonString = gson.toJson(addProductImgReqDTO);
                SendPicVerify sendPic = new SendPicVerify();
                sendPic.execute(serializedJsonString, mSessionManager.uploadProductPhoto());
                break;
        }
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
            AddProductReqDTO addProductReqDTO = new AddProductReqDTO(strProductName, "الشوكولاتة واقية"
                    , strProductDetails, "الشوكولاتة هي دلو كامل", 2, price, "No offer");
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(addProductReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_ADD_PRODUCT,
                    mSessionManager.addProductUrl(), baseRequestDTO);
        }
    }

    private void requestGrantPermission() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            this.requestPermissions(new String[]{Manifest.permission.MEDIA_CONTENT_CONTROL,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    EDIT_PROFILE_MEDIA_PERMISSION_CODE);
        } else {
            openGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EDIT_PROFILE_MEDIA_PERMISSION_CODE && grantResults[1] == 0) {
            openGallery();
        } else if (requestCode == EDIT_PROFILE_MEDIA_PERMISSION_CODE && grantResults[1] != 0) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "User denied permission", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), EDIT_SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_OK) {
            if (requestCode == EDIT_SELECT_IMAGE) {
                Uri selectedImageUri = data.getData();
                Uri uri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = this.getContentResolver().query(uri, filePathColumn, null, null, null);
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedPath = cursor.getString(columnIndex);
                } else {
                    Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                imgProduct.setImageBitmap(BitmapFactory.decodeFile(selectedPath));
            }
        }
    }

    public class SendPicVerify extends AsyncTask<String, Void, String> {
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String jsonData = params[0];
            String url = params[1];
            Log.d(TAG, "##" + jsonData);
            Log.d(TAG, "##" + url);
            File uploadFile = new File(selectedPath);
            System.out.println("##" + uploadFile.canRead());
            System.out.println("##" + uploadFile.isFile());
            try {
                MultipartUtility multipart = new MultipartUtility(
                        url, "UTF-8");
                multipart.addHeaderField("Content-Type", "multipart/form-data");
                multipart.addFormField("json", jsonData);
                multipart.addFilePart("photo", uploadFile);
                response = multipart.finish();

            } catch (Exception e) {
                Log.e(TAG, "##Error:" + e);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            try {
                BaseResponseDTO base = BaseResponseDTO.deserializeJson(result);
                if (base.getErrorCode() == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.str_image_upload_success)
                            , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), VendorMainActivity.class));
                    finish();
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "##" + result);
        }


    }
}
