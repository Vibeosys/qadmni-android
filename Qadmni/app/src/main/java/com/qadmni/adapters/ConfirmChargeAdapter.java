package com.qadmni.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qadmni.R;
import com.qadmni.data.responseDataDTO.OrderChargesResDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 20-01-2017.
 */
public class ConfirmChargeAdapter extends RecyclerView.Adapter<ConfirmChargeAdapter.ConfirmChargeHolder> {

    private Context mContext;
    private ArrayList<OrderChargesResDTO> mData;

    public ConfirmChargeAdapter(Context mContext, ArrayList<OrderChargesResDTO> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ConfirmChargeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.row_order_charges, parent, false);
        return new ConfirmChargeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ConfirmChargeHolder holder, int position) {
        OrderChargesResDTO orderItemResDTO = mData.get(position);
        holder.txtDetails.setText(orderItemResDTO.getChargeDetails());
        double amount = orderItemResDTO.getAmount();
        if (amount == 0) {
            holder.txtCharges.setText(mContext.getString(R.string.str_free));
        } else {
            holder.txtCharges.setText(String.format("%.2f", amount));
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ConfirmChargeHolder extends RecyclerView.ViewHolder {
        protected TextView txtDetails, txtCharges;

        public ConfirmChargeHolder(View itemView) {
            super(itemView);

            txtDetails = (TextView) itemView.findViewById(R.id.txt_details);
            txtCharges = (TextView) itemView.findViewById(R.id.txt_charges);
        }
    }
}
