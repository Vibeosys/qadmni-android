package com.qadmni.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qadmni.R;

/**
 * Created by shrinivas on 20-01-2017.
 */
public class LiveOrdersFragment extends BaseFragment{

    TextView mTrackOrders, mFeedBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_live_orders, container, false);
       /* mTrackOrders = (TextView) view.findViewById(R.id.trackOrders);
        mFeedBack = (TextView) view.findViewById(R.id.provideFeedback);
        mTrackOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DisplayTrackOrdersActivity.class);
                startActivity(intent);
            }
        });
        mFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFeedBackDialog(savedInstanceState);
            }
        });*/
        return view;

    }

    /*public void OpenFeedBackDialog(Bundle savedInstanceState) {
        final Dialog dlg = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_MinWidth);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //View view = getLayoutInflater(savedInstanceState).inflate(R.layout.feedback_dialog_layout, null);
        dlg.setContentView(R.layout.feedback_dialog_layout);

        dlg.getWindow().setBackgroundDrawableResource(R.color.dialog_color);
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dlg.show();
    }*/
}
