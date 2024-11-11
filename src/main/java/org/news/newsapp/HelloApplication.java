package org.news.newsapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.news.newsapp.service.DatabaseService;

public class HelloApplication extends Application {

    private static HttpURLConnection connection;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1390, 780);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DatabaseService.connect();
        launch();
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