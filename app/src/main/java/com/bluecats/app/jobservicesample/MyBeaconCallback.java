package com.bluecats.app.jobservicesample;

import com.bluecats.sdk.BCBeacon;
import com.bluecats.sdk.BCBeaconManagerCallback;

import android.content.Context;
import android.util.Log;

import java.util.List;

public class MyBeaconCallback extends BCBeaconManagerCallback {
    private static final String TAG = "MyBeaconCallback";

    /**
     * By defining the Constructor with Context parameter, you'll get the Context
     * @param appct
     */
    public MyBeaconCallback(Context appct) {
        Log.d(TAG, "MyBeaconCallback constructed");
    }

    @Override
    public void didRangeBeacons(List<BCBeacon> beacons) {
        /**
         * do everything you want to do with the ranged beacons.
         * E.g. print a log, or send an Android broadcast, etc.
         * But, just keep in mind that Context.startService(...) is not allowed to call if the app is in background.
         */
        for (BCBeacon item: beacons) {
            Log.d(TAG, "beacon ranged: "+item.getBluetoothAddress()+" mac "+item.getPeripheralIdentifier()+" mp@1m "+/*item.getMeasuredPowerAt1MeterFromAdData() +*/" rssi "+ item.getRSSI() + " dist "+item.getAccuracy());
        }

    }
}
