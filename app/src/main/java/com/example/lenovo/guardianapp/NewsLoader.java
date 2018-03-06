package com.example.lenovo.guardianapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsClass>> {
    private String mURL;

    public NewsLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsClass> loadInBackground() {
        if (mURL == null) {
            return null;
        }
        List<NewsClass> news = NewsUtils.fetchNewsData(mURL);
        return news;
    }
}
