package com.qadmni.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qadmni.MainActivity;
import com.qadmni.R;
import com.qadmni.adapters.MyCartAdapter;
import com.qadmni.data.MyCartDTO;

import java.util.ArrayList;

public class UserMyCartActivity extends BaseActivity {
    ArrayList<MyCartDTO> myCartDTOs = null;
    MyCartAdapter myCartAdapter;
    ListView listView;
    LinearLayout mAddMoreLayout, bottomLayout;
    TextView editCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_my_cart);
        listView = (ListView) findViewById(R.id.myCartList);
        mAddMoreLayout = (LinearLayout) findViewById(R.id.AddMoreLayout);
        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        editCart = (TextView) findViewById(R.id.editCart);
        myCartDTOs = qadmniHelper.getCartDetails();
        myCartAdapter = new MyCartAdapter(myCartDTOs, getApplicationContext());
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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlaceOrderActivity.class);
                startActivity(intent);
            }
        });
    }
}
