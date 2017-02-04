package com.qadmni.activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qadmni.R;
import com.qadmni.adapters.CategorySpinnerAdapter;
import com.qadmni.data.requestDataDTO.AddProductImgReqDTO;
import com.qadmni.data.requestDataDTO.AddProductReqDTO;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.ItemDetailsReqDTO;
import com.qadmni.data.requestDataDTO.UpdateProductReqDTO;
import com.qadmni.data.responseDataDTO.AddProductResDTO;
import com.qadmni.data.responseDataDTO.BaseResponseDTO;
import com.qadmni.data.responseDataDTO.CategoryListResponseDTO;
import com.qadmni.data.responseDataDTO.ItemDetailsResDTO;
import com.qadmni.data.responseDataDTO.VendorItemResDTO;
import com.qadmni.utils.MultipartUtility;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class AddOrUpdateProductActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {
    public static final String IS_NEW_PRODUCT = "isNewProduct";
    public static final String PRODUCT_DATA = "productData";
    private static final String TAG = AddOrUpdateProductActivity.class.getSimpleName();
    private EditText edtProductName, edtProductNameAr, edtProductDetails, edtProductDetailsAr,
            edtProductPrice, edtProductOffer;
    private Button btnCancel, btnSave;
    private RelativeLayout btnUploadImg;
    // private boolean isNewProduct;
    private long productId;
    private ItemDetailsResDTO item;
    private ImageView imgProduct;
    private int EDIT_PROFILE_MEDIA_PERMISSION_CODE = 19;
    private int EDIT_SELECT_IMAGE = 20;
    private String selectedPath = "No Pic";
    private Spinner spnCategory;
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private int selectedCategoryId;
    private boolean isImageChanged = false;
    private Switch swtAvailable;
    private Bundle bundle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_update_product);
        edtProductName = (EditText) findViewById(R.id.edt_product_name);
        edtProductNameAr = (EditText) findViewById(R.id.edt_product_name_ar);
        edtProductDetails = (EditText) findViewById(R.id.edt_product_details);
        edtProductDetailsAr = (EditText) findViewById(R.id.edt_product_details_ar);
        edtProductPrice = (EditText) findViewById(R.id.edt_price);
        edtProductOffer = (EditText) findViewById(R.id.edt_offer);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSave = (Button) findViewById(R.id.btn_save);
        swtAvailable = (Switch) findViewById(R.id.swtAvailable);
        imgProduct = (ImageView) findViewById(R.id.img_product);
        spnCategory = (Spinner) findViewById(R.id.spn_product_category);
        btnUploadImg = (RelativeLayout) findViewById(R.id.btn_upload_img);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            //isNewProduct = bundle.getBoolean(IS_NEW_PRODUCT);
            productId = bundle.getLong("productId");
        }
        btnUploadImg.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);

        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_GET_CATEGORY,
                mSessionManager.getCategoryListUrl(), baseRequestDTO);

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryListResponseDTO categoryListResponseDTO = (CategoryListResponseDTO)
                        categorySpinnerAdapter.getItem(i);
                selectedCategoryId = categoryListResponseDTO.getCategoryId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupUi() {
        edtProductName.setText(item.getItemNameEn());
        edtProductNameAr.setText(item.getItemNameAr());
        edtProductDetails.setText(item.getItemDescEn());
        edtProductDetailsAr.setText(item.getItemDescAr());
        edtProductOffer.setText(item.getOfferText());
        edtProductPrice.setText(String.format("%.2f", item.getUnitPrice()));
        swtAvailable.setChecked(item.isActive() == 0 ? false : true);
        //spnCategory.setSelection();
        DownloadImage downloadImage = new DownloadImage();
        downloadImage.execute(item.getImageUrl());
        spnCategory.setSelection(categorySpinnerAdapter.getPosition(item.getCategoryId()));
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
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_ADD_PRODUCT:
                break;
            case ServerRequestConstants.REQUEST_GET_CATEGORY:
                break;
            case ServerRequestConstants.REQUEST_UPDATE_PRODUCT:
                break;
            case ServerRequestConstants.REQUEST_ITEM_DETAILS:
                break;
        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_ADD_PRODUCT:
                customAlterDialog(getString(R.string.str_err_add_product), errorMessage);
                break;
            case ServerRequestConstants.REQUEST_GET_CATEGORY:
                customAlterDialog(getString(R.string.str_err_categry_list), errorMessage);
                break;
            case ServerRequestConstants.REQUEST_UPDATE_PRODUCT:
                customAlterDialog(getString(R.string.str_err_categry_list), errorMessage);
                break;
            case ServerRequestConstants.REQUEST_ITEM_DETAILS:
                customAlterDialog(getString(R.string.str_err_categry_list), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_ADD_PRODUCT: {
                AddProductResDTO addProductResDTO = AddProductResDTO.deserializeJson(data);
                AddProductImgReqDTO addProductImgReqDTO = new AddProductImgReqDTO(
                        mSessionManager.getVendorId(), mSessionManager.getVendorPassword(),
                        addProductResDTO.getProductId());
                Gson gson = new Gson();
                String serializedJsonString = gson.toJson(addProductImgReqDTO);
                SendPicVerify sendPic = new SendPicVerify();
                sendPic.execute(serializedJsonString, mSessionManager.uploadProductPhoto());
            }

            break;
            case ServerRequestConstants.REQUEST_GET_CATEGORY:
                ArrayList<CategoryListResponseDTO> categoryListResponseDTOs = CategoryListResponseDTO.deSerializedToJson(data);
                categorySpinnerAdapter = new CategorySpinnerAdapter(getApplicationContext(), categoryListResponseDTOs);
                spnCategory.setAdapter(categorySpinnerAdapter);
                if (bundle != null) {
                    callItemDetails();
                }
                break;
            case ServerRequestConstants.REQUEST_UPDATE_PRODUCT: {
                Toast.makeText(getApplicationContext(), getString(R.string.str_update_product_success),
                        Toast.LENGTH_SHORT).show();
                if (isImageChanged) {
                    AddProductImgReqDTO addProductImgReqDTO = new AddProductImgReqDTO(
                            mSessionManager.getVendorId(), mSessionManager.getVendorPassword(),
                            item.getItemId());
                    Gson gson = new Gson();
                    String serializedJsonString = gson.toJson(addProductImgReqDTO);
                    SendPicVerify sendPic = new SendPicVerify();
                    sendPic.execute(serializedJsonString, mSessionManager.uploadProductPhoto());
                } else {
                    startActivity(new Intent(getApplicationContext(), VendorMainActivity.class));
                    finish();
                }
            }
            break;
            case ServerRequestConstants.REQUEST_ITEM_DETAILS:
                item = ItemDetailsResDTO.deserializeJson(data);
                setupUi();
                break;
        }
    }

    private void callItemDetails() {
        ItemDetailsReqDTO itemDetailsReqDTO = new ItemDetailsReqDTO(productId);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(itemDetailsReqDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_ITEM_DETAILS,
                mSessionManager.getItemDetails(), baseRequestDTO);
    }

    private void updateData() {
        String strProductName = edtProductName.getText().toString();
        String strProductNameAr = edtProductNameAr.getText().toString();
        String strProductDetails = edtProductDetails.getText().toString();
        String strProductDetailsAr = edtProductDetailsAr.getText().toString();
        String strProductOffer = edtProductOffer.getText().toString();
        double price = Double.parseDouble(edtProductPrice.getText().toString());
        boolean isAvail = swtAvailable.isChecked();
        View focusView = null;
        boolean check = false;
        if (strProductName.isEmpty()) {
            check = true;
            focusView = edtProductName;
            edtProductName.setError(getString(R.string.str_err_product_name_empty));
        } else if (strProductNameAr.isEmpty()) {
            check = true;
            focusView = edtProductNameAr;
            edtProductNameAr.setError(getString(R.string.str_err_product_name_ar_empty));
        } else if (strProductDetails.isEmpty()) {
            check = true;
            focusView = edtProductDetails;
            edtProductDetails.setError(getString(R.string.str_err_product_details_empty));
        } else if (strProductDetailsAr.isEmpty()) {
            check = true;
            focusView = edtProductDetailsAr;
            edtProductDetailsAr.setError(getString(R.string.str_err_product_details_ar_empty));
        } else if (price == 0) {
            check = true;
            focusView = edtProductPrice;
            edtProductPrice.setError(getString(R.string.str_err_price_empty));
        }
        if (bundle != null) {

        } else {
            if (selectedPath.equals("No Pic")) {
                check = true;
                focusView = imgProduct;
                Toast.makeText(getApplicationContext(), getString(R.string.str_err_image), Toast.LENGTH_SHORT).show();
            }
        }

        if (check) {
            focusView.requestFocus();
        } else {
            //Send data on server
            if (item == null) {
                progressDialog.show();
                AddProductReqDTO addProductReqDTO = new AddProductReqDTO(strProductName, strProductNameAr
                        , strProductDetails, strProductDetailsAr, selectedCategoryId, price, strProductOffer);
                Gson gson = new Gson();
                String serializedJsonString = gson.toJson(addProductReqDTO);
                BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
                baseRequestDTO.setData(serializedJsonString);
                mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_ADD_PRODUCT,
                        mSessionManager.addProductUrl(), baseRequestDTO);
            } else {
                progressDialog.show();
                UpdateProductReqDTO updateProductReqDTO = new UpdateProductReqDTO(item.getItemId(),
                        isAvail, strProductName, strProductNameAr, strProductDetails, strProductDetailsAr,
                        selectedCategoryId, price, strProductOffer);
                Gson gson = new Gson();
                String serializedJsonString = gson.toJson(updateProductReqDTO);
                BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
                baseRequestDTO.setData(serializedJsonString);
                mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_UPDATE_PRODUCT,
                        mSessionManager.updateProductUrl(), baseRequestDTO);
            }

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
            isImageChanged = true;
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

    // DownloadImage AsyncTask
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
                bitmap = null;
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imgProduct.setImageBitmap(result);
            } else {
                imgProduct.setImageResource(R.drawable.default_img);
            }
        }
    }
}
