package com.example.rkjc.news_app_2;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

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

}
