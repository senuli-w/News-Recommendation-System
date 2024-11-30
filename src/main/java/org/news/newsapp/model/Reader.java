package org.news.newsapp.model;


import org.bson.Document;

import java.util.ArrayList;

public class Reader extends User {
    private ArrayList<Article> viewedArticles;
    private ArrayList<Article> ratedArticles;
    private ArrayList<Integer> ratings;
    private ArrayList<String> categories;

    public Reader(String name, String email, String password) {
        super(name, email, password, "READER");
        viewedArticles = new ArrayList<>();
        ratedArticles = new ArrayList<>();
        ratings = new ArrayList<>();
        categories = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Reader{" +
                "viewedArticles=" + viewedArticles +
                ", ratedArticles=" + ratedArticles +
                ", ratings=" + ratings +
                ", categories=" + categories +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public Document toDocument() {
        return new Document("name", this.name)
                .append("email", this.email)
                .append("password", this.password)
                .append("type", this.type)
                .append("viewedArticles", this.viewedArticles)
                .append("ratedArticles", this.ratedArticles)
                .append("ratings", this.ratings)
                .append("categories", this.categories);
    }

    public void addCategory(String category) {
        categories.add(category);
    }

    public ArrayList<Article> getViewedArticles() {
        return viewedArticles;
    }

    public void setViewedArticles(ArrayList<Article> viewedArticles) {
        this.viewedArticles = viewedArticles;
    }

    public ArrayList<Article> getRatedArticles() {
        return ratedArticles;
    }

    public void setRatedArticles(ArrayList<Article> ratedArticles) {
        this.ratedArticles = ratedArticles;
    }

    public ArrayList<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<Integer> ratings) {
        this.ratings = ratings;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
}
