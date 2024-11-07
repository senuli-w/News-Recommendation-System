package org.news.newsapp.model;

import java.util.ArrayList;

public class Article {
    String link;
    String headline;
    String category;
    String description;
    String authors;
    String date;

    public Article(String link, String headline, String category, String description, String authors, String date) {
        this.link = link;
        this.headline = headline;
        this.category = category;
        this.description = description;
        this.authors = authors;
        this.date = date;
    }
}
