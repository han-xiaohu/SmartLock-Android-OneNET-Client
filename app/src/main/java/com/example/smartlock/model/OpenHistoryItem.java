package com.example.smartlock.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OpenHistoryItem {
    private int type;
    private int id;

    public OpenHistoryItem(String value) {
        JsonObject resp = new JsonParser().parse(value).getAsJsonObject();
        this.type = resp.get("type").getAsInt();
        this.id = resp.get("id").getAsInt();
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
