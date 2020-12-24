package com.example.smartlock.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.smartlock.R;
import com.example.smartlock.fragment.ItemFragment;


public class ItemActivity extends AppCompatActivity {

    private static final String ARG_LIST_TYPE = "arg_list_type";
    private static final String ARG_DEVICE_ID = "arg_device_id";

    public static Intent newIntent(Context packageContext, int type, String deviceId) {
        Intent intent = new Intent(packageContext, ItemActivity.class);
        intent.putExtra(ARG_LIST_TYPE, type);
        intent.putExtra(ARG_DEVICE_ID, deviceId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity);


        String deviceId = getIntent().getStringExtra(ARG_DEVICE_ID);
        int listType = getIntent().getIntExtra(ARG_LIST_TYPE, 1);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ItemFragment.newInstance(listType, deviceId))
                    .commitNow();
        }
    }

}