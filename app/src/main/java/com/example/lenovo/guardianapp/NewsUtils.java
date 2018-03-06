package com.example.lenovo.guardianapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class NewsUtils {
    private static final String LOG_TAG = NewsUtils.class.getSimpleName();

    private NewsUtils() {
    }

    public static List<NewsClass> extractNews(String newsJson) {
        URL url = createUrl(newsJson);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP Request.", e);
        }
        List<NewsClass> news = extractFeatureFromJson(jsonResponse);
        return news;
    }

    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem Create The Url", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String JsonResponse = "";
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.connect();
            inputStream = connection.getInputStream();
            JsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return JsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<NewsClass> extractFeatureFromJson(String newsJson) {
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }
        List<NewsClass> news = new ArrayList<>();
        try {
            JSONObject response = new JSONObject(newsJson);
            JSONObject object = response.getJSONObject("response");
            JSONArray newsArray = object.getJSONArray("results");
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNew = newsArray.getJSONObject(i);
                String Title = currentNew.getString("webTitle");
                String Name = currentNew.getString("sectionName");
                String URL = currentNew.getString("webUrl");
                String date = currentNew.getString("webPublicationDate");
                JSONArray nameArray = currentNew.getJSONArray("tags");
                JSONObject auther = nameArray.getJSONObject(0);
                String firstName = auther.getString("firstName");
                String lastName = auther.getString("lastName");
                NewsClass NEW = new NewsClass(Title, Name, URL, firstName + " " + lastName, date);
                news.add(NEW);
            }
        } catch (JSONException e) {
            Log.e("NewsUtils", "Problem parsing the news JSON results", e);
        }
        return news;
    }

    public static List<NewsClass> fetchNewsData(String requestUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<NewsClass> news = extractFeatureFromJson(jsonResponse);
        return news;
    }
}

