package com.example.rkjc.news_app_2.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class NewsSyncIntentService extends IntentService {
    private static final String TAG = "NEWS_SYNC_INTENT_SERVICE";
    public NewsSyncIntentService() {
        super("NewsSyncIntentService");
        Log.d(TAG, "Creating Intent Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Handling with Intent Service");
        NewsSyncTask.syncNews(this);
    }
}
