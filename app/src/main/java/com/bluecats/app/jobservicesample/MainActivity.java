package com.bluecats.app.jobservicesample;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private Button btn_start;
    private Button btn_stop;
    private JobScheduler mJobScheduler;

    public static final int MY_JOB_ID = 1111;
    public static final long SCAN_WINDOW_IN_MS = 30*1000;

    public static final String APP_TOKEN = "YOUR_APP_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start = (Button)findViewById(R.id.btn_start);
        btn_stop = (Button)findViewById(R.id.btn_stop);

        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);

        mJobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_start) {
            scheduleJob();
        } else if (view.getId() == R.id.btn_stop) {
            cancelJob();
        }
    }

    private void cancelJob() {
        mJobScheduler.cancel(MY_JOB_ID);
        Log.d(TAG, "Job is canceled.");
    }

    public static JobInfo getJobInfo(Context ctx) {
        JobInfo jobInfo = new JobInfo.Builder(MY_JOB_ID, new ComponentName(ctx, SdkStarterService.class))
                .setMinimumLatency(SCAN_WINDOW_IN_MS)
//                .setPersisted(true) //start job on device reboot
                .setBackoffCriteria(0, JobInfo.BACKOFF_POLICY_LINEAR)
                .build();
        return jobInfo;
    }
    private void scheduleJob() {

        mJobScheduler.schedule(getJobInfo(getApplicationContext()));
        Log.d(TAG, "Job is scheduled.");
    }
}
