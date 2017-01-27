package com.qadmni.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qadmni.MainActivity;
import com.qadmni.R;
import com.qadmni.adapters.MyCartAdapter;
import com.qadmni.data.MyCartDTO;
import com.qadmni.utils.UserAuth;

import java.util.ArrayList;
import java.util.List;

public class UserMyCartActivity extends BaseActivity implements MyCartAdapter.OnQuantityChangeListener {
    private static final int CALL_LOGIN = 32;
    ArrayList<MyCartDTO> myCartDTOs = null;
    MyCartAdapter myCartAdapter;
    ListView listView;
    LinearLayout mAddMoreLayout, bottomLayout;
    TextView editCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_my_cart);
        setTitle(getResources().getString(R.string.str_my_cart));
        listView = (ListView) findViewById(R.id.myCartList);
        mAddMoreLayout = (LinearLayout) findViewById(R.id.AddMoreLayout);
        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        editCart = (TextView) findViewById(R.id.editCart);
        myCartDTOs = qadmniHelper.getCartDetails();
        myCartAdapter = new MyCartAdapter(myCartDTOs, getApplicationContext());
        myCartAdapter.setOnQuantityChangeListener(this);
        listView.setAdapter(myCartAdapter);

        mAddMoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        editCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myCartAdapter.setVisiblity(true);
                myCartAdapter.notifyDataSetChanged();
            }
        });
        bottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserAuth.isUserLoggedIn()) {
                    startActivityForResult(new Intent(getApplicationContext(), CustomerLoginActivity.class), CALL_LOGIN);
                } else {
                    int count = qadmniHelper.getCountCartTable();
                    if(count==0)
                    {
                        customAlterDialog(getResources().getString(R.string.str_my_cart),getResources().getString(R.string.str_empty_cart));
                    }
                    if (count != 0) {
                        Intent intent = new Intent(getApplicationContext(), PlaceOrderActivity.class);
                        startActivity(intent);
                    }

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CALL_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                boolean chkLogin = data.getExtras().getBoolean(CustomerLoginActivity.LOGIN_RESULT);
                if (chkLogin) {

                }
            }
        }
    }

    @Override
    public void onQuantityChange(MyCartDTO myCartDTO, int position) {
        boolean result1 = qadmniHelper.editCart(myCartDTO);
    }

    @Override
    public void onDeleteClick(MyCartDTO myCartDTO, int position) {
        boolean result = qadmniHelper.deleteMyCartDetailsWithWhere(myCartDTO.getProductId());
        myCartAdapter.notifyDataSetChanged();
    }
}
