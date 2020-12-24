package com.example.smartlock.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OpenHistoryStream {
    private String at;
    private String value;

    public OpenHistoryItem getValue() {
        return new OpenHistoryItem(value);
    }

    public String getDate() {
        Date date = parseDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr=sdf.format(date);
        return dateStr;
    }

    public String getTime() {
        Date date = parseDate();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateStr=sdf.format(date);
        return dateStr;
    }


    private Date parseDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(at);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

}
