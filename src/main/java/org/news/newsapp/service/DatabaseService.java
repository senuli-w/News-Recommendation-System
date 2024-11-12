package org.news.newsapp.service;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.news.newsapp.model.Article;

import org.bson.Document;
import org.news.newsapp.model.User;

import java.util.ArrayList;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseService {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static User currentUser;

    public static void connect(){
        mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost:27017"));
        database = mongoClient.getDatabase("newsdataset");
    }

    public static ArrayList<Article> getArticles(){
        ArrayList<Article> articles = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("article");

        for (Document doc : collection.find()){
            Article article = new Article(
                    doc.getString("link"),
                    doc.getString("headline"),
                    doc.getString("category"),
                    doc.getString("short_description"),
                    doc.getString("authors"),
                    doc.getString("date")
            );
            articles.add(article);
        }
        return articles;
    }

    public static ArrayList<Article> getArticlesFromCategory(String category) {
        ArrayList<Article> articles = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("article");

        // Find documents where the 'category' field matches the given category
        for (Document doc : collection.find(eq("category", category)).limit(5)) {
            Article article = new Article(
                    doc.getString("link"),
                    doc.getString("headline"),
                    doc.getString("category"),
                    doc.getString("short_description"),
                    doc.getString("authors"),
                    doc.getString("date")
            );
            articles.add(article);
        }
        return articles;
    }


    public static void createAccount(User newUser){
        MongoCollection<Document> collection = database.getCollection("user");
        collection.insertOne(newUser.toDocument());
    }

    public static void loginUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static User getUser(String email){
        MongoCollection<Document> collection = database.getCollection("user");
        Document doc = collection.find(eq("email", email)).first();

        if (doc != null) {
            User newUser = new User(doc.getString("email"),
                    doc.getString("name"),
                    doc.getString("password"));
            return newUser;
        }
        return null;
    }

    public static void deleteAccount(String email){
        MongoCollection<Document> collection = database.getCollection("user");
        collection.deleteOne(eq("email", email));
    }

    public static ArrayList<String> getCategoryList() {
        ArrayList<String> categories = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("article");

        for (String category : collection.distinct("category", String.class)) {
            categories.add(category);
        }
        return categories;
    }
}
