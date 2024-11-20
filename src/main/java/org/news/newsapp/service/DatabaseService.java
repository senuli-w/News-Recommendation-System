package org.news.newsapp.service;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.types.ObjectId;
import org.news.newsapp.model.Article;

import org.bson.Document;
import org.news.newsapp.model.NormalUser;
import org.news.newsapp.model.User;

import java.util.ArrayList;

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
        MongoCollection<Document> collection = database.getCollection("articles");

        for (Document doc : collection.find()){
            Article article = new Article(
                    doc.getString("url"),
                    doc.getString("title"),
                    doc.getString("description"),
                    doc.getString("keyword"),
                    doc.getString("content"),
                    doc.getString("author"),
                    doc.getString("publishedAt"),
                    doc.getString("urlToImage")
            );
            article.setId(doc.getObjectId("_id").toString());
            articles.add(article);
        }
        return articles;
    }

    public static ArrayList<Article> getArticlesFromCategory(String keyword) {
        ArrayList<Article> articles = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("articles");

        // Find documents where the 'keyword' field matches the given keyword
        for (Document doc : collection.find(eq("keyword", keyword)).limit(5)) {
            Article article = new Article(
                    doc.getString("url"),
                    doc.getString("title"),
                    doc.getString("description"),
                    doc.getString("keyword"),
                    doc.getString("content"),
                    doc.getString("author"),
                    doc.getString("publishedAt"),
                    doc.getString("urlToImage")
            );
            article.setId(doc.getObjectId("_id").toString());
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

    public static void trackArticleView(String email, String articleId) {
        MongoCollection<Document> collection = database.getCollection("userViewArticle");

        // Check if the document already exists
        boolean exists = collection.find(Filters.and(
                Filters.eq("userEmail", email),
                Filters.eq("articleId", new ObjectId(articleId))
        )).first() != null;

        // If the document doesn't exist, insert it
        if (!exists) {
            Document viewRecord = new Document("userEmail", email)  // User's email
                    .append("articleId", new ObjectId(articleId));  // Article's ObjectId

            collection.insertOne(viewRecord);  // Insert the view record into the collection
            System.out.println("View record added: " + viewRecord);
        } else {
            System.out.println("View record already exists for email: " + email + " and articleId: " + articleId);
        }
    }

    public static void trackArticleRatings(String email, String articleId, int rating) {
        // Get the collection
        MongoCollection<Document> collection = database.getCollection("userLikeArticle");

        // Create the filter to find the document
        Document filter = new Document("email", email).append("articleId", articleId);

        // Create the update operation to set the rating
        Document update = new Document("$set", new Document("rating", rating));

        // Use the upsert option to insert if it doesn't exist
        UpdateOptions options = new UpdateOptions().upsert(true);

        // Perform the update or insert operation
        collection.updateOne(filter, update, options);
    }


    public static User getCurrentUser() {
        return currentUser;
    }

    public static User getUser(String email){
        MongoCollection<Document> collection = database.getCollection("user");
        Document doc = collection.find(eq("email", email)).first();

        if (doc != null) {
            if (doc.containsKey("")){}
            return new NormalUser(doc.getString("name"),
                    doc.getString("email"),
                    doc.getString("password"));
        }
        return null;
    }

    public static Article getArticle(String id) {
        MongoCollection<Document> collection = database.getCollection("articles");

        try {
            // Find the document by _id
            Document doc = collection.find(eq("_id", new ObjectId(id))).first();

            if (doc != null) {
                // Create an Article object using the document's fields
                Article article = new Article(
                        doc.getString("url"),
                        doc.getString("title"),
                        doc.getString("description"),
                        doc.getString("keyword"),
                        doc.getString("content"),
                        doc.getString("author"),
                        doc.getString("publishedAt"),
                        doc.getString("urlToImage")
                );
                article.setId(doc.getObjectId("_id").toString());
                return article;
            }
        } catch (IllegalArgumentException e) {
            // Handle invalid ObjectId formats
            System.err.println("Invalid ObjectId format: " + id);
        } catch (Exception e) {
            // Handle other exceptions
            System.out.println(e);
        }

        return null; // Return null if not found or an exception occurs
    }

    public static boolean userExists(String email){
        MongoCollection<Document> collection = database.getCollection("user");
        Document doc = collection.find(eq("email", email)).first();
        if (doc == null){
            return false;
        }
        return true;
    }

    public static void deleteAccount(String email){
        MongoCollection<Document> collection = database.getCollection("user");
        collection.deleteOne(eq("email", email));
    }

    public static ArrayList<String> getCategoryList() {
        ArrayList<String> categories = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("articles");

        for (String category : collection.distinct("keyword", String.class)) {
            categories.add(category);
        }
        return categories;
    }
}
