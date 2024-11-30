package org.example.diagramnewsrecommendation.db;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.model.Updates;
import org.example.diagramnewsrecommendation.model.Admin;
import org.example.diagramnewsrecommendation.model.Article;
import org.example.diagramnewsrecommendation.model.Reader;
import org.example.diagramnewsrecommendation.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class UserService extends Database implements DatabaseManager<User>{
    private final MongoCollection<Document> collection = database.getCollection("reader");

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

    @Override
    public List<User> getAll() {
        ArrayList<User> users = new ArrayList<>();

        // Limit the query to 20 documents
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

    public boolean isAccountTaken(String email){
        Document doc = collection.find(eq("email", email)).first();
        if (doc == null){
            return false;
        }
        return true;
    }

    public void trackArticleView(String email, Article article){
        Document articleDoc = article.toDocument();

        collection.updateOne(
                eq("email", email), // Find the document by email
                Updates.push("viewedArticles", articleDoc) // Append the article to the array
        );
        System.out.println(email + " read " + article.getId());
    }

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

    public void trackUserCategories(String email, ArrayList<String> categories){
        collection.updateOne(
                eq("email", email), // Find the document by email
                Updates.set("categories", categories) // Append the article to the array
        );
        System.out.println(email + " added categories " + categories);
    }
}
