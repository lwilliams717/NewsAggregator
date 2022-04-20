package com.example.newsaggregator;

import android.annotation.SuppressLint;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Article {
    private String newsName;
    private String author;
    private String title;
    private String desc;
    private String url;
    private String urlToImage;
    private String date;
    private String content;

    public Article(String a, String t, String d, String url, String url_i, String date, String c, String newsName){
        this.newsName = newsName;
        setAuthor(a);
        setTitle(t);
        setDesc(d);
        setUrl(url);
        setUrlToImage(url_i);
        setDate(date);
        setContent(c);
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public void setAuthor(String author) {
        if(author.equals("null")){
            this.author = newsName;
        }
        else{
            this.author = author;
        }

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        if(author.equals("null")){
            this.desc = "No description available";
        }
        else{
            this.desc = desc;
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        if(content.equals("null")){
            this.content = "";
        }
        else {
            this.content = content;
        }
    }

    @SuppressLint("NewApi")
    public String dateTimeZulu() {
        try {
            DateTimeFormatter parser = DateTimeFormatter.ISO_DATE_TIME;
            Instant instant = parser.parse(this.getDate(), Instant::from);
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM d, yyyy hh:mm a");

            return ldt.format(dtf);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public String toString() {
        return "Article{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
