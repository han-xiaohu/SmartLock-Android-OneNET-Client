package com.example.smartlock.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.example.smartlock.R;
import com.example.smartlock.utils.IntentActions;
import com.example.smartlock.utils.Preferences;

public class SettingActivity extends AppCompatActivity {

    private Preferences mPreferences;
    private EditText mApiKeyEt;
    private EditText mDeviceIdEt;
    private EditText mRemoteIdEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        mPreferences = Preferences.getInstance(this);
        mApiKeyEt = findViewById(R.id.et_api_key);
        mDeviceIdEt = findViewById(R.id.et_device_id);
        mRemoteIdEt = findViewById(R.id.et_remote_id);

        String apiKey = mPreferences.getString(Preferences.API_KEY, "");
        String deviceId = mPreferences.getString(Preferences.DEVICE_ID, "");
        String remoteId = mPreferences.getString(Preferences.REMOTE_ID, "");
        if (0 == apiKey.length()) {
            apiKey = OneNetApi.getAppKey();
        }

        mApiKeyEt.setText(apiKey);
        mDeviceIdEt.setText(deviceId);
        mRemoteIdEt.setText(remoteId);
    }

    public void saveKeyInfo(View v) {
        String apiKey = mApiKeyEt.getText().toString();
        String deviceId = mDeviceIdEt.getText().toString();
        String remoteId = mRemoteIdEt.getText().toString();

        if (apiKey.trim().isEmpty() || deviceId.trim().isEmpty() || remoteId.isEmpty()) {
            Toast.makeText(this, R.string.not_empty_text, Toast.LENGTH_SHORT).show();
        } else {
            OneNetApi.setAppKey(apiKey.trim());
            mPreferences.putString(Preferences.API_KEY, apiKey.trim());
            mPreferences.putString(Preferences.DEVICE_ID, deviceId.trim());
            mPreferences.putString(Preferences.REMOTE_ID, remoteId.trim());

            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(IntentActions.ACTION_UPDATE_APIKEY));
            Toast.makeText(this, R.string.save_successful_text, Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}