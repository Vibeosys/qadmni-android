package com.qadmni.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by shrinivas on 12-01-2017.
 */
public class SessionManager {
    private static final String PROJECT_PREFERENCES = "com.quadmni";
    private static SessionManager mSessionManager;
    private static SharedPreferences mProjectSharedPref = null;
    private static Context mContext = null;
    private static PropertyFileReader mPropertyFileReader = null;

    public static SessionManager getInstance(Context context) {
        if (mSessionManager != null)
            return mSessionManager;

        mContext = context;
        mPropertyFileReader = mPropertyFileReader.getInstance(context);
        loadProjectSharedPreferences();
        mSessionManager = new SessionManager();

        return mSessionManager;

    }

    public static SessionManager Instance() throws IllegalArgumentException {
        if (mSessionManager != null)
            return mSessionManager;
        else
            throw new IllegalArgumentException("No instance is yet created");
    }

    private static void loadProjectSharedPreferences() {
        if (mProjectSharedPref == null) {
            mProjectSharedPref = mContext.getSharedPreferences(PROJECT_PREFERENCES, Context.MODE_PRIVATE);
        }

        String versionNumber = mProjectSharedPref.getString(PropertyTypeConstants.VERSION_NUMBER, null);
        Float versionNoValue = versionNumber == null ? 0 : Float.valueOf(versionNumber);

        if (mPropertyFileReader.getVersion() > versionNoValue) {
            boolean sharedPrefChange = addOrUpdateSharedPreferences();
            if (!sharedPrefChange)
                Log.e("SharedPref", "No shared preferences are changed");
        }
    }

    private static boolean addOrUpdateSharedPreferences() {

        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putString(PropertyTypeConstants.GET_CATEGORY_LIST, mPropertyFileReader.getCategoryListUrl());
        editor.putString(PropertyTypeConstants.REGISTER_SHOP, mPropertyFileReader.registerShopUrl());
        editor.putString(PropertyTypeConstants.GET_ITEM_LIST, mPropertyFileReader.getItemListUrl());
        editor.putString(PropertyTypeConstants.VENDOR_LOGIN_URL, mPropertyFileReader.vendorLoginUrl());
        editor.putString(PropertyTypeConstants.VENDOR_ITEMS, mPropertyFileReader.vendorListUrl());
        editor.putString(PropertyTypeConstants.CUSTOMER_LOGIN, mPropertyFileReader.customerLoginUrl());
        editor.putString(PropertyTypeConstants.REGISTER_CUSTOMER, mPropertyFileReader.customerRegisterUrl());
        editor.putString(PropertyTypeConstants.ADD_PRODUCT, mPropertyFileReader.addProductUrl());
        editor.putString(PropertyTypeConstants.ADD_PRODUCT_IMAGE, mPropertyFileReader.addProductImageUrl());
        editor.putString(PropertyTypeConstants.INIT_ORDER, mPropertyFileReader.initOrder());
        editor.putString(PropertyTypeConstants.PROCESS_ORDER, mPropertyFileReader.processOrder());
        editor.putString(PropertyTypeConstants.CONFIRM_ORDER, mPropertyFileReader.confirmOrder());
        editor.putString(PropertyTypeConstants.LIVE_ORDERS, mPropertyFileReader.liveOrders());
        editor.putString(PropertyTypeConstants.VENDOR_ORDER, mPropertyFileReader.vendorOrder());
        editor.putString(PropertyTypeConstants.CUSTOMER_FORGOT_PW, mPropertyFileReader.customerForgotPw());
        editor.putString(PropertyTypeConstants.VENDOR_FORGOT_PW, mPropertyFileReader.vendorForgotPw());
        editor.putString(PropertyTypeConstants.PAST_ORDERS, mPropertyFileReader.pastOrder());
        editor.putString(PropertyTypeConstants.IS_DUPLICATE_EMAIL, mPropertyFileReader.isDuplicateEmail());
        editor.putString(PropertyTypeConstants.UPDATE_PRODUCT, mPropertyFileReader.updateProductUrl());
        editor.putString(PropertyTypeConstants.GET_ITEM_DETAILS, mPropertyFileReader.getItemDetails());
        editor.putString(PropertyTypeConstants.GET_FAV_ITEM_LIST, mPropertyFileReader.getUserFavItem());
        editor.putString(PropertyTypeConstants.ADD_OR_REMOVE_FAV, mPropertyFileReader.addRemoveFav());
        editor.putString(PropertyTypeConstants.UPDATE_DELIVERY_STATUS, mPropertyFileReader.updateDeliveryStatus());
        editor.putString(PropertyTypeConstants.GET_REVIEW_ITEMS, mPropertyFileReader.getReviewItems());
        editor.putString(PropertyTypeConstants.SUBMIT_REVIEW_ITEMS, mPropertyFileReader.submitReview());
        editor.putString(PropertyTypeConstants.GET_ORDER_DETAILS, mPropertyFileReader.getOrderDetails());
        editor.putInt(PropertyTypeConstants.DATABASE_VERSION_NUMBER, mPropertyFileReader.getDbVersion());
        editor.putString(PropertyTypeConstants.GET_CUSTOMER_PROFILE, mPropertyFileReader.getCustomerEditProfile());
        editor.putString(PropertyTypeConstants.GET_VENDOR_PROFILE, mPropertyFileReader.getProducerEditProfile());
        editor.putString(PropertyTypeConstants.GET_ORDER_TRACK_DETAILS, mPropertyFileReader.getOrderTrackDetails());
        editor.apply();
        return true;
    }

