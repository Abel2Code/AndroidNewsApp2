package com.example.rkjc.news_app_2.sync;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.rkjc.news_app_2.R;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;

public class NewsFirebaseJobService extends JobService {
    private static final String CHANNEL_ID = "NewsJobService";
    private AsyncTask<Void, Void, Void> mFetchNewsTask;
    private static final String TAG = "Job Service";
    private static Context context;
    private int idCounter = 0;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Log.d(TAG, "Starting Job");
        mFetchNewsTask = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {

                Context myContext = getApplicationContext();
                context = myContext;
                NewsSyncTask.syncNews(myContext);
                createNotification(myContext);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //  COMPLETED (6) Once the weather data is sync'd, call jobFinished with the appropriate arguements
                jobFinished(jobParameters, false);
            }
        };

        mFetchNewsTask.execute();
        return true;
    }

    private int getNextId(){
        this.idCounter +=1;
        return this.idCounter;
    }

    private void createNotification(Context context){
        createNotificationChannel();
        int notificationId = getNextId();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(dismissAction(context));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(notificationId, mBuilder.build());

    }

    private NotificationCompat.Action dismissAction(Context context) {
        Intent dismissIntent = new Intent(context, DismissIntentService.class);
        dismissIntent.setAction("Dismiss");
        PendingIntent dismissPendingIntent = PendingIntent.getService(
                context,
                0,
                dismissIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Action dismissAction = new NotificationCompat.Action(R.drawable.ic_action_cancel, "Dismiss", dismissPendingIntent);
        return dismissAction;
    }

    public static void deleteNotifications(){
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Stopping Job");
        if (mFetchNewsTask != null) {
            mFetchNewsTask.cancel(true);
        }
        return true;
    }
}
