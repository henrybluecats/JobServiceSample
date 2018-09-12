package com.bluecats.app.jobservicesample;

import com.bluecats.sdk.BCBeacon;
import com.bluecats.sdk.BCBeaconManager;
import com.bluecats.sdk.BCBeaconManagerCallback;
import com.bluecats.sdk.BCBeaconMode;
import com.bluecats.sdk.BlueCatsSDK;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private JobScheduler mJobScheduler;

    public static final int MY_JOB_ID = 1111;

    public static final String APP_TOKEN = "YOUR_APP_TOKEN";

    @Override
    protected void onResume() {
        super.onResume();

        checkPermission();

    }
    private void checkPermission() {
        int granded = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (granded != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 0x1010);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mJobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        mJobScheduler.schedule(getJobInfo(this));
    }


    private void cancelJob() {
        mJobScheduler.cancel(MY_JOB_ID);
        Log.d(TAG, "Job is canceled.");
    }

    public JobInfo getJobInfo(Context ctx) {
        JobInfo jobInfo = new JobInfo.Builder(MY_JOB_ID, new ComponentName(ctx, SdkStarterService.class))
                .setMinimumLatency(0)
                .build();
        return jobInfo;
    }

    public void unregisterCallback() {
        BlueCatsSDK.stopPurring();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startSDKPlain(getApplicationContext(), null);
            }
        }, 1000);
    }

    public static void startSDKPlain(Context ctx, Class<?> callback) {

        if (ctx == null) {
            return;
        }

        Map<String, String> options = new HashMap<>();

        options.put(BlueCatsSDK.BC_OPTION_CROWD_SOURCE_BEACON_UPDATES, "false");
        options.put(BlueCatsSDK.BC_OPTION_BEACON_VISIT_TRACKING_ENABLED, "false");

        /*
         * here is the magic of background scan when the BlueCats SDK is in background(no visible activities and
         * no forground Services are running)
         * An instance of BC_OPTION_BEACONMANAGER_CALLBACK_CLASS object will be created and bound to BlueCatsSDKService Which
         * is a JobSerivce that is able to run in background, you will receive callbacks in BC_OPTION_BEACONMANAGER_CALLBACK_CLASS object,
         * make sure BC_OPTION_BEACONMANAGER_CALLBACK_CLASS object is extended BCBeaconManagerCallback class.
         */
        if (callback != null) {
            options.put(BlueCatsSDK.BC_OPTION_BEACONMANAGER_CALLBACK_CLASS, callback.getCanonicalName());
        } else {
            options.put(BlueCatsSDK.BC_OPTION_BEACONMANAGER_CALLBACK_CLASS, ""); //unregister the callback class in this way
        }

        BlueCatsSDK.setOptions(options);
        BlueCatsSDK.startPurringWithAppToken(ctx, APP_TOKEN);
        BlueCatsSDK.didEnterForeground();
    }
}
