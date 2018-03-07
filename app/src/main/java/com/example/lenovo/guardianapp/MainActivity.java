package com.example.lenovo.guardianapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsClass>> {
    private TextView mEmptyStateTextVeiw;
    private static final String URL = "https://content.guardianapis.com/search?&show-tags=contributor&api-key=test";
    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView newsList = (ListView) findViewById(R.id.list_item);
        adapter = new NewsAdapter(this, new ArrayList<NewsClass>());
        newsList.setAdapter(adapter);
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsClass currentNew = adapter.getItem(position);
                Uri newUri = Uri.parse(currentNew.getNUri());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newUri);
                startActivity(websiteIntent);
            }
        });
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected == true) {
            mEmptyStateTextVeiw = (TextView) findViewById(R.id.empty_view);
            newsList.setEmptyView(mEmptyStateTextVeiw);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
            onResume();
        } else {
            View LoadingIndicator = findViewById(R.id.loading_spinner);
            LoadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextVeiw = (TextView) findViewById(R.id.empty_view);
            mEmptyStateTextVeiw.setText(R.string.no_connection);
        }
    }


    @Override
    public Loader<List<NewsClass>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sectionName = sharedPreferences.getString(
                getString(R.string.category_setting_key),
                getString(R.string.category_setting_default)
        );
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        Uri baseuri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseuri.buildUpon();
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("pageSize", "10");
        uriBuilder.appendQueryParameter("q", sectionName);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    protected void onResume() {
        onRestart();
        super.onResume();
    }

    @Override
    public void onLoadFinished(Loader<List<NewsClass>> loader, List<NewsClass> News) {
        View LoadingIndicator = findViewById(R.id.loading_spinner);
        LoadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextVeiw.setText(R.string.no_news);
        adapter.clear();
        if (News != null && !News.isEmpty()) {
            adapter.addAll(News);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsClass>> loader) {
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

