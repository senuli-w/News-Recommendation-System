package org.news.newsapp;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.news.newsapp.models.Article;

import org.bson.Document;

public class DatabaseConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    public static void connect(){
        mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost:27017"));
        database = mongoClient.getDatabase("news");
    }

    public static void saveArticleToDatabase(Article article) {
        MongoCollection<Document> collection = database.getCollection("article"); // Change to your collection name

        Document articleDocument = new Document("source", new Document("id", article.getSource().getId())
                .append("name", article.getSource().getName()))
                .append("author", article.getAuthor())
                .append("title", article.getTitle())
                .append("description", article.getDescription())
                .append("url", article.getUrl())
                .append("urlToImage", article.getUrlToImage())
                .append("publishedAt", article.getPublishedAt())
                .append("content", article.getContent())
                .append("keywords", article.getKeywords()); // Assuming you add the getter in Article class

        collection.insertOne(articleDocument);
    }
}
