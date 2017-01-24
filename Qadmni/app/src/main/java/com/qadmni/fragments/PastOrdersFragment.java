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
import com.qadmni.adapters.PastOrderAdapter;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.responseDataDTO.PastOrderResponseDTO;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

import java.util.ArrayList;

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
        progressDialog.show();
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_PAST_ORDERS,
                mSessionManager.getPastOrderUrl(), baseRequestDTO);

    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_PAST_ORDERS:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }

    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_PAST_ORDERS:
                customAlterDialog(getString(R.string.str_past_order_error), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_PAST_ORDERS:
                ArrayList<PastOrderResponseDTO> pastOrderResponseDTOs = PastOrderResponseDTO.deserialize(data);
                PastOrderAdapter pastOrderAdapter = new PastOrderAdapter(pastOrderResponseDTOs, getActivity());
                mListView.setAdapter(pastOrderAdapter);
                break;
        }


    }
}
