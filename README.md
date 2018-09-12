# JobServiceSample

## branch: background-scan
In this branch, the source code demonstrates how to keep listening to BCBeaconMangerCallback when there is no visible activities or forground services are running.

### In brief, here are the steps:
* Create a class extending BCBeaconManagerCallback. If your class requires Context, just add a contructor with Context parameter for your class.
```java
    public MyBeaconCallback(Context appct) {
        Log.d(TAG, "MyBeaconCallback constructed");
    }
```
* Designate the class name in BlueCatsSDK optoin
```java
        options.put(BlueCatsSDK.BC_OPTION_BEACONMANAGER_CALLBACK_CLASS, MyBeaconCallback.class.getCanonicalName());

```
* Call BlueCatsSDK.startPurringWithApptoken(....) as usual
```java
        BlueCatsSDK.setOptions(options);
        BlueCatsSDK.startPurringWithAppToken(getApplicationContext(), MainActivity.APP_TOKEN);
        BlueCatsSDK.didEnterForeground();
```
* Get beacons list from the callback class
```java
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
```

## How to unregister the background callback class
Once the background callback class is registered into BlueCatsSDK, the name of this class will be save in the SharedPreference. Here is the method to unregister it:
Call BlueCatsSDK.stopPurring() which will stop the running JobService and cancel job from JobScheduler, and then re-start SDK by passing an empty background callback class name
```java
    public void unregisterCallback() {
        BlueCatsSDK.stopPurring();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startSDKPlain(getApplicationContext(), null);
            }
        }, 1000);
    }

...     
options.put(BlueCatsSDK.BC_OPTION_BEACONMANAGER_CALLBACK_CLASS, ""); //unregister the callback class in this way
...
```