package com.example.newsaggregator;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class NewsDownloader {
    private static MainActivity mainActivity;
    private static RequestQueue queue;
    private static String newspaper;
    private static String urlToUse;
    private static final String yourAPIKey = "1638c37da51d499aae182fec6a6ed8ff";
    private static final String newsLink = "https://newsapi.org/v2/sources?apiKey=";
    private static final String newsHeadline = "https://newsapi.org/v2/top-headlines?sources=";
    private static final String getNewsHeadline_end = "&apiKey=";


    public static void NewsDownloader(MainActivity main, String news){
        mainActivity = main;
        queue = Volley.newRequestQueue(mainActivity);
        newspaper = news;

        Response.Listener<JSONObject> listener =
                response -> parseJSON_Main(response.toString());

        Response.ErrorListener error =
                error1 -> mainActivity.ErrorDownload();

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private static void parseJSON_Main(String toString) {

    }
}
