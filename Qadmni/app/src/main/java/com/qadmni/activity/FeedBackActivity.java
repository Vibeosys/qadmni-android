package com.qadmni.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qadmni.R;
import com.qadmni.adapters.FeedBackAdapter;
import com.qadmni.data.FeedbackItemDTO;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.CustomerLoginReqDTO;
import com.qadmni.data.requestDataDTO.FeedbackItemSubReqDTO;
import com.qadmni.data.requestDataDTO.ReviewItemsReqDTO;
import com.qadmni.data.requestDataDTO.SubmitFeedReqDTO;
import com.qadmni.data.responseDataDTO.ReviewItemsResDTO;
import com.qadmni.utils.NetworkUtils;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

import java.util.ArrayList;

public class FeedBackActivity extends BaseActivity implements ServerSyncManager.OnErrorResultReceived,
        ServerSyncManager.OnSuccessResultReceived, View.OnClickListener {
    private long orderId;
    private FeedBackAdapter adapter;
    private RecyclerView recyclerView;
    private Button btnSubmit;
    private ArrayList<FeedbackItemDTO> feedbackItemDTOs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        setTitle(getString(R.string.str_provide_feedback));
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        recyclerView = (RecyclerView) findViewById(R.id.feedback_list);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            orderId = bundle.getLong("orderId");
        }

        btnSubmit.setOnClickListener(this);
        if (!NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
            createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                    getResources().getString(R.string.str_err_net_msg));
        } else {
            progressDialog.show();
            ReviewItemsReqDTO reviewItemsReqDTO = new ReviewItemsReqDTO(orderId);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(reviewItemsReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_REVIEW_ITEMS,
                    mSessionManager.getReviewItems(), baseRequestDTO);
        }

    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_REVIEW_ITEMS:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
            case ServerRequestConstants.SUBMIT_FEEDBACK:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_REVIEW_ITEMS:
                customAlterDialog(getString(R.string.str_server_err_title), errorMessage);
                break;
            case ServerRequestConstants.SUBMIT_FEEDBACK:
                customAlterDialog(getString(R.string.str_server_err_title), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_REVIEW_ITEMS:
                feedbackItemDTOs = ReviewItemsResDTO.convertToFeedbackItemDTO(data);
                adapter = new FeedBackAdapter(getApplicationContext(), feedbackItemDTOs);
                recyclerView.setAdapter(adapter);
                break;
            case ServerRequestConstants.SUBMIT_FEEDBACK:
                Toast.makeText(getApplicationContext(), getString(R.string.str_feedback_success), Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), UserOrderHistoryActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_submit:
                if (feedbackItemDTOs != null) {
                    submitFeedBack();
                }
                break;
        }
    }

    private void submitFeedBack() {
        ArrayList<FeedbackItemSubReqDTO> feedbackItemSubReqDTOs = new ArrayList<>();
        for (FeedbackItemDTO feedbackItem : adapter.getData()) {
            feedbackItemSubReqDTOs.add(new FeedbackItemSubReqDTO(feedbackItem.getItemId(), feedbackItem.getRating()));
        }

        SubmitFeedReqDTO submitFeedReqDTO = new SubmitFeedReqDTO(orderId, feedbackItemSubReqDTOs);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(submitFeedReqDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.SUBMIT_FEEDBACK,
                mSessionManager.submitReviewItems(), baseRequestDTO);
    }
}
