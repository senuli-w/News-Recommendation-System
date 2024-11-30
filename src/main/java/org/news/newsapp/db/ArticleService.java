package org.news.newsapp.db;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.news.newsapp.model.Article;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ArticleService extends Database implements DatabaseManager<Article>{
    private static final MongoCollection<Document> collection = database.getCollection("article");

    @Override
    public void createNew(Article element) {

        collection.insertOne(element.toDocument());
        System.out.println("New Article " + element.getId());
    }

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

    @Override
    public List<Article> getAll() {
        ArrayList<Article> articles = new ArrayList<>();
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

    public ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();

        for (String category : collection.distinct("category", String.class)) {
            categories.add(category);
        }
        return categories;
    }
}
