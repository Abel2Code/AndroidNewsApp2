package com.example.rkjc.news_app_2.sync;

import android.content.Context;
import android.util.Log;


import com.example.rkjc.news_app_2.MainActivity;
import com.example.rkjc.news_app_2.NetworkUtils;
import com.example.rkjc.news_app_2.NewsItemRepository;

import java.net.URL;

public class NewsSyncTask {


    /**
     * Performs the network request for updated weather, parses the JSON from that request, and
     * inserts the new weather information into our ContentProvider. Will notify the user that new
     * weather has been loaded if the user hasn't been notified of the weather within the last day
     * AND they haven't disabled notifications in the preferences screen.
     *
     * @param context Used to access utility methods and the ContentResolver
     */
    synchronized public static void syncNews(Context context) {

        Log.d("NewsSyncTask", "Syncing");
        try {
            URL url = NetworkUtils.buildUrl();
            String output = NetworkUtils.getResponseFromHttpUrl(url);
            NewsItemRepository.syncDB();
        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }
    }

}