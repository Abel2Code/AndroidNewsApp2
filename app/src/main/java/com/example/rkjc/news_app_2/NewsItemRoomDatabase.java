package com.example.rkjc.news_app_2;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {NewsItem.class}, version = 1)
public abstract class NewsItemRoomDatabase extends RoomDatabase {
    private static volatile NewsItemRoomDatabase INSTANCE;
    public abstract NewsItemDao newsItemDao();

    static NewsItemRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NewsItemRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NewsItemRoomDatabase.class, "news_item")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final NewsItemDao mDao;

        PopulateDbAsync(NewsItemRoomDatabase db) {
            mDao = db.newsItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
