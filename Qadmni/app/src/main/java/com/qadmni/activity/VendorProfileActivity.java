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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qadmni.R;
import com.qadmni.utils.NetworkUtils;

public class VendorProfileActivity extends BaseActivity implements View.OnClickListener {
    private TextView mUpdateProfile;
    private EditText mUserFirstName, mUserEmailId, mUserPassword;
    private ImageView mUserPhoto;
    public static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final int REQUEST_SELECT_PICTURE = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile);
        mUpdateProfile = (TextView) findViewById(R.id.saveProfile_vendor);
        mUserFirstName = (EditText) findViewById(R.id.userName);
        mUserEmailId = (EditText) findViewById(R.id.userEmailId);
        mUserPassword = (EditText) findViewById(R.id.userPassword);
        mUserPhoto = (ImageView) findViewById(R.id.circleViewImage);
        mUserEmailId.setEnabled(false);
        mUserFirstName.setText("" + mSessionManager.getVendorName());
        mUserEmailId.setText("" + mSessionManager.getVendorEmailId());
        mUserPassword.setText("" + mSessionManager.getVendorPassword());

        mUserPhoto.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.circleViewImage:
                openGallery();
                break;
            case R.id.saveProfile_vendor:
               boolean result= callToValidation();
                if(result)
                {
                    if(NetworkUtils.isActiveNetworkAvailable(getApplicationContext()))
                    {
                        callToWebService();
                    }
                    else
                    {
                        customAlterDialog(getResources().getString(R.string.str_net_err),getResources().getString(R.string.str_err_net_msg));
                    }

                }
        }

    }

    private void callToWebService() {

    }

    private boolean callToValidation() {
        String userName = mUserFirstName.getText().toString().trim();
        String userEmail = mUserEmailId.getText().toString().trim();
        String userPassword = mUserPassword.getText().toString().trim();
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
        return true;
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VendorProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.str_select_picture)), VendorProfileActivity.REQUEST_SELECT_PICTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            // Get the cursor
            Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgString = cursor.getString(columnIndex);
            cursor.close();
            mUserPhoto.setImageBitmap(BitmapFactory.decodeFile(imgString));
            mUserPhoto.requestFocus();

        }
    }
}
