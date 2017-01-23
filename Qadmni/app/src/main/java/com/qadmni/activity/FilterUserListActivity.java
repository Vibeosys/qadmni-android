package com.qadmni.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qadmni.MainActivity;
import com.qadmni.R;
import com.qadmni.fragments.SortFragment;

public class FilterUserListActivity extends BaseActivity implements View.OnClickListener, SortFragment.OnItemSortSelected {

    public static final String SORT_BY = "sortBy";
    public static final String SELECTED_DISTANCE = "distance";
    public static final String SELECTED_PRICE = "price";

    private int sortById = 0;
    private TextView btnApply, btnClear;
    private int mSeekBarStep = 1, mSeekBarMax, mSeekBarMin;
    private int mSeekBarPriceStep = 1, mSeekBarPriceMax = 10000, mSeekBarPriceMin = 0;
    private SeekBar mSeekBar;
    private SeekBar mSeekBarPrice;
    private int selectedRadius = 0;
    private int selectedPrice = 0;
    private TextView mFilterVal, filterPriceVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_user_list);
        setTitle(getString(R.string.str_filter_activity_title));

        sortById = mSessionManager.getListSortBy();
        selectedPrice = mSessionManager.getSelectedPrice();
        selectedRadius = mSessionManager.getSelectedDistance();

        SortFragment sortFragment = new SortFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SortFragment.SORT_BY, sortById);
        sortFragment.setArguments(bundle);
        sortFragment.setOnItemSortSelected(this);
        getSupportFragmentManager().beginTransaction().
                add(R.id.sortList, sortFragment, "SortList").commit();

        btnApply = (TextView) findViewById(R.id.btn_apply);
        btnClear = (TextView) findViewById(R.id.txt_clear);
        btnApply.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        mSeekBarMax = 100;
        mSeekBarMin = 0;
        mSeekBar = (SeekBar) findViewById(R.id.skb_radius);
        mSeekBarPrice = (SeekBar) findViewById(R.id.skb_price);
        mFilterVal = (TextView) findViewById(R.id.filterVal);
        filterPriceVal = (TextView) findViewById(R.id.filterPriceVal);

        mSeekBar.setVerticalScrollbarPosition(selectedRadius);

        mSeekBar.setMax((mSeekBarMax - mSeekBarMin) / mSeekBarStep);
        mFilterVal.setText("0 " + getResources().getString(R.string.raduis_selected));
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedRadius = mSeekBarMin + (progress * mSeekBarStep);
                mFilterVal.setText("" + selectedRadius + " " + getResources().getString(R.string.raduis_selected));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarPrice.setVerticalScrollbarPosition(selectedPrice);
        mSeekBarPrice.setMax((mSeekBarPriceMax - mSeekBarPriceMin) / mSeekBarPriceStep);
        mSeekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedPrice = mSeekBarPriceMin + (progress * mSeekBarPriceStep);
                filterPriceVal.setText(getResources().getString(R.string.sar) + " " + selectedPrice);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_apply:
                Intent intent = new Intent();
                intent.putExtra(SORT_BY, sortById);
                intent.putExtra(SELECTED_DISTANCE, selectedRadius);
                intent.putExtra(SELECTED_PRICE, selectedPrice);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.txt_clear:
                Intent intent1 = new Intent();
                intent1.putExtra(SORT_BY, 0);
                intent1.putExtra(SELECTED_DISTANCE, 0);
                intent1.putExtra(SELECTED_PRICE, 0);
                setResult(Activity.RESULT_OK, intent1);
                finish();
                break;
        }

    }

    @Override
    public void onSortSelect(int id) {
        sortById = id;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();//finishing activity
        //super.onBackPressed();

    }
}
