package com.example.smartlock.fragment;

import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.example.smartlock.R;
import com.example.smartlock.model.DoorStatusStream;
import com.example.smartlock.model.OpenHistoryStream;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DoorStatusHistoryAdapter extends RecyclerView.Adapter<DoorStatusHistoryAdapter.ViewHolder> {

    private List<DoorStatusStream> mValues = new ArrayList<>();
    private DoorStatusHistoryAdapter mAdapter = this;
    private String mDeviceId;

    private void getDoorStatus() {
        Map<String, String> params = new HashMap<>();
        params.put("datastream_id", "door_status");  //数据流door_status
        params.put("limit", "20");
        OneNetApi.queryDataPoints(mDeviceId, params, new OneNetApiCallback() {
            @Override
            public void onSuccess(String response) {
                JsonObject resp = new JsonParser().parse(response).getAsJsonObject();
                int errno = resp.get("errno").getAsInt();
                if (0 == errno) {
                    JsonArray dataElement = resp.get("data").getAsJsonObject().get("datastreams").getAsJsonArray();
                    if (dataElement != null) {

                        JsonArray jsonArray = dataElement.get(0).getAsJsonObject().get("datapoints").getAsJsonArray();
                        mValues.clear();
                        Gson gson = new Gson();
                        for (JsonElement element : jsonArray) {
                            mValues.add(gson.fromJson(element, DoorStatusStream.class));
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    String error = resp.get("error").getAsString();
                }
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });
    }


    public DoorStatusHistoryAdapter(String deviceId) {
        this.mDeviceId = deviceId;
        getDoorStatus();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_door_status_history, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mDoorStatus.setText(mValues.get(position).isDoorOpen() ? R.string.open_text : R.string.close_text);
        holder.mDoorStatusDate.setText(R.string.date_text);
        holder.mDoorStatusDate.append("：" + mValues.get(position).getDate());
        holder.mDoorStatusTime.setText(R.string.time_text);
        holder.mDoorStatusTime.append("：" + mValues.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mDoorStatus;
        public final TextView mDoorStatusTime;
        public final TextView mDoorStatusDate;

        public ViewHolder(View view) {
            super(view);

            mDoorStatus = (TextView) view.findViewById(R.id.tv_door_status);
            mDoorStatusTime = (TextView) view.findViewById(R.id.tv_stream_time);
            mDoorStatusDate = (TextView) view.findViewById(R.id.tv_stream_date);
        }

    }
}