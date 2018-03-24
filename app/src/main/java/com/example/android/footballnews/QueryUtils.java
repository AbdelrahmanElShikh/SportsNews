package com.example.android.footballnews;

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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bodiy_000 on 02-Mar-18.
 */

public final class QueryUtils {
    //private and empty constructor as it will not be inherited .
    private QueryUtils() {
    }

    public static List<News> fetchData(String strUrl) {
        URL url = null;
        String jsonResponse = null;
        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            Log.e(MainActivity.LOG_TAG, "Error Creating URL", e);
        }
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(MainActivity.LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> news = extractFromJsonResponse(jsonResponse);
        return news;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null)
            return jsonResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(MainActivity.LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(MainActivity.LOG_TAG, "Problem retrieving the News JSON results.", e);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = br.readLine();
            while (line != null) {
                output.append(line);
                line = br.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<News> extractFromJsonResponse(String jsonResponse) {
        if (jsonResponse.isEmpty())
            return null;
        ArrayList<News> news = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject current = results.getJSONObject(i);
                String title = current.getString("webTitle");
                String sectionName = current.getString("sectionName");
                String strDate = current.getString("webPublicationDate");
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
                Date date = new Date();
                strDate = dateFormat.format(date);
                String webUrl = current.getString("webUrl");
                String webTitle = title;
                if (title.contains("|"))
                    title = title.substring(0, title.indexOf("|"));
                String autor = "";
                if (webTitle.contains("|"))
                    autor = webTitle.substring(webTitle.indexOf("|") + 1, webTitle.length());
                news.add(new News(title, sectionName, strDate, webUrl, autor));
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the News JSON results", e);
        }
        return news;
    }
}
