package com.qadmni.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.qadmni.R;
import com.qadmni.data.ItemListDetailsDTO;
import com.qadmni.data.responseDataDTO.ItemInfoList;
import com.qadmni.utils.CustomVolleyRequestQueue;

import java.util.ArrayList;

/**
 * Created by shrinivas on 16-01-2017.
 */
public class ItemListAdapter extends BaseAdapter {
    ArrayList<ItemInfoList> itemInfoLists;
    ArrayList<ItemListDetailsDTO> itemListDetailsDTOs;
    Context context;
    private ImageLoader mImageLoader;
    CustomButtonListener customButtonListener;

    /* public ItemListAdapter(ArrayList<ItemInfoList> itemInfoLists, Context context) {
         this.itemInfoLists = itemInfoLists;
         this.context = context;
     }*/
    public ItemListAdapter(ArrayList<ItemListDetailsDTO> itemListDetailsDTOs, Context context) {
        this.itemListDetailsDTOs = itemListDetailsDTOs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemListDetailsDTOs.size();
    }

    @Override
    public Object getItem(int i)
    {
        return itemListDetailsDTOs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
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
            viewHolder.itemDistances = (TextView) row.findViewById(R.id.product_distance);
            viewHolder.itemTime = (TextView) row.findViewById(R.id.prod_time);
            viewHolder.itemSar = (TextView) row.findViewById(R.id.product_price);
            viewHolder.mAdditionBtn = (Button) row.findViewById(R.id.plus_product);
            viewHolder.mSubtractionBtn = (Button) row.findViewById(R.id.minus_product);
            viewHolder.itemQuantity = (TextView) row.findViewById(R.id.no_product_val);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.imgProduct.setImageUrl(null, mImageLoader);
        }
        final ItemListDetailsDTO itemListDetailsDTO = itemListDetailsDTOs.get(i);
        // ItemInfoList itemInfoListDTO = itemInfoLists.get(i);
        String temp = itemListDetailsDTO.getItemName();
        viewHolder.itemName.setText(itemListDetailsDTO.getItemName());
        viewHolder.itemDescription.setText(itemListDetailsDTO.getItemDesc());
        viewHolder.ProducerName.setText(itemListDetailsDTO.getBusinessName());
        viewHolder.itemDistances.setText("" + itemListDetailsDTO.getUserDistance());
        viewHolder.itemTime.setText("" + itemListDetailsDTO.getUserTime());
        viewHolder.itemSar.setText("" + itemListDetailsDTO.getUnitPrice());
        viewHolder.itemReviews.setText("" + itemListDetailsDTO.getReviews() + "\t" + context.getResources().getString(R.string.str_reviews));
        viewHolder.itemQuantity.setText("" + itemListDetailsDTO.getQuantity());
        try {
            String url = itemListDetailsDTO.getImageUrl();
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
        viewHolder.mAdditionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customButtonListener != null)
                    customButtonListener.onButtonClickListener(view.getId(), i, itemListDetailsDTO.getQuantity(), itemListDetailsDTO);
            }
        });
        viewHolder.mSubtractionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customButtonListener != null)
                    customButtonListener.onButtonClickListener(view.getId(), i, itemListDetailsDTO.getQuantity(), itemListDetailsDTO);
            }
        });
        return row;
    }

    class ViewHolder {
        protected TextView itemName, itemReviews, ProducerName, itemDescription, itemDistances,
                itemTime, itemSar, itemQuantity;
        protected NetworkImageView imgProduct;
        protected Button mAdditionBtn, mSubtractionBtn;

    }

    public interface CustomButtonListener {
        public void onButtonClickListener(int id, int position, int value, ItemListDetailsDTO itemListDetailsDTOs);
    }

    public void setCustomButtonListner(CustomButtonListener listener) {
        this.customButtonListener = listener;
    }
}
