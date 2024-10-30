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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.news.newsapp.models.Article;

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
        getApiData("emotional");
        DatabaseConnection.connect();
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

    public static void parseJson(String s, String mainKeyword) throws IOException {
        JSONObject jsonObject = new JSONObject(s);
        System.out.println(jsonObject);

        int totalResults = jsonObject.getInt("totalResults");
//        System.out.println(totalResults);
        JSONArray articles = jsonObject.getJSONArray("articles");

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Article> articleList = mapper.readValue(articles.toString(), ArrayList.class);

//        for (Article article : articleList){
//            article.setKeywords(new ArrayList<>());
//            article.getKeywords().add(mainKeyword);
//        }

        System.out.println(articleList.size());
//        System.out.println(articleList.getFirst().getKeywords());
    }
}