package org.news.newsapp.model;

import org.news.newsapp.service.DatabaseService;

import java.util.ArrayList;

public class NormalUser extends User {
    private ArrayList<Category> categories;
    private ArrayList<String> viewedArticles;
    private ArrayList<String> ratedArticles;
    private ArrayList<Integer> ratings;

    public NormalUser(){
        viewedArticles = new ArrayList<>();
        ratedArticles = new ArrayList<>();
        ratings = new ArrayList<>();
        categories = new ArrayList<>();
    }

    public NormalUser(String name, String email, String password){
        super(name, email, password);
        viewedArticles = new ArrayList<>();
        ratedArticles = new ArrayList<>();
        ratings = new ArrayList<>();
        categories = new ArrayList<>();
    }

    public void addCategory(String category){
        categories.add(Category.valueOf(category.toUpperCase()));
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public ArrayList<String> getViewedArticles() {
        return viewedArticles;
    }

    public ArrayList<String> getRatedArticles() {
        return ratedArticles;
    }

    public ArrayList<Integer> getRatings() {
        return ratings;
    }

}
