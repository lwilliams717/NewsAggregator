package com.example.newsaggregator;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class NewsDownloaderArticle implements Runnable {

    private static final String TAG = "Exception";
    public static MainActivity mainActivity;
    private static NewsSource newsSource;
    private static RequestQueue queue;
    private static String newspaper;
    private static String urlToUse;
    private static final String yourAPIKey = "&apiKey=1638c37da51d499aae182fec6a6ed8ff";
    private static final String urlFirst = "https://newsapi.org/v2/top-headlines?sources=" ;

    NewsDownloaderArticle(MainActivity main, String newsSource) {
        this.mainActivity = main;
        this.newspaper = newsSource;
    }

    @Override
    public void run(){

        String urlToUse = urlFirst + newspaper + yourAPIKey;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent","");
            connection.connect();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                handleResults(null);
                return;
            }

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {
            handleResults(null);
            return;
        }
        handleResults(sb.toString());
    }

    public void handleResults(final String jsonString) {
        final ArrayList<Article> w = parseArticlesJSON(jsonString);
        mainActivity.runOnUiThread(() -> mainActivity.addArticle(w));
    }


    private ArrayList<Article> parseArticlesJSON(String s) {
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray articalArray = jObjMain.getJSONArray("articles");
            ArrayList<Article> articles = new ArrayList<>();

            for (int i = 0; i < articalArray.length(); i++) {
                JSONObject specific_article = (JSONObject) articalArray.get(i);

                //this is to get the news name
                JSONObject source = (JSONObject) specific_article.getJSONObject("source");
                String newsName = source.getString("name");

                //grabs the article and pulls all the information from it
                String author = specific_article.getString("author");

                String title = specific_article.getString("title");
                //this is always empty for some reason so use the content instead
                String description = specific_article.getString("description");

                String url = specific_article.getString("url");

                String urlToImage = specific_article.getString("urlToImage");

                String publishedAt = specific_article.getString("publishedAt");

                String content = specific_article.getString("content");

                Article article = new Article(author,title,description,url,urlToImage,publishedAt,content, newsName);
                articles.add(article);
            }

            return articles;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
