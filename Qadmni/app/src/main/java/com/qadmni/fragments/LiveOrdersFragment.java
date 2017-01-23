package com.qadmni.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qadmni.R;
import com.qadmni.adapters.LiveOrdersAdapter;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.GetItemListDTO;
import com.qadmni.data.responseDataDTO.LiveOrdersResponseDTO;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

import java.util.ArrayList;

/**
 * Created by shrinivas on 20-01-2017.
 */
public class LiveOrdersFragment extends BaseFragment implements ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {

    TextView mTrackOrders, mFeedBack;
    ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_live_orders, container, false);
        mListView = (ListView) view.findViewById(R.id.LiveItemList);
        callToWebService();
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        return view;

    }

    private void callToWebService() {

        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();

        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_LIVE_ORDERS,
                mSessionManager.getLiveOrdersUrl(), baseRequestDTO);
        Log.d("error", "error");
        Log.d("error", "error");

    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        Log.d("error", "error");
        Log.d("error", "error");
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        Log.d("error", "error");
        Log.d("error", "error");

    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        Log.d("error", "error");
        Log.d("error", "error");
        ArrayList<LiveOrdersResponseDTO> liveOrdersResponseDTO = LiveOrdersResponseDTO.deserializeJson(data);
        LiveOrdersAdapter liveOrdersAdapter = new LiveOrdersAdapter(liveOrdersResponseDTO, getActivity());
        mListView.setAdapter(liveOrdersAdapter);
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
