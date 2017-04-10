package com.qadmni.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.google.firebase.crash.FirebaseCrash;
import com.qadmni.data.CategoryMasterDTO;
import com.qadmni.data.responseDataDTO.CategoryListResponseDTO;
import com.qadmni.fragments.ItemListFragment;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by shrinivas on 13-01-2017.
 */
public class CategoryFragmentAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = CategoryFragmentAdapter.class.getSimpleName();
    ArrayList<ItemListFragment> itemListFragments;

    public CategoryFragmentAdapter(FragmentManager fm, ArrayList<ItemListFragment> itemListFragments) {
        super(fm);
        this.itemListFragments = itemListFragments;
    }

    @Override
    public Fragment getItem(int i) {
        return itemListFragments.get(i);
    }

    @Override
    public int getCount() {
        return itemListFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String name = "";
        try {
            name = this.itemListFragments.get(position).getCategoryMasterDTO().getCategory();
        } catch (NullPointerException e) {
            name = "";
            Log.e(TAG, "## error in get page title");
            FirebaseCrash.log("Error in " + TAG + " catch block " + e.getMessage());
        }
        return name;
    }

    /*@Override
    public int getItemPosition(Object object) {
        int index = itemListFragments.indexOf(object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }*/

}
