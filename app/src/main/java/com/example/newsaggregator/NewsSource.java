package com.example.newsaggregator;

import java.util.ArrayList;

public class NewsSource {
    private String id;
    private String name;
    private String category;
    private String link;
    private ArrayList<Article> articles = new ArrayList<>();

    public NewsSource(String id, String n, String c, String l){
        setId(id);
        setName(n);
        setCategory(c);
        setLink(l);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getLink() {
        return link;
    }

    public void addArticle(Article a){
        articles.add(a);
    }

    public ArrayList<Article> getArticles(){
        return articles;
    }


    @Override
    public String toString() {
        return name;
    }
}
