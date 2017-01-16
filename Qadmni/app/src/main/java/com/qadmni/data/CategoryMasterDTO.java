package com.qadmni.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.qadmni.data.responseDataDTO.CategoryListResponseDTO;

/**
 * Created by shrinivas on 14-01-2017.
 */
public class CategoryMasterDTO implements Parcelable {
    private int categoryId;
    private String category;

    public CategoryMasterDTO(CategoryListResponseDTO categoryListResponseDTO) {
        this.categoryId = categoryListResponseDTO.getCategoryId();
        this.category = categoryListResponseDTO.getCategory();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    protected CategoryMasterDTO(Parcel in) {
        categoryId = in.readInt();
        category = in.readString();
    }

    public static final Creator<CategoryMasterDTO> CREATOR = new Creator<CategoryMasterDTO>() {
        @Override
        public CategoryMasterDTO createFromParcel(Parcel in) {
            return new CategoryMasterDTO(in);
        }

        @Override
        public CategoryMasterDTO[] newArray(int size) {
            return new CategoryMasterDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(categoryId);
        parcel.writeString(category);
    }
}
