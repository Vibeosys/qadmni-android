package com.qadmni.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qadmni.R;
import com.qadmni.adapters.VendorItemAdapter;
import com.qadmni.adapters.VendorOrderListAdapter;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.UpdateDeliveryStatus;
import com.qadmni.data.responseDataDTO.UpdatableStatusCodesDTO;
import com.qadmni.data.responseDataDTO.VendorOrderDTO;
import com.qadmni.utils.NetworkUtils;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

import java.util.ArrayList;

public class VendorOrderListActivity extends BaseActivity implements ServerSyncManager.OnErrorResultReceived,
        ServerSyncManager.OnSuccessResultReceived, VendorOrderListAdapter.UpdateButton {

    private RecyclerView reItemList;
    private VendorOrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_order_list);
        reItemList = (RecyclerView) findViewById(R.id.vendorList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reItemList.setLayoutManager(layoutManager);
        if (!NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
            createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                    getResources().getString(R.string.str_err_net_msg));
        } else {
            callToWebservice();
        }
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);

    }

    public void callToWebservice() {
        progressDialog.show();
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_VENDOR_ORDER,
                mSessionManager.getVendorOrder(), baseRequestDTO);
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_VENDOR_ORDER:
                ArrayList<VendorOrderDTO> vendorOrderDTOs = VendorOrderDTO.deSerializeToArray(data);
                adapter = new VendorOrderListAdapter(VendorOrderListActivity.this, vendorOrderDTOs);
                reItemList.setAdapter(adapter);
                adapter.setOrderStatus(this);
                break;
            case ServerRequestConstants.REQUEST_UPDATE_DELIVERY_STATUS:
                customAlterDialog(getResources().getString(R.string.app_name), getResources().getString(R.string.str_product_status));
                callToWebservice();
                break;
        }
    }

    @Override
    public void SendOrderStatus(long id, int status) {
        progressDialog.dismiss();
        UpdateDeliveryStatus updateDeliveryStatus = new UpdateDeliveryStatus(id, status);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(updateDeliveryStatus);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_UPDATE_DELIVERY_STATUS,
                mSessionManager.getUpdateOrderStatusUrl(), baseRequestDTO);
    }
}
