package com.qadmni.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
            //nothingSelected();
        } else if (sortBy == SortByList.DISTANCE) {
            distanceSelected();
        } else if (sortBy == SortByList.PRICE) {
            priceSelected();
        } else if (sortBy == SortByList.REVIEW) {
            reviewSelected();
        }
    }

    private void reviewSelected() {
        txtDistance.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryText2));
        imgDistance.setImageDrawable(getContext().getDrawable(R.drawable.ic_marker_24_black));
        txtPrice.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryText2));
        imgPrice.setImageDrawable(getContext().getDrawable(R.drawable.ic_coins_24_black));
        txtStar.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        imgStar.setImageDrawable(getContext().getDrawable(R.drawable.ic_star_24_accent));
    }

    private void priceSelected() {
        txtDistance.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryText2));
        imgDistance.setImageDrawable(getContext().getDrawable(R.drawable.ic_marker_24_black));
        txtPrice.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        imgPrice.setImageDrawable(getContext().getDrawable(R.drawable.ic_coins_24_accent));
        txtStar.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryText2));
        imgStar.setImageDrawable(getContext().getDrawable(R.drawable.ic_star_24_black));
    }

    private void distanceSelected() {
        txtDistance.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        imgDistance.setImageDrawable(getContext().getDrawable(R.drawable.ic_marker_24_accent));
        txtPrice.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryText2));
        imgPrice.setImageDrawable(getContext().getDrawable(R.drawable.ic_coins_24_black));
        txtStar.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryText2));
        imgStar.setImageDrawable(getContext().getDrawable(R.drawable.ic_star_24_black));
    }

    private void nothingSelected() {
        txtDistance.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryText2));
        imgDistance.setImageDrawable(getContext().getDrawable(R.drawable.ic_marker_24_black));
        txtPrice.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryText2));
        imgPrice.setImageDrawable(getContext().getDrawable(R.drawable.ic_coins_24_black));
        txtStar.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryText2));
        imgStar.setImageDrawable(getContext().getDrawable(R.drawable.ic_star_24_black));
    }

    public interface OnItemSortSelected {
        void onSortSelect(int id);
    }

    public void setOnItemSortSelected(OnItemSortSelected onItemSortSelected) {
        this.onItemSortSelected = onItemSortSelected;
    }
}
