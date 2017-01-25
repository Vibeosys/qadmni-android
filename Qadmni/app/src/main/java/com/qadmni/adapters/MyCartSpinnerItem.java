package com.qadmni.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qadmni.R;

import java.util.ArrayList;

/**
 * Created by shrinivas on 25-01-2017.
 */
public class MyCartSpinnerItem extends BaseAdapter {
    private Context mContext;
    private ArrayList<Integer> arrayList = new ArrayList<>();

    MyCartSpinnerItem(Context mContext) {
        this.mContext = mContext;
        for (int i = 0; i <=10; i++) {
            arrayList.add(i,i);

        }
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
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

            LayoutInflater theLayoutInflater = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflater.inflate(R.layout.spinner_child_element, null);
            viewHolder = new ViewHolder();
            viewHolder.spinnerChild = (TextView) row.findViewById(R.id.txtSpinnerChild);
            row.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        arrayList.indexOf(i);
        viewHolder.spinnerChild.setText("" + arrayList.indexOf(i));
        return row;
    }

    private class ViewHolder {
        TextView spinnerChild;
    }

}
