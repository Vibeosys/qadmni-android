package com.qadmni.utils;

import android.content.Context;
import android.content.Intent;

import com.qadmni.activity.VendorLoginActivity;
import com.qadmni.data.CustomerDTO;
import com.qadmni.data.VendorDTO;

/**
 * Created by akshay on 16-01-2017.
 */
public class UserAuth {

    public static boolean isVendorLoggedIn(Context context, String userName, String password) {
        if (password == null || password == "" || userName == null || userName == "") {
            Intent theLoginIntent = new Intent(context, VendorLoginActivity.class);
            //theLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            theLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            theLoginIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(theLoginIntent);
            return false;
        }
        return true;
    }


    public static boolean isVendorLoggedIn() {
        long subscriberId = SessionManager.Instance().getVendorId();
        String theUserEmail = SessionManager.Instance().getVendorEmailId();
        //String theUserPhotoURL = SessionManager.Instance().getUserPhotoUrl();
        if (subscriberId == 0 || theUserEmail == null || theUserEmail == "") {
            return false;
        }
        return true;
    }

    public static boolean isUserLoggedIn() {
        long customerId = SessionManager.Instance().getCustomerId();
        String theUserEmail = SessionManager.Instance().getUserEmail();
        //String theUserPhotoURL = SessionManager.Instance().getUserPhotoUrl();
        if (customerId == 0 || theUserEmail == null || theUserEmail == "") {
            return false;
        }
        return true;
    }

    public static boolean isAnyUserLogin() {
        int userType = SessionManager.Instance().getUserType();
        if (userType == UserType.USER_OTHER) {
            return false;
        } else if (userType == UserType.USER_VENDOR) {
            return isVendorLoggedIn();
        } else if (userType == UserType.USER_CUSTOMER) {
            return isUserLoggedIn();
        }
        return true;
    }

    public void saveVendorInfo(VendorDTO vendorDTO, final Context context) {
        if (vendorDTO == null)
            return;

        if (vendorDTO.getEmailId() == null || vendorDTO.getEmailId() == "" ||
                vendorDTO.getProducerName() == null || vendorDTO.getProducerName() == "")
            return;

        SessionManager theSessionManager = SessionManager.getInstance(context);
        theSessionManager.setVendorId(vendorDTO.getProducerId());
        theSessionManager.setVendorName(vendorDTO.getProducerName());
        theSessionManager.setVendorEmailId(vendorDTO.getEmailId());
        theSessionManager.setVendorPassword(vendorDTO.getPassword());
        theSessionManager.setUserType(UserType.USER_VENDOR);
        theSessionManager.setBusinessNameEn(vendorDTO.getBusinessNameEn());
        theSessionManager.setBusinessNameAr(vendorDTO.getBusinessNameAr());
        theSessionManager.setBusinessAddress(vendorDTO.getBusinessAddress());
        theSessionManager.setBusinessLat(vendorDTO.getBusinessLat());
        theSessionManager.setBusinessLong(vendorDTO.getBusinessLong());

    }

    public static boolean CleanVendorAuthenticationInfo() {

        SessionManager theSessionManager = SessionManager.Instance();
        theSessionManager.setVendorId(0);
        theSessionManager.setVendorName(null);
        theSessionManager.setVendorEmailId(null);
        theSessionManager.setVendorPassword(null);
        theSessionManager.setBusinessNameEn(null);
        theSessionManager.setBusinessNameAr(null);
        theSessionManager.setBusinessAddress(null);
        theSessionManager.setBusinessLat(0);
        theSessionManager.setBusinessLong(0);
        theSessionManager.setUserType(UserType.USER_OTHER);
        return true;
    }

    public void saveUserInfo(CustomerDTO customerDTO, Context context) {
        if (customerDTO == null)
            return;
        SessionManager theSessionManager = SessionManager.getInstance(context);
        theSessionManager.setCustomerId(customerDTO.getCustomerId());
        theSessionManager.setCustomerName(customerDTO.getName());
        theSessionManager.setCustomerPh(customerDTO.getPhone());
        theSessionManager.setUserEmail(customerDTO.getEmailId());
        theSessionManager.setUserPass(customerDTO.getPassword());
        theSessionManager.setUserType(UserType.USER_CUSTOMER);
    }

    public static boolean CleanCustomerAuthenticationInfo() {

        SessionManager theSessionManager = SessionManager.Instance();
        theSessionManager.setCustomerId(0);
        theSessionManager.setCustomerName(null);
        theSessionManager.setCustomerPh(null);
        theSessionManager.setUserEmail(null);
        theSessionManager.setUserPass(null);
        theSessionManager.setUserType(UserType.USER_OTHER);
        return true;
    }
}
