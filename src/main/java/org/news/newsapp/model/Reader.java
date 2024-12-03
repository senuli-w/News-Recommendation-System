package org.news.newsapp.model;

import org.bson.Document;

import java.util.ArrayList;

/**
 * @Class: Reader
 * @Author: Senuli Wickramage
 * @Description:
 * The Reader class represents a reader user in the application. It extends the User class and contains additional properties
 * specific to a reader, such as the list of viewed articles, rated articles, ratings given, and categories of interest.
 * The class provides methods for managing these properties, adding categories, and converting the reader object to a MongoDB-compatible Document.
 */
public class Reader extends User {
    private ArrayList<Article> viewedArticles; // List of articles viewed by the reader
    private ArrayList<Article> ratedArticles; // List of articles rated by the reader
    private ArrayList<Integer> ratings; // List of ratings given by the reader
    private ArrayList<String> categories; // List of categories the reader is interested in

    /**
     * Constructor to initialize a Reader object with the given name, email, and password.
     * Initializes the lists for viewed articles, rated articles, ratings, and categories.
     *
     * @param name The name of the reader.
     * @param email The email of the reader.
     * @param password The password of the reader.
     */
    public Reader(String name, String email, String password) {
        super(name, email, password, "READER");
        viewedArticles = new ArrayList<>();
        ratedArticles = new ArrayList<>();
        ratings = new ArrayList<>();
        categories = new ArrayList<>();
    }

    /**
     * Converts the Reader object to a string representation for easy display or debugging.
     *
     * @return A string representing the Reader object with all its properties.
     */
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

    /**
     * Converts the Reader object into a MongoDB-compatible Document for storage.
     *
     * @return A Document representation of the Reader object with all its properties.
     */
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

    /**
     * Adds a category to the list of categories that the reader is interested in.
     *
     * @param category The category to be added.
     */
    public void addCategory(String category) {
        categories.add(category);
    }

    // Getter and Setter Methods
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
