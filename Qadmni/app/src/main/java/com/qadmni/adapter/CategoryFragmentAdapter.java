package com.qadmni.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.qadmni.data.responseDataDTO.CategoryListResponseDTO;
import com.qadmni.fragments.ItemListFragment;

import java.util.ArrayList;

/**
 * Created by shrinivas on 13-01-2017.
 */
public class CategoryFragmentAdapter extends FragmentStatePagerAdapter {
    ArrayList<CategoryListResponseDTO> categoryListResponseDTOs = new ArrayList<>();

    public CategoryFragmentAdapter(FragmentManager fm, ArrayList<CategoryListResponseDTO> categoryListResponseDTOs) {
        super(fm);
        this.categoryListResponseDTOs = categoryListResponseDTOs;
        Log.d("TAG", "TAG");
        Log.d("TAG", "TAG");
        Log.d("TAG", "TAG");
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
        return categoryListResponseDTOs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // return "OBJECT " + (position + 1);
        CategoryListResponseDTO categoryListResponseDTO = categoryListResponseDTOs.get(position);
        return " " + categoryListResponseDTO.getCategory();
    }
}
