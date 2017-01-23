package com.qadmni.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by akshay on 20-01-2017.
 */
public class DateUtils {
    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    final SimpleDateFormat dateReadFormat = new SimpleDateFormat("dd MMM hh:mm aa");
    final static SimpleDateFormat readableDateAndTime = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");

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
        java.util.Date date = null;
        try {
            date = df2.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getReadDateInFormat(date);
    }
}
