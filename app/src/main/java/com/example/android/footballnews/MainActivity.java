package com.example.android.footballnews;

import android.app.LoaderManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    public static final String THE_GUARDIAN_URL = "http://content.guardianapis.com/search?q=sport&api-key=test";
    public static final String LOG_TAG = MainActivity.class.getName();
    private NewsAdapter newsAdapter;
    private TextView emptyText;
    private RecyclerView newsRecycleView;
    private static final int LOADER_ID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRecycleView = findViewById(R.id.list);
        newsAdapter = new NewsAdapter(getApplicationContext(), new ArrayList<News>());
        newsRecycleView.setAdapter(newsAdapter);
        emptyText = findViewById(R.id.empty);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            View loadingIndecator = findViewById(R.id.loading);
            loadingIndecator.setVisibility(View.GONE);
            emptyText.setText(R.string.internerError);
        }
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        newsRecycleView.setLayoutManager(llm);
        newsRecycleView.setAdapter(newsAdapter);
    }

    @Override
    public android.content.Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, THE_GUARDIAN_URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<News>> loader, List<News> news) {
        View loadingIndecator = findViewById(R.id.loading);
        loadingIndecator.setVisibility(View.GONE);
        emptyText.setText(R.string.emptyText);
        emptyText.setVisibility(View.VISIBLE);
        newsAdapter.clear();
        newsAdapter.swap(news);
        if (newsAdapter.getNewsList().size() > 0)
            emptyText.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<News>> loader) {
        newsAdapter.clear();
    }
}
