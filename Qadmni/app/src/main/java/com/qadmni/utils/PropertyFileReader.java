package com.qadmni.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by shrinivas on 12-01-2017.
 */
public class PropertyFileReader {
    private static PropertyFileReader mPropertyFileReader = null;
    private static Context mContext;
    protected static Properties mProperties;

    public static PropertyFileReader getInstance(Context context) {
        if (mPropertyFileReader != null)
            return mPropertyFileReader;

        mContext = context;
        mProperties = getProperties();
        mPropertyFileReader = new PropertyFileReader();
        return mPropertyFileReader;
    }

    protected static Properties getProperties() {
        try {
            AssetManager assetManager = mContext.getAssets();
            InputStream inputStream = assetManager.open("app.properties");
            mProperties = new Properties();
            mProperties.load(inputStream);

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        return mProperties;
    }

    protected String getEndPointUri() {
        return mProperties.getProperty(PropertyTypeConstants.API_ENDPOINT_URI);
    }

    public float getVersion() {
        String versionNumber = mProperties.getProperty(PropertyTypeConstants.VERSION_NUMBER);
        return Float.valueOf(versionNumber);
    }

    public String getCategoryListUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.GET_CATEGORY_LIST);
    }

    public String registerShopUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.REGISTER_SHOP);
    }

    public String getItemListUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.GET_ITEM_LIST);
    }

    public String vendorLoginUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.VENDOR_LOGIN_URL);
    }

    public String vendorListUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.VENDOR_ITEMS);
    }

    public String customerLoginUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.CUSTOMER_LOGIN);
    }

    public String customerRegisterUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.REGISTER_CUSTOMER);
    }

    public String addProductUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.ADD_PRODUCT);
    }

    public String addProductImageUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.ADD_PRODUCT_IMAGE);
    }

    public String updateDeliveryStatus() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.UPDATE_DELIVERY_STATUS);
    }

    public int getDbVersion() {
        String versionNumber = mProperties.getProperty(PropertyTypeConstants.DATABASE_VERSION_NUMBER);
        return Integer.valueOf(versionNumber);
    }


    public String initOrder() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.INIT_ORDER);
    }

    public String processOrder() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.PROCESS_ORDER);
    }

    public String confirmOrder() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.CONFIRM_ORDER);
    }

    public String liveOrders() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.LIVE_ORDERS);
    }

    public String vendorOrder() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.VENDOR_ORDER);
    }

    public String customerForgotPw() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.CUSTOMER_FORGOT_PW);
    }

    public String vendorForgotPw() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.VENDOR_FORGOT_PW);
    }

    public String pastOrder() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.PAST_ORDERS);
    }

    public String isDuplicateEmail() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.IS_DUPLICATE_EMAIL);
    }

    public String updateProductUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.UPDATE_PRODUCT);
    }

    public String getItemDetails() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.GET_ITEM_DETAILS);
    }

    public String getUserFavItem() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.GET_FAV_ITEM_LIST);
    }

    public String addRemoveFav() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.ADD_OR_REMOVE_FAV);
    }

    public String getReviewItems() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.GET_REVIEW_ITEMS);
    }

    public String submitReview() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.SUBMIT_REVIEW_ITEMS);
    }

    public String getOrderDetails() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.GET_ORDER_DETAILS);
    }
    public String getCustomerEditProfile()
    {
        return getEndPointUri()+mProperties.getProperty(PropertyTypeConstants.GET_CUSTOMER_PROFILE);
    }
    public String getProducerEditProfile()
    {
        return getEndPointUri()+mProperties.getProperty(PropertyTypeConstants.GET_VENDOR_PROFILE);
    }
}
