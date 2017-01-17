package com.qadmni.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.qadmni.R;
import com.qadmni.data.responseDataDTO.ItemInfoList;
import com.qadmni.utils.CustomVolleyRequestQueue;

import java.util.ArrayList;

/**
 * Created by shrinivas on 16-01-2017.
 */
public class ItemListAdapter extends BaseAdapter {
    ArrayList<ItemInfoList> itemInfoLists;
    Context context;
    private ImageLoader mImageLoader;

    public ItemListAdapter(ArrayList<ItemInfoList> itemInfoLists, Context context) {
        this.itemInfoLists = itemInfoLists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemInfoLists.size();
    }

    @Override
    public Object getItem(int i) {
        return itemInfoLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder viewHolder = null;
        mImageLoader = CustomVolleyRequestQueue.getInstance(context)
                .getImageLoader();
        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.item_list_child_element, null);
            viewHolder = new ViewHolder();
            viewHolder.itemName = (TextView) row.findViewById(R.id.productName);
            viewHolder.itemReviews = (TextView) row.findViewById(R.id.product_review);
            viewHolder.ProducerName = (TextView) row.findViewById(R.id.vendor_name);
            viewHolder.itemDescription = (TextView) row.findViewById(R.id.product_desc);
            viewHolder.imgProduct = (NetworkImageView) row.findViewById(R.id.imgProduct);

            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.imgProduct.setImageUrl(null, mImageLoader);
        }
        ItemInfoList itemInfoListDTO = itemInfoLists.get(i);
        String temp = itemInfoListDTO.getItemName();
        viewHolder.itemName.setText(itemInfoListDTO.getItemName());
        viewHolder.itemDescription.setText(itemInfoListDTO.getItemDesc());
        try {
            String url = itemInfoListDTO.getImageUrl();
            if (url != null && !url.isEmpty()) {
                try {
                    mImageLoader.get(url, ImageLoader.getImageListener(viewHolder.imgProduct,
                            R.drawable.default_img, R.drawable.default_img));
                    viewHolder.imgProduct.setImageUrl(url, mImageLoader);
                } catch (Exception e) {
                    viewHolder.imgProduct.setImageResource(R.drawable.default_img);
                }
            } else {
                viewHolder.imgProduct.setImageResource(R.drawable.default_img);
            }
        } catch (NullPointerException e) {
            viewHolder.imgProduct.setDefaultImageResId(R.drawable.default_img);
        }
        return row;
    }

    class ViewHolder {
        TextView itemName, itemReviews, ProducerName, itemDescription, itemDistances, itemTime, itemSar;
        protected NetworkImageView imgProduct;
    }
}
