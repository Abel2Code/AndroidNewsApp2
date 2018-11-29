package com.example.rkjc.news_app_2.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class DismissIntentService extends IntentService {
    public DismissIntentService() {
        super("DismissIntentService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NewsFirebaseJobService.deleteNotifications();
    }
}
