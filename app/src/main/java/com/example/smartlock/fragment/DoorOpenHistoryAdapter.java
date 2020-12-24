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
import com.example.smartlock.model.OpenHistoryItem;
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


public class DoorOpenHistoryAdapter extends RecyclerView.Adapter<DoorOpenHistoryAdapter.ViewHolder> {

    private List<OpenHistoryStream> mValues = new ArrayList<>();
    private DoorOpenHistoryAdapter mAdapter = this;
    private String mDeviceId;

    private void getDoorStatus() {
        Map<String, String> params = new HashMap<>();
        params.put("datastream_id", "door_function");  //数据流door_function
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
                            mValues.add(gson.fromJson(element, OpenHistoryStream.class));
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


    public DoorOpenHistoryAdapter(String deviceId) {
        this.mDeviceId = deviceId;
        getDoorStatus();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_door_open_history, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        OpenHistoryItem item = mValues.get(position).getValue();
        switch (item.getType()) {
            case 1:
                holder.mOpenType.setText(R.string.infrared_text);
                break;
            case 2:
                holder.mOpenType.setText(R.string.fingerprint_text);
                break;
            case 3:
                holder.mOpenType.setText(R.string.remote_text);
                break;
        }

        holder.mOpenId.setText("ID：" + item.getId());
        holder.mOpenStatusDate.setText(R.string.date_text);
        holder.mOpenStatusDate.append("：" + mValues.get(position).getDate());
        holder.mOpenStatusTime.setText(R.string.time_text);
        holder.mOpenStatusTime.append("：" + mValues.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mOpenType;
        public final TextView mOpenId;
        public final TextView mOpenStatusTime;
        public final TextView mOpenStatusDate;

        public ViewHolder(View view) {
            super(view);

            mOpenType = (TextView) view.findViewById(R.id.tv_open_type);
            mOpenId = (TextView) view.findViewById(R.id.tv_open_id);
            mOpenStatusTime = (TextView) view.findViewById(R.id.tv_stream_time);
            mOpenStatusDate = (TextView) view.findViewById(R.id.tv_stream_date);
        }

    }
}