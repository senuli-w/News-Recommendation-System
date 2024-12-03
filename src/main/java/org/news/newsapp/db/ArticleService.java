package org.news.newsapp.db;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.news.newsapp.model.Article;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * @Class: ArticleService
 * @Author: Senuli Wickramage
 * @Description:
 * The ArticleService class provides database operations for managing articles in the system.
 * It implements the DatabaseManager interface, defining methods for creating, retrieving, and listing articles.
 * This class interacts with a MongoDB collection to store and retrieve article data.
 */
public class ArticleService extends Database implements DatabaseManager<Article>{
    // MongoDB collection for articles
    private static final MongoCollection<Document> collection = database.getCollection("article");

    /**
     * Creates a new article in the database.
     *
     * @param element The Article object to be created.
     */
    @Override
    public void createNew(Article element) {

        collection.insertOne(element.toDocument());
        System.out.println("New Article " + element.getId());
    }

    /**
     * Retrieves an article from the database using its unique ID.
     *
     * @param uniqueValue The ID of the article as a string.
     * @return The Article object corresponding to the ID.
     */
    @Override
    public Article get(String uniqueValue) {
        int id = Integer.parseInt(uniqueValue);
        Document doc = collection.find(eq("id", id)).first();
        assert doc != null;
        Article article = new Article(
                doc.getInteger("id"),
                doc.getString("title"),
                doc.getString("content") + " " + doc.getString("description"),
                doc.getString("author"),
                doc.getString("imageUrl"),
                doc.getString("dateTime"),
                doc.getString("category")
        );
        return article;
    }

    /**
     * Retrieves all articles from the database.
     *
     * @return A list of all Article objects in the database.
     */
    @Override
    public List<Article> getAll() {
        ArrayList<Article> articles = new ArrayList<>();
        // Iterate over all documents in the collection and convert them into Article objects
        for (Document doc : collection.find()){
            Article article = new Article(
                    (Integer) doc.get("id"),
                    doc.getString("title"),
                    doc.getString("content") + " " + doc.getString("description"),
                    doc.getString("author"),
                    doc.getString("imageUrl"),
                    doc.getString("dateTime"),
                    doc.getString("category")
            );
            articles.add(article);
        }
        return articles;
    }

    /**
     * Retrieves all distinct categories from the articles in the database.
     *
     * @return A list of unique categories.
     */
    public ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();

        for (String category : collection.distinct("category", String.class)) {
            categories.add(category);
        }
        return categories;
    }
}
