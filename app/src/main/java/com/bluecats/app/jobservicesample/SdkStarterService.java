package com.bluecats.app.jobservicesample;

import com.bluecats.sdk.BCBeacon;
import com.bluecats.sdk.BCBeaconManager;
import com.bluecats.sdk.BCBeaconManagerCallback;
import com.bluecats.sdk.BCBeaconRegion;
import com.bluecats.sdk.BCLogManager;
import com.bluecats.sdk.BlueCatsSDK;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SdkStarterService extends JobService {


    private static final String TAG = "SdkStarterService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        MainActivity.startSDKPlain(getApplicationContext(), MyBeaconCallback.class);

        Log.d(TAG, "onStartJob");

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob");
        return false;
    }


}
