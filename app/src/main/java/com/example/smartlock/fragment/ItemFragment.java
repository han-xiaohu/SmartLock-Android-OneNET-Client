package com.example.smartlock.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartlock.R;


public class ItemFragment extends Fragment {

    private static final String ARG_LIST_TYPE = "list-type";
    private static final String ARG_DEVICE_ID = "device-id";

    private int mListType = 1;
    private String mDeviceId;

    public ItemFragment() {
    }

    public static ItemFragment newInstance(int listType, String deviceId) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, listType);
        args.putString(ARG_DEVICE_ID, deviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mListType = getArguments().getInt(ARG_LIST_TYPE);
            mDeviceId = getArguments().getString(ARG_DEVICE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if (mListType == 1) {
                recyclerView.setAdapter(new DoorOpenHistoryAdapter(mDeviceId));
            } else if (mListType == 2) {
                recyclerView.setAdapter(new DoorStatusHistoryAdapter(mDeviceId));
            }

        }
        return view;
    }
}