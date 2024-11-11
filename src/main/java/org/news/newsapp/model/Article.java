package org.news.newsapp.model;

import java.util.ArrayList;

public class Article {
    private String link;
    private String headline;
    private String category;
    private String description;
    private String authors;
    private String date;

    public Article(String link, String headline, String category, String description, String authors, String date) {
        this.link = link;
        this.headline = headline;
        this.category = category;
        this.description = description;
        this.authors = authors;
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
