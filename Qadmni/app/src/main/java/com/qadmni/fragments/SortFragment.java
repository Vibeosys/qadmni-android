package com.qadmni.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qadmni.R;
import com.qadmni.utils.SortByList;

/**
 * Created by akshay on 21-01-2017.
 */
public class SortFragment extends BaseFragment implements View.OnClickListener {
    public static final String SORT_BY = "sortBy";
    private LinearLayout nearLay, priceLay, reviewLay;
    private ImageView imgDistance, imgPrice, imgStar;
    private TextView txtDistance, txtPrice, txtStar;
    private OnItemSortSelected onItemSortSelected;
    private int sortBy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.sort_menu_list, container, false);

        nearLay = (LinearLayout) view.findViewById(R.id.nearLay);
        priceLay = (LinearLayout) view.findViewById(R.id.priceLay);
        reviewLay = (LinearLayout) view.findViewById(R.id.reviewLay);

        imgDistance = (ImageView) view.findViewById(R.id.imgDistance);
        imgPrice = (ImageView) view.findViewById(R.id.imgPrice);
        imgStar = (ImageView) view.findViewById(R.id.imgStar);

        txtDistance = (TextView) view.findViewById(R.id.txtDistance);
        txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        txtStar = (TextView) view.findViewById(R.id.txtReview);

        nearLay.setOnClickListener(this);
        priceLay.setOnClickListener(this);
        reviewLay.setOnClickListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            sortBy = bundle.getInt(SORT_BY);
            setupDisplay(sortBy);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.nearLay:
                distanceSelected();
                if (onItemSortSelected != null)
                    onItemSortSelected.onSortSelect(SortByList.DISTANCE);
                break;

            case R.id.priceLay:
                priceSelected();
                if (onItemSortSelected != null)
                    onItemSortSelected.onSortSelect(SortByList.PRICE);
                break;

            case R.id.reviewLay:
                reviewSelected();
                if (onItemSortSelected != null)
                    onItemSortSelected.onSortSelect(SortByList.REVIEW);
                break;
        }
    }

    public void setupDisplay(int sortBy) {
        if (sortBy == 0) {
            nothingSelected();
        } else if (sortBy == SortByList.DISTANCE) {
            distanceSelected();
        } else if (sortBy == SortByList.PRICE) {
            priceSelected();
        } else if (sortBy == SortByList.REVIEW) {
            reviewSelected();
        }
    }

    private void reviewSelected() {
        txtDistance.setTextColor(getResources().getColor(R.color.primaryText2, null));
        imgDistance.setImageDrawable(getResources().getDrawable(R.drawable.ic_marker_24_black, null));
        txtPrice.setTextColor(getResources().getColor(R.color.primaryText2, null));
        imgPrice.setImageDrawable(getResources().getDrawable(R.drawable.ic_coins_24_black, null));
        txtStar.setTextColor(getResources().getColor(R.color.colorAccent, null));
        imgStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_24_accent, null));
    }

    private void priceSelected() {
        txtDistance.setTextColor(getResources().getColor(R.color.primaryText2, null));
        imgDistance.setImageDrawable(getResources().getDrawable(R.drawable.ic_marker_24_black, null));
        txtPrice.setTextColor(getResources().getColor(R.color.colorAccent, null));
        imgPrice.setImageDrawable(getResources().getDrawable(R.drawable.ic_coins_24_accent, null));
        txtStar.setTextColor(getResources().getColor(R.color.primaryText2, null));
        imgStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_24_black, null));
    }

    private void distanceSelected() {
        txtDistance.setTextColor(getResources().getColor(R.color.colorAccent, null));
        imgDistance.setImageDrawable(getResources().getDrawable(R.drawable.ic_marker_24_accent, null));
        txtPrice.setTextColor(getResources().getColor(R.color.primaryText2, null));
        imgPrice.setImageDrawable(getResources().getDrawable(R.drawable.ic_coins_24_black, null));
        txtStar.setTextColor(getResources().getColor(R.color.primaryText2, null));
        imgStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_24_black, null));
    }

    private void nothingSelected() {
        txtDistance.setTextColor(getResources().getColor(R.color.primaryText2, null));
        imgDistance.setImageDrawable(getResources().getDrawable(R.drawable.ic_marker_24_black, null));
        txtPrice.setTextColor(getResources().getColor(R.color.primaryText2, null));
        imgPrice.setImageDrawable(getResources().getDrawable(R.drawable.ic_coins_24_black, null));
        txtStar.setTextColor(getResources().getColor(R.color.primaryText2, null));
        imgStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_24_black, null));
    }

    public interface OnItemSortSelected {
        void onSortSelect(int id);
    }

    public void setOnItemSortSelected(OnItemSortSelected onItemSortSelected) {
        this.onItemSortSelected = onItemSortSelected;
    }
}
