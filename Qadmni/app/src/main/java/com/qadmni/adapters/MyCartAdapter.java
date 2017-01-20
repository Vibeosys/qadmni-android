package com.qadmni.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qadmni.R;
import com.qadmni.data.MyCartDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 20-01-2017.
 */
public class MyCartAdapter extends BaseAdapter {
    ArrayList<MyCartDTO> myCartDTOs;
    Context context;

    public MyCartAdapter(ArrayList<MyCartDTO> myCartDTOs, Context context) {
        this.myCartDTOs = myCartDTOs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return myCartDTOs.size();
    }

    @Override
    public Object getItem(int i) {
        return myCartDTOs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder viewHolder = null;
        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.my_cart_child_element, null);
            viewHolder = new ViewHolder();
            viewHolder.itemName = (TextView) row.findViewById(R.id.itemName);
            viewHolder.itemQuantity = (TextView) row.findViewById(R.id.quantity);
            viewHolder.itemSAR = (TextView) row.findViewById(R.id.idPrice);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final MyCartDTO myCartDTO = myCartDTOs.get(i);
        viewHolder.itemName.setText("" + myCartDTO.getItemName());
        viewHolder.itemQuantity.setText("" + myCartDTO.getItemQuantity() + " " + context.getResources().getString(R.string.cart_one_qty));
        viewHolder.itemSAR.setText("" + myCartDTO.getUnitPrice());
        return row;
    }

    class ViewHolder {
        protected TextView itemName, itemQuantity, itemSAR;
    }
}
