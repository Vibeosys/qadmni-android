package com.qadmni.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.qadmni.R;
import com.qadmni.data.MyCartDTO;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by shrinivas on 20-01-2017.
 */
public class MyCartAdapter extends BaseAdapter {
    ArrayList<MyCartDTO> myCartDTOs;
    Context context;
    boolean aBoolean;
    OnQuantityChangeListener onQuantityChangeListener;

    public MyCartAdapter(ArrayList<MyCartDTO> myCartDTOs, Context context) {
        this.myCartDTOs = myCartDTOs;
        this.context = context;
        this.aBoolean = false;

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
    public View getView(final int position, View view, ViewGroup viewGroup) {
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
            viewHolder.mSetSelectedValue = (Spinner) row.findViewById(R.id.spinnerId);
            viewHolder.mDeleteBtn = (Button) row.findViewById(R.id.deletebtn);
            viewHolder.mSaveBtn = (Button) row.findViewById(R.id.savebtn);

            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final MyCartDTO myCartDTO = myCartDTOs.get(position);
        viewHolder.itemName.setText("" + myCartDTO.getItemName());
        viewHolder.itemQuantity.setText("" + myCartDTO.getItemQuantity() + " " + context.getResources().getString(R.string.cart_one_qty));
        viewHolder.itemSAR.setText(String.format("%.2f", myCartDTO.getUnitPrice()));
        if (aBoolean) {
            viewHolder.mSetSelectedValue.setVisibility(View.VISIBLE);
            final MyCartSpinnerItem myCartSpinnerItem = new MyCartSpinnerItem(context);
            viewHolder.mSetSelectedValue.setAdapter(myCartSpinnerItem);
            viewHolder.mSetSelectedValue.setSelection(myCartDTO.getItemQuantity());
            viewHolder.mSetSelectedValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int selectedVal = (int) myCartSpinnerItem.getItem(i);
                    myCartDTO.setItemQuantity(selectedVal);
                   /* if (onQuantityChangeListener != null) {
                        onQuantityChangeListener.onQuantityChange(myCartDTO, position);
                    }*/
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            viewHolder.mSaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //myCartDTO.setItemQuantity(selectedVal);
                    if (onQuantityChangeListener != null) {
                        onQuantityChangeListener.onQuantityChange(myCartDTO, position);
                    }
                }
            });
            viewHolder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onQuantityChangeListener != null) {
                        onQuantityChangeListener.onDeleteClick(myCartDTO, position);
                        myCartDTOs.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });
            viewHolder.itemQuantity.setVisibility(View.GONE);
            viewHolder.mDeleteBtn.setVisibility(View.VISIBLE);
            viewHolder.mSaveBtn.setVisibility(View.VISIBLE);

        } else {
            viewHolder.mSetSelectedValue.setVisibility(View.GONE);
            viewHolder.mDeleteBtn.setVisibility(View.GONE);
            viewHolder.mSaveBtn.setVisibility(View.GONE);
            viewHolder.itemQuantity.setVisibility(View.VISIBLE);

        }

        return row;
    }

    class ViewHolder {
        protected TextView itemName, itemQuantity, itemSAR;
        protected Spinner mSetSelectedValue;
        protected Button mDeleteBtn, mSaveBtn;
    }

    public void setVisiblity(boolean visiblity) {
        this.aBoolean = visiblity;
    }

    public void setOnQuantityChangeListener(OnQuantityChangeListener onQuantityChangeListener) {
        this.onQuantityChangeListener = onQuantityChangeListener;
    }

    public interface OnQuantityChangeListener {
        public void onQuantityChange(MyCartDTO myCartDTO, int position);

        public void onDeleteClick(MyCartDTO myCartDTO, int position);
    }
}
