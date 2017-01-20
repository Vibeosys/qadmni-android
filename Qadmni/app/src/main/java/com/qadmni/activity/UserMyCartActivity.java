package com.qadmni.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.qadmni.R;
import com.qadmni.adapters.MyCartAdapter;
import com.qadmni.data.MyCartDTO;

import java.util.ArrayList;

public class UserMyCartActivity extends BaseActivity {
    ArrayList<MyCartDTO> myCartDTOs = null;
    MyCartAdapter myCartAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_my_cart);
        listView = (ListView) findViewById(R.id.myCartList);
        myCartDTOs = qadmniHelper.getCartDetails();
        myCartAdapter = new MyCartAdapter(myCartDTOs,getApplicationContext());
        listView.setAdapter(myCartAdapter);

    }
}
