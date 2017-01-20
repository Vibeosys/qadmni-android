package com.qadmni.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qadmni.R;
import com.qadmni.data.responseDataDTO.OrderChargesResDTO;
import com.qadmni.data.responseDataDTO.OrderItemResDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 20-01-2017.
 */
public class UserConfirmListAdapter extends RecyclerView.Adapter<UserConfirmListAdapter.ConfirmItemHolder> {

    private Context mContext;
    private ArrayList<OrderItemResDTO> mData;

    public UserConfirmListAdapter(Context mContext, ArrayList<OrderItemResDTO> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ConfirmItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.row_confirm_order_item, parent, false);
        return new ConfirmItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ConfirmItemHolder holder, int position) {
        OrderItemResDTO orderItemResDTO = mData.get(position);
        holder.txtItemQty.setText(String.valueOf(orderItemResDTO.getItemQty()) + " x ");
        holder.txtItemName.setText(orderItemResDTO.getItemName());
        holder.txtTotalPrice.setText(String.format("%.2f", orderItemResDTO.getItemTotalPrice()));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ConfirmItemHolder extends RecyclerView.ViewHolder {
        protected TextView txtItemQty, txtItemName, txtTotalPrice;

        public ConfirmItemHolder(View itemView) {
            super(itemView);

            txtItemQty = (TextView) itemView.findViewById(R.id.txt_item_qty);
            txtItemName = (TextView) itemView.findViewById(R.id.txt_item_name);
            txtTotalPrice = (TextView) itemView.findViewById(R.id.txt_total_price);
        }
    }
}
