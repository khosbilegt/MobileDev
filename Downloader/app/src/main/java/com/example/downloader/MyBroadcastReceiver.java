package com.example.downloader;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.Calendar;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private JobScheduler mScheduler;
    private static final int JOB_ID = 0;
    private static final String PREFERENCE = "BroadcastPreference";

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("On Receive");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        if(sharedPref.getInt("day", 0) == day) {
            return;
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("day", day);
        editor.commit();
        mScheduler = (JobScheduler) context.getSystemService(context.JOB_SCHEDULER_SERVICE);

        ComponentName serviceName = new ComponentName(context.getPackageName(),
                NotificationJobService.class.getName());

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresCharging(true);

        JobInfo myJobInfo = builder.build();
        mScheduler.schedule(myJobInfo);
        Toast.makeText(context, "Job Scheduled, job will run when " +
                "the constraints are met.", Toast.LENGTH_SHORT).show();
    }
}
