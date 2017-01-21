package com.qadmni.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.qadmni.R;
import com.qadmni.fragments.SortFragment;

public class FilterUserListActivity extends BaseActivity implements View.OnClickListener {


    FrameLayout sortList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_user_list);
        setTitle(getString(R.string.str_filter_activity_title));
        SortFragment sortFragment = new SortFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.sortList, sortFragment, "SortList").commit();

    }

    @Override
    public void onClick(View view) {

    }
}
