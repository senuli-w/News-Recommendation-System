package org.news.newsapp.model;

import java.util.ArrayList;

public class NormalUser extends User {
    private ArrayList<Category> categories;
    private ArrayList<String> viewedArticles;
    private ArrayList<String> ratedArticles;
    private ArrayList<Integer> ratings;

    public NormalUser(){
        categories = new ArrayList<>();
    }

    public NormalUser(String name, String email, String password){
        super(name, email, password);
    }

    public void addCategory(String category){
        if (categories == null){
            categories = new ArrayList<>();
        }
        categories.add(Category.valueOf(category.toUpperCase()));
    }
}
