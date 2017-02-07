package com.qadmni.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.qadmni.R;
import com.qadmni.data.FeedbackItemDTO;
import com.qadmni.data.responseDataDTO.ReviewItemsResDTO;
import com.qadmni.data.responseDataDTO.VendorItemResDTO;
import com.qadmni.utils.CustomVolleyRequestQueue;

import java.util.ArrayList;

/**
 * Created by akshay on 06-02-2017.
 */
public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.ItemViewHolder> {

    private Context mContext;
    private ArrayList<FeedbackItemDTO> mData = new ArrayList<>();


    public FeedBackAdapter(Context mContext, ArrayList<FeedbackItemDTO> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.row_feedback_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final FeedbackItemDTO reviewItemsResDTO = mData.get(position);
        holder.txtItemName.setText(reviewItemsResDTO.getItemName());
        holder.ratingBar.setRating(reviewItemsResDTO.getRating());
        holder.ratingBar.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
                reviewItemsResDTO.setRating(rating);
                //notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtItemName;
        protected SimpleRatingBar ratingBar;

        public ItemViewHolder(View itemView) {
            super(itemView);

            txtItemName = (TextView) itemView.findViewById(R.id.txt_item_name);
            ratingBar = (SimpleRatingBar) itemView.findViewById(R.id.product_ratings);
        }
    }

    public ArrayList<FeedbackItemDTO> getData() {
        return mData;
    }

    public void setData(ArrayList<FeedbackItemDTO> mData) {
        this.mData = mData;
    }
}