    private static void setValuesInSharedPrefs(String sharedPrefKey, long sharedPrefValue) {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putLong(sharedPrefKey, sharedPrefValue);
        editor.apply();
    }

    private static void setValuesInSharedPrefs(String sharedPrefKey, int sharedPrefValue) {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putInt(sharedPrefKey, sharedPrefValue);
        editor.apply();
    }

    private static void setValuesInSharedPrefs(String sharedPrefKey, String sharedPrefValue) {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putString(sharedPrefKey, sharedPrefValue);
        editor.apply();
    }

    public String getCategoryListUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.GET_CATEGORY_LIST, null);
    }

    public int getDatabaseVersion() {
        return mProjectSharedPref.getInt(PropertyTypeConstants.DATABASE_VERSION_NUMBER, 0);
    }

    public String getUserSelectedLang() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_SELECTED_LANGUAGE, "En");
    }

    public void setUserSelectedLang(String UserLang) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_SELECTED_LANGUAGE, UserLang);
    }

    public String registerBusiness() {
        return mProjectSharedPref.getString(PropertyTypeConstants.REGISTER_SHOP, null);
    }

    public String getItemListUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.GET_ITEM_LIST, null);
    }

    public String loginVendorUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.VENDOR_LOGIN_URL, null);
    }

    public String getLiveOrdersUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.LIVE_ORDERS, null);
    }

    public String getPastOrderUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.PAST_ORDERS, null);
    }

    public String getEditCustomerProfileUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.GET_CUSTOMER_PROFILE, null);
    }

    public String getEditProducerProfileUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.GET_VENDOR_PROFILE, null);
    }

    public void setVendorId(long vendorId) {
        setValuesInSharedPrefs(PropertyTypeConstants.VENDOR_ID, vendorId);
    }

    public long getVendorId() {
        return mProjectSharedPref.getLong(PropertyTypeConstants.VENDOR_ID, 0);
    }

    public void setVendorName(String vendorName) {
        setValuesInSharedPrefs(PropertyTypeConstants.VENDOR_NAME, vendorName);
    }

    public String getVendorName() {
        return mProjectSharedPref.getString(PropertyTypeConstants.VENDOR_NAME, null);
    }

    public void setVendorEmailId(String emailId) {
        setValuesInSharedPrefs(PropertyTypeConstants.VENDOR_EMAIL, emailId);
    }

    public String getVendorEmailId() {
        return mProjectSharedPref.getString(PropertyTypeConstants.VENDOR_EMAIL, null);
    }

    public void setVendorPassword(String vendorPassword) {
        setValuesInSharedPrefs(PropertyTypeConstants.VENDOR_PASSWORD, vendorPassword);
    }

    public String getVendorPassword() {
        return mProjectSharedPref.getString(PropertyTypeConstants.VENDOR_PASSWORD, null);
    }

    public void setUserType(int userType) {
        setValuesInSharedPrefs(PropertyTypeConstants.APP_USER_TYPE, userType);
    }

    public int getUserType() {
        return mProjectSharedPref.getInt(PropertyTypeConstants.APP_USER_TYPE, 0);
    }

    public void setBusinessNameEn(String businessNameEn) {
        setValuesInSharedPrefs(PropertyTypeConstants.BUSINESS_NAME_EN, businessNameEn);
    }

    public String getBusinessNameEn() {
        return mProjectSharedPref.getString(PropertyTypeConstants.BUSINESS_NAME_EN, null);
    }

    public void setBusinessNameAr(String businessNameAr) {
        setValuesInSharedPrefs(PropertyTypeConstants.BUSINESS_NAME_AR, businessNameAr);
    }

    public String getBusinessNameAr() {
        return mProjectSharedPref.getString(PropertyTypeConstants.BUSINESS_NAME_AR, null);
    }

    public void setBusinessAddress(String businessAddress) {
        setValuesInSharedPrefs(PropertyTypeConstants.BUSINESS_ADDRESS, businessAddress);
    }

    public String getBusinessAddress() {
        return mProjectSharedPref.getString(PropertyTypeConstants.BUSINESS_ADDRESS, null);
    }

    public void setBusinessLat(double businessLat) {
        setValuesInSharedPrefs(PropertyTypeConstants.BUSINESS_LAT, String.valueOf(businessLat));
    }

    public double getBusinessLat() {
        return Double.parseDouble(mProjectSharedPref.getString(PropertyTypeConstants.BUSINESS_LAT, "" + 0));
    }

    public void setBusinessLong(double businessLong) {
        setValuesInSharedPrefs(PropertyTypeConstants.BUSINESS_LONG, String.valueOf(businessLong));
    }

    public double getBusinessLong() {
        return Double.parseDouble(mProjectSharedPref.getString(PropertyTypeConstants.BUSINESS_LONG, "" + 0));
    }


    public String getVendorItems() {
        return mProjectSharedPref.getString(PropertyTypeConstants.VENDOR_ITEMS, null);
    }

    public String loginCostomerUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.CUSTOMER_LOGIN, null);
    }

    public void setCustomerId(long customerId) {
        setValuesInSharedPrefs(PropertyTypeConstants.CUSTOMER_ID, customerId);
    }

    public long getCustomerId() {
        return mProjectSharedPref.getLong(PropertyTypeConstants.CUSTOMER_ID, 0);
    }

    public void setCustomerName(String customerName) {
        setValuesInSharedPrefs(PropertyTypeConstants.CUSTOMER_NAME, customerName);
    }

    public String getCustomerName() {
        return mProjectSharedPref.getString(PropertyTypeConstants.CUSTOMER_NAME, null);
    }

    public void setCustomerPh(String customerPh) {
        setValuesInSharedPrefs(PropertyTypeConstants.CUSTOMER_PHONE, customerPh);
    }

    public String getCustomerPh() {
        return mProjectSharedPref.getString(PropertyTypeConstants.CUSTOMER_PHONE, null);
    }

    public void setUserEmail(String userEmail) {
        setValuesInSharedPrefs(PropertyTypeConstants.CUSTOMER_EMAIL, userEmail);
    }

    public String getUserEmail() {
        return mProjectSharedPref.getString(PropertyTypeConstants.CUSTOMER_EMAIL, null);
    }

    public void setUserPass(String userPass) {
        setValuesInSharedPrefs(PropertyTypeConstants.CUSTOMER_PASSWORD, userPass);
    }

    public String getUserPass() {
        return mProjectSharedPref.getString(PropertyTypeConstants.CUSTOMER_PASSWORD, null);
    }

    public String registerCostomerUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.REGISTER_CUSTOMER, null);
    }

    public String customerForgotPwUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.CUSTOMER_FORGOT_PW, null);
    }

    public String vendorForgotPwUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.VENDOR_FORGOT_PW, null);
    }

    public String addProductUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.ADD_PRODUCT, null);
    }

    public String uploadProductPhoto() {
        return mProjectSharedPref.getString(PropertyTypeConstants.ADD_PRODUCT_IMAGE, null);
    }

    public void setProducerId(long producerId) {
        setValuesInSharedPrefs(PropertyTypeConstants.PRODUCER_ID, producerId);
    }

    public long getProducerId() {
        return mProjectSharedPref.getLong(PropertyTypeConstants.PRODUCER_ID, 0);
    }

    public String initOrderUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.INIT_ORDER, null);
    }

    public String processOrderUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.PROCESS_ORDER, null);
    }

    public String confirmOrderUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.CONFIRM_ORDER, null);
    }

    public void setListSortBy(int listSortBy) {
        setValuesInSharedPrefs(PropertyTypeConstants.CUSTOMER_FILTER_SORT_BY, listSortBy);
    }

    public int getListSortBy() {
        return mProjectSharedPref.getInt(PropertyTypeConstants.CUSTOMER_FILTER_SORT_BY, 0);
    }

    public void setSelectedDistance(int selectedDistance) {
        setValuesInSharedPrefs(PropertyTypeConstants.CUSTOMER_FILTER_SELECTED_DIST, selectedDistance);
    }

    public int getSelectedDistance() {
        return mProjectSharedPref.getInt(PropertyTypeConstants.CUSTOMER_FILTER_SELECTED_DIST, 0);
    }

    public void setSelectedPrice(int selectedPrice) {
        setValuesInSharedPrefs(PropertyTypeConstants.CUSTOMER_FILTER_SELECTED_PRICE, selectedPrice);
    }

    public int getSelectedPrice() {
        return mProjectSharedPref.getInt(PropertyTypeConstants.CUSTOMER_FILTER_SELECTED_PRICE, 0);
    }

    public String getVendorOrder() {
        return mProjectSharedPref.getString(PropertyTypeConstants.VENDOR_ORDER, null);
    }

    public String isDuplicateUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.IS_DUPLICATE_EMAIL, null);
    }

    public String updateProductUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.UPDATE_PRODUCT, null);
    }

    public String getItemDetails() {
        return mProjectSharedPref.getString(PropertyTypeConstants.GET_ITEM_DETAILS, null);
    }

    public String getUserFavItem() {
        return mProjectSharedPref.getString(PropertyTypeConstants.GET_FAV_ITEM_LIST, null);
    }

    public String addOrRemoveFav() {
        return mProjectSharedPref.getString(PropertyTypeConstants.ADD_OR_REMOVE_FAV, null);
    }

    public String getUpdateOrderStatusUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.UPDATE_DELIVERY_STATUS, null);
    }


    public String getReviewItems() {
        return mProjectSharedPref.getString(PropertyTypeConstants.GET_REVIEW_ITEMS, null);
    }

    public String submitReviewItems() {
        return mProjectSharedPref.getString(PropertyTypeConstants.SUBMIT_REVIEW_ITEMS, null);
    }

    public String getOrderDetails() {
        return mProjectSharedPref.getString(PropertyTypeConstants.GET_ORDER_DETAILS, null);
    }

    public String getOrderTrackDetails() {
        return mProjectSharedPref.getString(PropertyTypeConstants.GET_ORDER_TRACK_DETAILS, null);
    }
}
