package com.qadmni.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qadmni.fragments.ItemListFragment;

/**
 * Created by shrinivas on 13-01-2017.
 */
public class CategoryFragmentAdapter extends FragmentStatePagerAdapter {
    public CategoryFragmentAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new ItemListFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(ItemListFragment.ARG_OBJECT, i + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}
