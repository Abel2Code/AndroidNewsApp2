package com.example.rkjc.news_app_2;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private static final String TAG = "MainActivity";
    protected static String newsSearchResults;
    private static final int LOADER_ID = 1;

    private Toolbar mToolbar;
    protected static ProgressBar mProgressBar;
    protected static TextView mTextView;

    private RecyclerView mRecyclerView;
    private NewsRecyclerViewAdapter mAdapter;
    private ArrayList<NewsItem> newsItems = new ArrayList<>();

    protected static NewsItemViewModel mNewsItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCreateLoader(0 , savedInstanceState);


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
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> newsSearchLoader = loaderManager.getLoader(LOADER_ID);
            if(newsSearchLoader == null){
                loaderManager.initLoader(LOADER_ID, bundle, this).forceLoad();
            }else{
                loaderManager.restartLoader(LOADER_ID, bundle, this).forceLoad();
            }

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
    @Override
    public android.support.v4.content.Loader<String> onCreateLoader(int id, final Bundle args) {
        return new NewsQueryTask(this, args);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<String> loader, String data) {
        Log.d("mycode", data);
        mProgressBar.setVisibility(View.GONE);
        populateWithNewsData(data);
        populateRecyclerView(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<String> loader) {}
}

class NewsQueryTask extends AsyncTaskLoader<String> {
    Bundle args;
    Context context;
    private static final String TAG = "NewsQueryTask";

    public NewsQueryTask(@NonNull Context context, Bundle args) {
        super(context);
        this.context = context;
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading called");
        super.onStartLoading();
        if(args == null){
            Log.d(TAG, "bundle null");
            return;
        }
        MainActivity.mTextView.setText(R.string.waiting_for_search);
        MainActivity.mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public String loadInBackground() {
        Log.d(TAG, "loadInBackground called");
        String output = "";
        try {
            Log.d(TAG, "begin network call");
            output = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl());
            MainActivity.mNewsItemViewModel.insert(JsonUtils.parseNews(output));

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, output);
        return output;
    }
}

