package org.news.newsapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import org.news.newsapp.models.Article;
import org.news.newsapp.models.Source;

public class HelloApplication extends Application {

    private static HttpURLConnection connection;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DatabaseConnection.connect();
        getApiData("emotional");
        launch();
    }

    public static void getApiData(String mainKeyword){
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        String urlString = "https://newsapi.org/v2/everything?q=" + mainKeyword + "&from=2024-10-25&sortBy=publishedAt&language=en&apiKey=b562b8cb5aa645f4901d43503256bf3a";
        try {
            URL url = new URL(String.format(urlString, mainKeyword));
            connection = (HttpURLConnection) url.openConnection();
            // Request setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            System.out.println(status);

            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null){
                    responseContent.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null){
                    responseContent.append(line);
                }
                parseJson(responseContent.toString(), mainKeyword);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            connection.disconnect();
        }
    }

    /*public static void parseJson(String s, String mainKeyword) throws IOException {
        JSONObject jsonObject = new JSONObject(s);
        System.out.println(jsonObject);

        int totalResults = jsonObject.getInt("totalResults");
        System.out.println(totalResults);
        JSONArray articles = jsonObject.getJSONArray("articles");
        System.out.println(articles.length());

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Article> articleList = mapper.readValue(articles.toString(), ArrayList.class);

//        for (Article article : articleList){
//            article.setKeywords(new ArrayList<>());
//            article.getKeywords().add(mainKeyword);
//        }

        System.out.println(articleList.size());
//        System.out.println(articleList.getFirst().getKeywords());
    }*/


    public static void parseJson(String s, String mainKeyword) throws IOException {
        JSONObject jsonObject = new JSONObject(s);
        System.out.println(jsonObject);

        JSONArray articles = jsonObject.getJSONArray("articles");

        ArrayList<Article> articleList = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            JSONObject articleJson = articles.getJSONObject(i);

            // Check if 'source' is not null
            JSONObject sourceJson = articleJson.optJSONObject("source");
            Source source = null;
            if (sourceJson != null) {
                String id = sourceJson.optString("id", "nan"); // Get id safely
                String name = sourceJson.optString("name", "nan"); // Default name if null
                source = new Source(id, name);
            }

            // Create an ArrayList with just the mainKeyword
            ArrayList<String> keywords = new ArrayList<>();
            keywords.add(mainKeyword); // Add mainKeyword to the keywords list

            // Create Article object
            Article article = new Article(
                    source,
                    articleJson.optString("author", "nan"),
                    articleJson.optString("title", "nan"),
                    articleJson.optString("description", "nan"),
                    articleJson.optString("url", "nan"),
                    articleJson.optString("urlToImage", "nan"),
                    articleJson.optString("publishedAt", "nan"),
                    articleJson.optString("content", "nan"),
                    keywords
            );
            articleList.add(article);
            DatabaseConnection.saveArticleToDatabase(article);
        }
        System.out.println(articleList.size());
    }
}