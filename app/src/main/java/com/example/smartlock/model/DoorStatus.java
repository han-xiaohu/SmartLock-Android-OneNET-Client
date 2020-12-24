package com.example.smartlock.model;

import com.google.gson.annotations.SerializedName;

public class DoorStatus {
    private String id;

    @SerializedName("current_value")
    private int currentValue;
    @SerializedName("update_at")
    private String updateTime;


    public String getId() {
        return id;
    }


    public int getCurrentValue() {
        return currentValue;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public boolean isDoorOpen() {
        return currentValue == 1;
    }
}
