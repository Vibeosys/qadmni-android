package com.qadmni.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by shrinivas on 14-01-2017.
 */
public class ItemListChildElements extends BaseAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    private class ViewHolder {
    TextView mCompanyName,mProductName,mProductReviews,mProductDesc,mProducPrice;
    }
}
