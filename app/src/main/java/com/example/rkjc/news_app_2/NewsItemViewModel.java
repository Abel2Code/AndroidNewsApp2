package com.example.rkjc.news_app_2;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class NewsItemViewModel extends AndroidViewModel {
    private NewsItemRepository mRepository;
    private LiveData<List<NewsItem>> mAllNewsItem;

    public NewsItemViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NewsItemRepository(application);
        mAllNewsItem = mRepository.getAllNewsItems();
    }

    LiveData<List<NewsItem>> getAllNewsItems() { return mAllNewsItem; }

    public void insert(NewsItem newsItem) { mRepository.insert(newsItem); }
}
