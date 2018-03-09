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
    BCBeaconManager mBeaconManager;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Map<String, String> options = new HashMap<>();

//        options.put(BlueCatsSDK.BC_OPTION_DISCOVER_BEACONS_NEARBY, "false");
//        options.put(BlueCatsSDK.BC_OPTION_CROWD_SOURCE_BEACON_UPDATES, "false");
//        options.put(BlueCatsSDK.BC_OPTION_BEACON_VISIT_TRACKING_ENABLED, "false");
//        options.put(BlueCatsSDK.BC_OPTION_CACHE_ALL_BEACONS_FOR_APP, "false");
//        options.put(BlueCatsSDK.BC_OPTION_CACHE_ALL_BEACONS_FOR_SITE, "false");
//        options.put(BlueCatsSDK.BC_OPTION_POST_ON_VISIT_BEGINS, "true");
//        options.put(BlueCatsSDK.BC_OPTION_USE_API, "false");
//        options.put(BlueCatsSDK.BC_OPTION_USE_LOCAL_STORAGE, "false");
//        options.put(BlueCatsSDK.BC_OPTION_MONITOR_BLUE_CATS_REGION_ON_STARTUP, "false");
//        options.put(BlueCatsSDK.BC_OPTION_ENERGY_SAVER_SCAN_STRATEGY, "true"); //be aware of battery
//        options.put("BC_OPTION_SCAN_TIME", "0"); //1 minute, 60*1000, be aware of battery


        BlueCatsSDK.setOptions(options);

        BlueCatsSDK.startPurringWithAppToken(getApplicationContext(), MainActivity.APP_TOKEN);
        BlueCatsSDK.didEnterForeground(); //immediately force foreground although the SDK running in background.

//        BCLogManager.getInstance().setLogLevel(BCLogManager.BC_LOG_TYPE_NETWORK, BCLogManager.BC_LOG_LEVEL_MORE);
//        BCLogManager.getInstance().setLogLevel(BCLogManager.BC_LOG_TYPE_RANGER, BCLogManager.BC_LOG_LEVEL_MORE);
//        BCLogManager.getInstance().setLogLevel(BCLogManager.BC_LOG_TYPE_SCANNER, BCLogManager.BC_LOG_LEVEL_MORE);
        BlueCatsSDK.didEnterForeground();

        mBeaconManager = new BCBeaconManager();
        mBeaconManager.registerCallback(mCallback);

        //the public iBeacon or Eddystone you want to scan
        BCBeaconRegion region = new BCBeaconRegion("050C6308-475C-468A-AFB6-5EED5F798F85", "region1234");
        region.setNamespaceID("73D710F60B044B3C8FF6");
        mBeaconManager.startMonitoringBeaconRegion(region);

        new AsyncTask<Void, Void, Void>() {
            Object lock = new Object();

            @Override
            protected Void doInBackground(Void... voids) {
                //do something or just wait for fun
                //and then stop SDK
                synchronized (lock) {
                    try {
                        lock.wait(MainActivity.SCAN_WINDOW_IN_MS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "time out");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                BlueCatsSDK.stopPurring();
                jobFinished(jobParameters,false);

                //re-schedule the job
                JobScheduler js = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
                js.schedule(MainActivity.getJobInfo(getApplicationContext()));
            }
        }.execute();

        Log.d(TAG, "onStartJob");

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    BCBeaconManagerCallback mCallback = new BCBeaconManagerCallback() {
        @Override
        public void didRangeBeacons(List<BCBeacon> beacons) {
            Log.d(TAG, "didRangeBeacons with "+beacons.size()+" beacons.");
        }
    };
}
