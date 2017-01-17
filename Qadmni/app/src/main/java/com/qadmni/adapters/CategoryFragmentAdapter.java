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
    ArrayList<CategoryListResponseDTO> categoryListResponseDTOs = new ArrayList<>();
    ArrayList<Integer> count = new ArrayList<>();
    CategoryMasterDTO categoryMasterDTO;

    public CategoryFragmentAdapter(FragmentManager fm, ArrayList<CategoryListResponseDTO> categoryListResponseDTOs) {
        super(fm);
        this.categoryListResponseDTOs = categoryListResponseDTOs;
    }

    @Override
    public Fragment getItem(int i) {

            categoryMasterDTO = new CategoryMasterDTO(categoryListResponseDTOs.get(i));
            Fragment fragment = new ItemListFragment();
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            args.putParcelable(ItemListFragment.ARG_OBJECT, categoryMasterDTO);
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

    @Override
    public int getItemPosition(Object object) {
        ItemListFragment fragment = (ItemListFragment) object;
        Bundle args = fragment.getArguments();
        if (args != null) {
            categoryMasterDTO = args.getParcelable(ItemListFragment.ARG_OBJECT);
            int position = categoryListResponseDTOs.indexOf(categoryMasterDTO);
            if (position >= 0) {
                return position;
            } else {
                return POSITION_NONE;
            }
        }

        return POSITION_NONE;
    }

}
