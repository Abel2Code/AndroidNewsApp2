package com.example.rkjc.news_app_2;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rkjc.news_app_2.sync.NewsSyncUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    protected static String newsSearchResults;
    private static final int LOADER_ID = 1;

    private Toolbar mToolbar;
    protected static ProgressBar mProgressBar;
    protected static TextView mTextView;

    private RecyclerView mRecyclerView;
    private NewsRecyclerViewAdapter mAdapter;
    private ArrayList<NewsItem> newsItems = new ArrayList<>();

    public static NewsItemViewModel mNewsItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = (RecyclerView) findViewById(R.id.news_recyclerview);
        mAdapter = new NewsRecyclerViewAdapter(this, newsItems);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNewsItemViewModel = ViewModelProviders.of(this).get(NewsItemViewModel.class);
        mNewsItemViewModel.getAllNewsItems().observe(this, new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(@Nullable final List<NewsItem> newsItems) {
                // Update the cached copy of the words in the adapter.
                mAdapter.setNewsItems((ArrayList<NewsItem>) newsItems);
                mAdapter.notifyDataSetChanged();
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        setSupportActionBar(mToolbar);

        mTextView = (TextView) findViewById(R.id.queryJSON);
        NewsSyncUtils.initialize(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Bundle bundle = new Bundle();
            mNewsItemViewModel.sync();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private URL makeSearchUrl() {
        URL newsSearchURL = NetworkUtils.buildUrl();
        return newsSearchURL;
    }

    private void populateWithNewsData(String data){
        mTextView.setText(data);
    }
    private void populateRecyclerView(String searchResults){
        newsItems = JsonUtils.parseNews(searchResults);
        mAdapter.mNewsItems.addAll(newsItems);
        mAdapter.notifyDataSetChanged();

    }
}

