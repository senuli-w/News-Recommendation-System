package org.news.newsapp.model;

import org.bson.Document;

import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private String password;
    private ArrayList<String> viewedArticles;
    private ArrayList<String> ratedArticles;
    private ArrayList<Integer> ratedValues;

    public User(){}

    public User(String name,String email, String password) {
        setName(name);
        setEmail(email);
        this.password = password;
    }

    public Document toDocument() {
        return new Document("name", this.name)
                .append("email", this.email)
                .append("password", this.password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static boolean validateEmail(String email){
        if (email.trim().isEmpty()) {return false;}
        if (email.contains(" ")) {return false;}
        if (email.split("@").length != 2){return false;}
        if (!email.split("@")[1].contains(".")){return false;}
        return true;
    }

    public String getPassword() {
        return password;
    }

    public boolean setPassword(String password) {
        if (password.trim().isEmpty()){return false;}
        this.password = password;
        return true;
    }

    public ArrayList<String> getViewedArticles() {
        return viewedArticles;
    }

    public void setViewedArticles(ArrayList<String> viewedArticles) {
        this.viewedArticles = viewedArticles;
    }

    public ArrayList<String> getRatedArticles() {
        return ratedArticles;
    }

    public void setRatedArticles(ArrayList<String> ratedArticles) {
        this.ratedArticles = ratedArticles;
    }

    public ArrayList<Integer> getRatedValues() {
        return ratedValues;
    }

    public void setRatedValues(ArrayList<Integer> ratedValues) {
        this.ratedValues = ratedValues;
    }
}
