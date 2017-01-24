package com.qadmni.utils;


import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by akshay on 20-01-2017.
 */
public class DateUtils {
    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    final SimpleDateFormat dateReadFormat = new SimpleDateFormat("dd MMM hh:mm aa");
    final static SimpleDateFormat readableDateAndTime = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
    final SimpleDateFormat dateWithTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public String getLocalDateInFormat(java.util.Date date) {
        return dateFormat.format(date);
    }

    public static String getReadDateInFormat(java.util.Date date) {
        return readableDateAndTime.format(date);
    }

    public String getReadableDateNTime(Date date) {
        return dateReadFormat.format(date);
    }

    public static String convertRegisterTimeToDate(String strDate) {
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        df2.setTimeZone(TimeZone.getTimeZone("gmt"));
        java.util.Date date = null;
        try {
            date = df2.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getReadDateInFormat(date);
    }

    public java.util.Date getFormattedDate(String strDate) {
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df2.setTimeZone(TimeZone.getTimeZone("gmt"));
        java.util.Date date = null;
        try {
            date = df2.parse(strDate);
            Log.d("TAG", "TAG");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
