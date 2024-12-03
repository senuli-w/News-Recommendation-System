package org.news.newsapp.db;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.model.Updates;
import org.news.newsapp.model.Admin;
import org.news.newsapp.model.Article;
import org.news.newsapp.model.Reader;
import org.news.newsapp.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * @Class: UserService
 * @Author: Senuli Wickramage
 * @Description:
 * UserService is responsible for managing user-related operations in the database.
 * It includes creating new users, retrieving users, updating user information,
 * and tracking user activity (such as viewed articles, ratings, and categories).
 */
public class UserService extends Database implements DatabaseManager<User>{
    // MongoDB collection for users
    private final MongoCollection<Document> collection = database.getCollection("reader");

    /**
     * Creates a new user in the database.
     * The user is inserted based on their type (Admin or Reader).
     *
     * @param element The user to be added (Admin or Reader).
     */
    @Override
    public void createNew(User element) {
        if (element.getType().equals("ADMIN")) {
            Admin admin = (Admin) element;
            collection.insertOne(admin.toDocument());
        } else {
            Reader reader = (Reader) element;
            collection.insertOne(reader.toDocument());
        }
    }

    /**
     * Retrieves a user by their unique email.
     *
     * @param uniqueValue The unique email of the user.
     * @return The user corresponding to the provided email, or null if not found.
     */
    @Override
    public User get(String uniqueValue) {
        Document doc = collection.find(eq("email", uniqueValue)).first();

        if (doc != null) {
            String type = doc.getString("type");

            if ("reader".equalsIgnoreCase(type)) {
                Reader reader = new Reader(doc.getString("name"),
                        doc.getString("email"),
                        doc.getString("password"));
                reader.setViewedArticles((ArrayList<Article>) doc.get("viewedArticles"));
                reader.setRatedArticles((ArrayList<Article>) doc.get("ratedArticles"));
                reader.setRatings((ArrayList<Integer>) doc.get("ratings"));
                reader.setCategories((ArrayList<String>) doc.get("categories"));
                return reader;
            } else if ("admin".equalsIgnoreCase(type)) {
                return new Admin (
                        doc.getString("name"),
                        doc.getString("email"),
                        doc.getString("password")
                );
            }
        }
        return null;
    }

    /**
     * Retrieves all users (both Admin and Reader) from the database.
     *
     * @return A list of all users.
     */
    @Override
    public List<User> getAll() {
        ArrayList<User> users = new ArrayList<>();

        FindIterable<Document> docs = collection.find();

        for (Document doc : docs) {
            String type = doc.getString("type");

            if ("reader".equalsIgnoreCase(type)) {
                Reader reader = new Reader(
                        doc.getString("name"),
                        doc.getString("email"),
                        doc.getString("password")
                );
                reader.setViewedArticles((ArrayList<Article>) doc.get("viewedArticles"));
                reader.setRatedArticles((ArrayList<Article>) doc.get("ratedArticles"));
                reader.setRatings((ArrayList<Integer>) doc.get("ratings"));
                reader.setCategories((ArrayList<String>) doc.get("categories"));
                users.add(reader);
            } else if ("admin".equalsIgnoreCase(type)) {
                Admin admin = new Admin(
                        doc.getString("name"),
                        doc.getString("email"),
                        doc.getString("password")
                );
                users.add(admin);
            }
        }

        return users;
    }

    /**
     * Updates a user's information (name, email, and/or password) in the database.
     *
     * @param name The new name (optional).
     * @param email The new email (optional).
     * @param password The new password (optional).
     */
    public void update(String name, String email, String password) {
        // Create a filter to find the document by email
        Document filter = new Document("email", email);

        // Create an update document with the new values for name, email, and password
        Document update = new Document();

        // Directly set the fields to be updated
        if (name != null) {
            update.append("name", name);
        }
        if (email != null) {
            update.append("email", email);
        }
        if (password != null) {
            update.append("password", password);
        }

        // Perform the update operation on the collection
        if (!update.isEmpty()) {
            collection.updateOne(filter, new Document("$set", update));
            System.out.println("Account updated for email: " + email);
        } else {
            System.out.println("No fields to update for email: " + email);
        }
        System.out.println("Update done DB93");
    }

    /**
     * Checks if an account already exists with the given email.
     *
     * @param email The email to check.
     * @return true if the account exists, false otherwise.
     */
    public boolean isAccountTaken(String email){
        Document doc = collection.find(eq("email", email)).first();
        if (doc == null){
            return false;
        }
        return true;
    }

    /**
     * Tracks an article viewed by the user by appending the article to the user's viewed articles.
     *
     * @param email The email of the user.
     * @param article The article that the user has viewed.
     */
    public void trackArticleView(String email, Article article){
        Document articleDoc = article.toDocument();

        collection.updateOne(
                eq("email", email), // Find the document by email
                Updates.push("viewedArticles", articleDoc) // Append the article to the array
        );
        System.out.println(email + " read " + article.getId());
    }

    /**
     * Tracks an article rating by the user and appends the rating to the user's ratings.
     *
     * @param email The email of the user.
     * @param article The article that the user rated.
     * @param rating The rating provided by the user (1-5).
     */
    public void trackArticleRatings(String email, Article article, int rating) {
        Document articleDoc = article.toDocument();

        collection.updateOne(
                eq("email", email), // Find the document by email
                Updates.push("ratedArticle", articleDoc) // Append the article to the array
        );

        collection.updateOne(
                eq("email", email),
                Updates.push("rating", rating) // update the rating
        );

        System.out.println("Reader " + email + " rated article " + article.getId() + " - " + rating);
    }

    /**
     * Tracks the categories that a user is interested in and updates the categories in the user's profile.
     *
     * @param email The email of the user.
     * @param categories The list of categories the user is interested in.
     */
    public void trackUserCategories(String email, ArrayList<String> categories){
        collection.updateOne(
                eq("email", email), // Find the document by email
                Updates.set("categories", categories) // Append the article to the array
        );
        System.out.println(email + " added categories " + categories);
    }
}
