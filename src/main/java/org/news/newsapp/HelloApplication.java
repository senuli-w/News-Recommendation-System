package org.news.newsapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.news.newsapp.service.DatabaseService;
import org.news.newsapp.util.RecommendationEngine;

public class HelloApplication extends Application {

    private static HttpURLConnection connection;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("signup.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1390, 780);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DatabaseService.connect();
//        launch();
        RecommendationEngine recommendationEngine = new RecommendationEngine();
        recommendationEngine.run();
    }

//    public static void parseJson(String s, String mainKeyword) throws IOException {
//        JSONObject jsonObject = new JSONObject(s);
//        System.out.println(jsonObject);
//
//        JSONArray articles = jsonObject.getJSONArray("articles");
//
//        ArrayList<Article> articleList = new ArrayList<>();
//
//        for (int i = 0; i < 20; i++) {
//            JSONObject articleJson = articles.getJSONObject(i);
//
//            // Check if 'source' is not null
//            JSONObject sourceJson = articleJson.optJSONObject("source");
//            Source source = null;
//            if (sourceJson != null) {
//                String id = sourceJson.optString("id", "nan"); // Get id safely
//                String name = sourceJson.optString("name", "nan"); // Default name if null
//                source = new Source(id, name);
//            }
//
//            // Create an ArrayList with just the mainKeyword
//            ArrayList<String> keywords = new ArrayList<>();
//            keywords.add(mainKeyword); // Add mainKeyword to the keywords list
//
//            // Create Article object
//            Article article = new Article(
//                    source,
//                    articleJson.optString("author", "nan"),
//                    articleJson.optString("title", "nan"),
//                    articleJson.optString("description", "nan"),
//                    articleJson.optString("url", "nan"),
//                    articleJson.optString("urlToImage", "nan"),
//                    articleJson.optString("publishedAt", "nan"),
//                    articleJson.optString("content", "nan"),
//                    keywords
//            );
//            articleList.add(article);
//            DatabaseService.saveArticleToDatabase(article);
//        }
//        System.out.println(articleList.size());
//    }
}

/*
package org.news.newsapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class HelloApplication {
    private static HttpURLConnection connection;

    public static void main(String[] args) {
        String key = "Entertainment";
        getApiData(key);
        System.out.println("Data insertion complete for keyword: " + key);
    }

    public static void getApiData(String mainKeyword) {
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        String urlString = "https://newsapi.org/v2/everything?q=" + mainKeyword + "&from=2024-10-25&sortBy=publishedAt&language=en&apiKey=b562b8cb5aa645f4901d43503256bf3a";

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            // Request setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            System.out.println("HTTP Status Code: " + status);

            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                insertDataToMongo(responseContent.toString(), mainKeyword);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            connection.disconnect();
        }
    }

    public static void insertDataToMongo(String response, String mainKeyword) {
        JSONObject jsonObject = new JSONObject(response);

        // Extract articles array
        JSONArray articles = jsonObject.getJSONArray("articles");

        // Set up MongoDB connection
        MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("newsdataset");
         MongoCollection<Document> collection = database.getCollection("articles"); // Collection name

            // Loop through articles and insert each into MongoDB
            for (int i = 0; i < articles.length(); i++) {
                JSONObject articleJson = articles.getJSONObject(i);

                // Add the mainKeyword to the article JSON
                articleJson.put("keyword", mainKeyword);

                // Convert the JSON object to a MongoDB Document
                Document articleDoc = Document.parse(articleJson.toString());

                // Insert the document into the collection
                collection.insertOne(articleDoc);
            }

            System.out.println(articles.length() + " articles inserted into the database.");
    }
}
*/
