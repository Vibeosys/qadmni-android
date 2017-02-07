package com.qadmni.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qadmni.R;

public class UserProfileActivity extends BaseActivity {

    private TextView mUpdateProfile;
    private EditText mUserFirstName, mUserEmailId, mUserPassword;
    private ImageView mUserPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mUpdateProfile = (TextView) findViewById(R.id.saveProfile);
        mUserFirstName = (EditText) findViewById(R.id.userName);
        mUserEmailId = (EditText) findViewById(R.id.userEmailId);
        mUserPassword = (EditText) findViewById(R.id.userPassword);
        mUserPhoto = (ImageView) findViewById(R.id.circleViewUser);
        mUserEmailId.setEnabled(false);
        String userName =   mSessionManager.getCustomerName();
        mUserFirstName.setText("" + mSessionManager.getCustomerName());
        mUserEmailId.setText("" + mSessionManager.getUserEmail());
        mUserPassword.setText("" + mSessionManager.getUserPass());
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
}
