package com.example.newsaggregator;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewsDownloaderSource {
    private static final String TAG = "Exception";
    public static MainActivity mainActivity;
    private static RequestQueue queue;
    private static String newspaper;
    private static String urlToUse;
    private static final String yourAPIKey = "810dcf333dbe462283464e599d59d31d";
    private static final String newsLink = "https://newsapi.org/v2/sources?apiKey=";
    private static final String newsHeadline = "https://newsapi.org/v2/top-headlines?sources=";
    private static final String getNewsHeadline_end = "&apiKey=";
    private static boolean all = false;

    public static void NewsDownloader(MainActivity main, String source){
        mainActivity = main;
        queue = Volley.newRequestQueue(mainActivity);
        if(source.equals("All")){
            all = true;
        }
        else{
            newspaper = source;
        }

        urlToUse = newsLink + yourAPIKey;

        Response.Listener<JSONObject> listener =
                response -> parseJSON_Main(response.toString());

        Response.ErrorListener error =
                error1 -> mainActivity.ErrorDownload();

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "");
                        return headers;
                    }
                };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    //downloader specifically searches for all topics and loads in the dynamic menu
    //only called at the beginning
    public static void NewsDownloaderTopics(MainActivity main){
        mainActivity = main;
        queue = Volley.newRequestQueue(mainActivity);

        urlToUse = newsLink + yourAPIKey;

        Response.Listener<JSONObject> listener =
                response -> parseTopics(response.toString());

        Response.ErrorListener error =
                error1 -> mainActivity.ErrorDownload();

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "val");
                        return headers;
                    }
                };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    //this one find a specific sources based on a selected topic
    private static void parseJSON_Main(String s) {
        try {
            MainActivity.current_items.clear();
            JSONObject jMain = new JSONObject(s);
            //sources is an array
            JSONArray sources = jMain.getJSONArray("sources");
            //loops thru each source
            for(int i = 0; i < sources.length(); i++) {
                //grab the json objects
                JSONObject specificSource = sources.getJSONObject(i);
                //gets the name category and link
                String id = specificSource.getString("id");
                String name = specificSource.getString("name");
                String topic = specificSource.getString("category");
                String url = specificSource.getString("url");

                //adds the news source to item array for the drawer ONLY if the category matches
                if(all){
                    MainActivity.current_items.add(new NewsSource(id, name, topic, url));
                }
                else if(topic.equals(newspaper)) {
                    MainActivity.current_items.add(new NewsSource(id, name, topic, url));
                }

            }
            mainActivity.loadDrawer(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //method goes thru all the sources to find topics to add to the menu
    private static void parseTopics(String s) {
        try {
            MainActivity.topics.clear();
            //make sure to add the ALL topic first
            MainActivity.topics.add("All");
            JSONObject jMain = new JSONObject(s);
            //sources is an array
            JSONArray sources = jMain.getJSONArray("sources");
            //loops thru each source
            for(int i = 0; i < sources.length(); i++) {
                //grab the json objects
                JSONObject specificSource = sources.getJSONObject(i);
                //gets the name category and link
                String id = specificSource.getString("id");
                String name = specificSource.getString("name");
                String topic = specificSource.getString("category");
                String url = specificSource.getString("url");

                //adds the news source to item array for the drawer
                MainActivity.all_items.add(new NewsSource(id, name, topic, url));

                //adds category to topic array for the menu
                MainActivity.addTopic(topic);
            }
            mainActivity.loadDrawer(true);
            mainActivity.makeMenu();
            if(MainActivity.loadedTitle == null){
                mainActivity.changeTitle(sources.length());
            }
            else{
                Log.d(TAG, "parseTopics: " + MainActivity.current_items.size());
                mainActivity.changeTitle(MainActivity.loadedTitle);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
