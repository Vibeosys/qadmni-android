package com.qadmni.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qadmni.R;

/**
 * Created by shrinivas on 13-01-2017.
 */
public class ItemListFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    PagerTabStrip pagerTabStrip;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_item_list_collection, container, false);
        /*Bundle args = getArguments();
        textView = ((TextView) rootView.findViewById(android.R.id.text1));
        int i = args.getInt(ARG_OBJECT);
        if (args != null) {
            textView.setText("" + i);
        }*/

        // pagerTabStrip = (PagerTabStrip)rootView.findViewById(R.id.pager_title_strip);
        return rootView;
    }
}
