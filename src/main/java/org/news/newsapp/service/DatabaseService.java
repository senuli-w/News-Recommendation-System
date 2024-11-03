package org.news.newsapp.service;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.news.newsapp.model.Article;

import org.bson.Document;
import org.news.newsapp.model.Source;

import java.util.ArrayList;

public class DatabaseService {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    public static void connect(){
        mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost:27017"));
        database = mongoClient.getDatabase("news");
    }

    public static ArrayList<Article> getArticles(){
        ArrayList<Article> articles = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("article");

        for (Document doc : collection.find()) {
            Source source = new Source(doc.get("source", Document.class).getString("id"),
                    doc.get("source", Document.class).getString("name"));
            Article article = new Article(
                    source,
                    doc.getString("author"),
                    doc.getString("title"),
                    doc.getString("description"),
                    doc.getString("url"),
                    doc.getString("urlToImage"),
                    doc.getString("publishedAt"),
                    doc.getString("content"),
                    (ArrayList<String>) doc.get("keywords")
            );
            articles.add(article);
        }

        return articles;
    }

//    public static void saveArticleToDatabase(Article article) {
//        MongoCollection<Document> collection = database.getCollection("article"); // Change to your collection name
//
//        Document articleDocument = new Document("source", new Document("id", article.getSource().getId())
//                .append("name", article.getSource().getName()))
//                .append("author", article.getAuthor())
//                .append("title", article.getTitle())
//                .append("description", article.getDescription())
//                .append("url", article.getUrl())
//                .append("urlToImage", article.getUrlToImage())
//                .append("publishedAt", article.getPublishedAt())
//                .append("content", article.getContent())
//                .append("keywords", article.getKeywords()); // Assuming you add the getter in Article class
//
//        collection.insertOne(articleDocument);
//    }
}
