package com.qadmni.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qadmni.R;
import com.qadmni.adapters.OrderHistoryAdapter;
import com.qadmni.utils.UserAuth;

public class UserOrderHistoryActivity extends BaseActivity {
    private static final int CALL_TO_LOGIN = 44;
    TabLayout trackOrder_Tab_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_history);
        if (!UserAuth.isUserLoggedIn()) {
            startActivityForResult(new Intent(getApplicationContext(), CustomerLoginActivity.class), CALL_TO_LOGIN);
        }
        trackOrder_Tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        trackOrder_Tab_layout.addTab(trackOrder_Tab_layout.newTab().setText(getResources().getString(R.string.str_live_track)));

        trackOrder_Tab_layout.addTab(trackOrder_Tab_layout.newTab().setText(getResources().getString(R.string.str_past_orders)));

        trackOrder_Tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
        trackOrder_Tab_layout.setSelectedTabIndicatorHeight(4);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(1);
        final OrderHistoryAdapter orderHistoryAdapter =
                new OrderHistoryAdapter(getSupportFragmentManager(), trackOrder_Tab_layout.getTabCount());
        viewPager.setAdapter(orderHistoryAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(trackOrder_Tab_layout));
        trackOrder_Tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CALL_TO_LOGIN) {
            if (requestCode == Activity.RESULT_OK) {

            }
        }
    }
}
