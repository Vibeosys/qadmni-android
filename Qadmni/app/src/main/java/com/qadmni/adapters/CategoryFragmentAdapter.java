package com.qadmni.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.qadmni.data.CategoryMasterDTO;
import com.qadmni.data.responseDataDTO.CategoryListResponseDTO;
import com.qadmni.fragments.ItemListFragment;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by shrinivas on 13-01-2017.
 */
public class CategoryFragmentAdapter extends FragmentStatePagerAdapter {
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
        return this.itemListFragments.get(position).getCategoryMasterDTO().getCategory();
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
