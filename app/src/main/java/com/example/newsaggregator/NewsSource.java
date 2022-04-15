package com.example.newsaggregator;

public class NewsSource {
    private String name;
    private String category;
    private String link;

    public NewsSource(String n, String c, String l){
        setName(n);
        setCategory(c);
        setLink(l);
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

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return name;
    }
}
