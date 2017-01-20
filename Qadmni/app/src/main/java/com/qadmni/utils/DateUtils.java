package com.qadmni.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by akshay on 20-01-2017.
 */
public class DateUtils {
    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    final SimpleDateFormat dateReadFormat = new SimpleDateFormat("dd MMM hh:mm aa");


    public String getLocalDateInFormat(java.util.Date date) {
        return dateFormat.format(date);
    }

    public String getReadableDateNTime(Date date) {
        return dateReadFormat.format(date);
    }
}
