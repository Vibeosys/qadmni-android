package com.qadmni.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qadmni.R;
import com.qadmni.adapters.CategoryFragmentAdapter;
import com.qadmni.adapters.ItemListAdapter;
import com.qadmni.data.CategoryMasterDTO;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.GetItemListDTO;
import com.qadmni.data.responseDataDTO.CategoryListResponseDTO;
import com.qadmni.data.responseDataDTO.ItemInfoList;
import com.qadmni.data.responseDataDTO.ItemListResponseDTO;
import com.qadmni.data.responseDataDTO.ProducerLocations;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

import java.util.ArrayList;

/**
 * Created by shrinivas on 13-01-2017.
 */
public class ItemListFragment extends BaseFragment implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived {
    public static final String ARG_OBJECT = "objectPar";
    private CategoryMasterDTO categoryMasterDTO;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_item_list_collection, container, false);
        mListView = (ListView) rootView.findViewById(R.id.item_list);
        Bundle args = getArguments();
        if (args != null) {
            categoryMasterDTO = args.getParcelable(ARG_OBJECT);

        }
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        callToWebService();

    }

    private void callToWebService() {
        String categoryIdStr = "" + categoryMasterDTO.getCategoryId();
        GetItemListDTO getItemListDTO = new GetItemListDTO(categoryIdStr);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(getItemListDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_GET_ITEM_LIST,
                mSessionManager.getItemListUrl(), baseRequestDTO);

    }


    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        // customAlterDialog(getResources().getString(R.string.str_err_server_err), error.getMessage());
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        //   customAlterDialog(getResources().getString(R.string.str_err_server_err), errorMessage);
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_GET_ITEM_LIST:
                ItemListResponseDTO itemListResponseDTO = ItemListResponseDTO.deserializeJson(data);
                ArrayList<ItemInfoList> itemInfoLists = ItemInfoList.deSerializedToJson(itemListResponseDTO.getItemInfoLists());
                ArrayList<ProducerLocations> producerLocationses = ProducerLocations.deSerializedToJson(itemListResponseDTO.getProducerLocations());

                ItemListAdapter itemListAdapter = new ItemListAdapter(itemInfoLists, getContext());
                mListView.setAdapter(itemListAdapter);
                break;
        }

    }


}
