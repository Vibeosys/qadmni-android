package com.qadmni.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qadmni.fragments.LiveOrdersFragment;
import com.qadmni.fragments.PastOrdersFragment;

/**
 * Created by shrinivas on 20-01-2017.
 */
public class OrderHistoryAdapter extends FragmentPagerAdapter {
    private int itemCount;

    public OrderHistoryAdapter(FragmentManager fm, int count) {
        super(fm);
        this.itemCount = count;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LiveOrdersFragment liveOrdersFragment = new LiveOrdersFragment();
                return liveOrdersFragment;
            case 1:
                PastOrdersFragment pastOrdersFragment = new PastOrdersFragment();
                return pastOrdersFragment;

            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return itemCount;
    }
}
