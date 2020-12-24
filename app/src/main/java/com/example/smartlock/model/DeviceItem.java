package com.example.smartlock.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeviceItem implements Serializable {
    private String id;
    private String title;

    private String protocol = "HTTP";
    private boolean online;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("auth_info")
    private String authInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(String authInfo) {
        this.authInfo = authInfo;
    }

}
