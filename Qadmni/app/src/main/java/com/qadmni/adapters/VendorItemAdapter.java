package com.qadmni.adapters;

/**
 * Created by akshay on 16-01-2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.qadmni.R;
import com.qadmni.data.responseDataDTO.VendorItemResDTO;
import com.qadmni.utils.CustomVolleyRequestQueue;

import java.util.ArrayList;

public class VendorItemAdapter extends RecyclerView.Adapter<VendorItemAdapter.ItemViewHolder> {

    private Context mContext;
    private ArrayList<VendorItemResDTO> mData;
    private ImageLoader mImageLoader;

    public VendorItemAdapter(Context mContext, ArrayList<VendorItemResDTO> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.row_vendor_list, parent, false);
        mImageLoader = CustomVolleyRequestQueue.getInstance(mContext)
                .getImageLoader();

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.imgProduct.setImageUrl(null, mImageLoader);
        final VendorItemResDTO item = mData.get(position);
        holder.txtItemName.setText(item.getItemName());
        holder.txtDetails.setText(item.getItemDesc());
        holder.txtAmount.setText(String.format("%.0f", item.getPrice()));
        holder.txtCategory.setText(item.getCategory());
        int isAvail = item.getAvailableForSell();
        if (isAvail == 1) {
            holder.txtAvail.setText(mContext.getString(R.string.str_avail_soon));
        } else {
            holder.txtAvail.setText(mContext.getString(R.string.str_not_avail));
        }
        try {
            String url = item.getImageUrl();
            if (url != null && !url.isEmpty()) {
                try {
                    mImageLoader.get(url, ImageLoader.getImageListener(holder.imgProduct,
                            R.drawable.default_img, R.drawable.default_img));
                    holder.imgProduct.setImageUrl(url, mImageLoader);
                } catch (Exception e) {
                    holder.imgProduct.setImageResource(R.drawable.default_img);
                }
            } else {
                holder.imgProduct.setImageResource(R.drawable.default_img);
            }
        } catch (NullPointerException e) {
            holder.imgProduct.setDefaultImageResId(R.drawable.default_img);
        }
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtItemName, txtDetails, txtAmount, txtAvail, txtCategory;
        protected NetworkImageView imgProduct;
        //protected Button btnUpdate;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imgProduct = (NetworkImageView) itemView.findViewById(R.id.imgProduct);
            txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
            txtDetails = (TextView) itemView.findViewById(R.id.txtDetails);
            txtAmount = (TextView) itemView.findViewById(R.id.txtAmount);
            txtAvail = (TextView) itemView.findViewById(R.id.txtAvail);
            txtCategory = (TextView) itemView.findViewById(R.id.txtCategory);
            //btnUpdate = (Button) itemView.findViewById(R.id.btn_update);
        }
    }
}
