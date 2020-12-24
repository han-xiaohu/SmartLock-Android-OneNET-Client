package com.example.smartlock.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.example.smartlock.R;
import com.example.smartlock.model.DeviceItem;
import com.example.smartlock.model.DoorStatus;
import com.example.smartlock.utils.IntentActions;
import com.example.smartlock.utils.Preferences;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView mDoorStatusTv;
    private ImageView mOnlineLogoIv;
    private TextView mOnlineStatusTv;
    private TextView mErrorLogTv;
    private Button mOpenBtn;
    private Button mOpenHistoryBtn;
    private Button mDoorHistoryBtn;
    private Preferences mPreferences;

    private Timer mTimer;

    private String mApiKey;
    private String mDeviceId;
    private String mRemoteId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDoorStatusTv = findViewById(R.id.tv_door_status);
        mErrorLogTv = findViewById(R.id.tv_error_log);
        mOnlineStatusTv = findViewById(R.id.tv_online_status);
        mOnlineLogoIv = findViewById(R.id.iv_online_status);
        mOpenBtn = findViewById(R.id.btn_open_door);
        mOpenHistoryBtn = findViewById(R.id.btn_open_history);
        mDoorHistoryBtn = findViewById(R.id.btn_door_history);


        mPreferences = Preferences.getInstance(this);
        mApiKey = mPreferences.getString(Preferences.API_KEY, "");
        mDeviceId = mPreferences.getString(Preferences.DEVICE_ID, "");
        mRemoteId = mPreferences.getString(Preferences.REMOTE_ID, "");

        if (mApiKey.isEmpty() || mDeviceId.isEmpty() || mRemoteId.isEmpty()) {
            startActivity(new Intent(this, SettingActivity.class));
        } else {
            OneNetApi.setAppKey(mApiKey);
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateApiKeyReceiver, new IntentFilter(IntentActions.ACTION_UPDATE_APIKEY));

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplication(), SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getStatus() {
        OneNetApi.querySingleDataStream(mDeviceId, "door_status", new OneNetApiCallback() {
            @Override
            public void onSuccess(String response) {
                JsonObject resp = new JsonParser().parse(response).getAsJsonObject();
                int errno = resp.get("errno").getAsInt();
                if (0 == errno) {
                    JsonElement dataElement = resp.get("data");
                    if (dataElement != null) {
                        Gson gson = new Gson();
                        DoorStatus DSDoorStatus = gson.fromJson(dataElement, DoorStatus.class);
                        if (DSDoorStatus.isDoorOpen()) {
                            mDoorStatusTv.setText(R.string.open_text);
                        } else {
                            mDoorStatusTv.setText(R.string.close_text);
                        }
                    }
                    mErrorLogTv.setText(null);
                } else {
                    String error = resp.get("error").getAsString();
                    mErrorLogTv.setText(String.valueOf(error));
                }
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });


        OneNetApi.querySingleDevice(mDeviceId, new OneNetApiCallback() {
            @Override
            public void onSuccess(String response) {
                JsonObject resp = new JsonParser().parse(response).getAsJsonObject();
                int errno = resp.get("errno").getAsInt();
                if (0 == errno) {
                    JsonElement dataElement = resp.get("data");
                    if (dataElement != null) {
                        Gson gson = new Gson();
                        DeviceItem deviceItem = gson.fromJson(dataElement, DeviceItem.class);
                        if (deviceItem.isOnline()) {
                            mOnlineStatusTv.setText(R.string.online_text);
                            mOnlineLogoIv.setImageResource(R.drawable.shape_online);
                        } else {
                            mOnlineStatusTv.setText(R.string.offline_text);
                            mOnlineLogoIv.setImageResource(R.drawable.shape_offline);
                        }
                    }
                    mErrorLogTv.setText(null);
                } else {
                    String error = resp.get("error").getAsString();
                    mErrorLogTv.setText(String.valueOf(error));
                }
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void openDoor(View v) {
        OneNetApi.sendCmdToDevice(mDeviceId, "{\"opendoor\":" + mRemoteId + "}", new OneNetApiCallback() {
            @Override
            public void onSuccess(String response) {
                JsonObject resp = new JsonParser().parse(response).getAsJsonObject();
                int errno = resp.get("errno").getAsInt();
                if (0 == errno) {
                    Toast.makeText(getApplicationContext(), R.string.successful_text, Toast.LENGTH_SHORT).show();
                } else {
                    String error = resp.get("error").getAsString();
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void doorOpenHistory(View v) {
        startActivity(ItemActivity.newIntent(this, 1, mDeviceId));
    }

    public void doorStatusHistory(View v) {
        startActivity(ItemActivity.newIntent(this, 2, mDeviceId));
    }


    private BroadcastReceiver mUpdateApiKeyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mApiKey = mPreferences.getString(Preferences.API_KEY, "");
            mDeviceId = mPreferences.getString(Preferences.DEVICE_ID, "");
            mRemoteId = mPreferences.getString(Preferences.REMOTE_ID, "");
        }
    };

    private void startTask() {

        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getStatus();
            }
        }, 100, 3000);
    }

}