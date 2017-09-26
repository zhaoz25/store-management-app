package com.example.dellcorei3.storemanagement.store.manager.controller;

import android.util.Log;

import com.google.firebase.database.ServerValue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 9/21/2017.
 */

public class TimeFormat {


    public static String convertTimeStampToDate(Map<String,Object> map){
        SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy");
        Long timestamp = (Long)map.get("timestamp");

        String date = sfd.format(new Date(timestamp));
        return date;
    }

    public static List<Date> getDates(String dateString1, String dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(dateString1);
            date2 = df1 .parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static Long convertDateToSeconds(String s){
        String myDate = s+" 00:00";
        long millis = 0;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = sdf.parse(myDate);
            millis = date.getTime();
        } catch (ParseException e) {
            Log.d("error", e.getMessage());
        }
        return millis;
    }
}
