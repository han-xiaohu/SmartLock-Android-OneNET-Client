package com.example.smartlock.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DoorStatusStream {
    private String at;
    private int value;

    public String getAt() {
        return at;
    }

    public int getValue() {
        return value;
    }

    public boolean isDoorOpen() {
        return value == 1;
    }


    public String getDate() {
        Date date = parseDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public String getTime() {
        Date date = parseDate();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateStr = sdf.format(date);
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
