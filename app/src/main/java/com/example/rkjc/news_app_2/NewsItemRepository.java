package com.example.rkjc.news_app_2;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class NewsItemRepository {
    private NewsItemDao mNewsItemDao;
    private LiveData<List<NewsItem>> mAllNewsItems;

    NewsItemRepository(Application application) {
        NewsItemRoomDatabase db = NewsItemRoomDatabase.getDatabase(application);
        mNewsItemDao = db.newsItemDao();
        mAllNewsItems = mNewsItemDao.loadAllNewsItems();
    }

    LiveData<List<NewsItem>> getAllNewsItems() {
        return mAllNewsItems;
    }

    public void insert (List<NewsItem> newsItems) {
        new insertAsyncTask(mNewsItemDao).execute(newsItems);
    }

    public void clear () {
//        new clearAsyncTask(mNewsItemDao).execute();
    }

    private static class insertAsyncTask extends AsyncTask<List<NewsItem>, Void, Void> {

        private NewsItemDao mAsyncTaskDao;

        insertAsyncTask(NewsItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(List<NewsItem>... newsItems) {
            List<NewsItem> items = newsItems[0];
            mAsyncTaskDao.insert(items);
            return null;
        }
    }

//    private static class clearAsyncTask extends AsyncTask<NewsItemDao, Void, Void> {
//
//        private NewsItemDao mAsyncTaskDao;
//
//        clearAsyncTask(NewsItemDao dao) {
//            mAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(NewsItemDao... dao) {
//            dao[0].deleteAll();
//            new insertAsyncTask(dao[0]).execute();
//            return null;
//        }
//    }


}
