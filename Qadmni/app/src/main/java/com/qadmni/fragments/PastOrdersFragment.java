package com.qadmni.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.qadmni.R;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

/**
 * Created by shrinivas on 20-01-2017.
 */
public class PastOrdersFragment extends BaseFragment implements ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {
    ListView mListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_past_orders, container, false);
        mListView = (ListView) view.findViewById(R.id.pastOrders);
        callToWebService();
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        return view;

    }

    private void callToWebService() {
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
//Change Url Here
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

    }
}
