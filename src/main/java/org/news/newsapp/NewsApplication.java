package org.news.newsapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.news.newsapp.db.Database;

import java.io.IOException;

/**
 * @Class: NewsApplication
 * @Author: Senuli Wickramage
 * @Description:
 * The NewsApplication class serves as the entry point for the JavaFX-based news recommendation application.
 * It initializes the primary user interface and connects to the database before launching the application.
 * This class manages the application lifecycle, including loading the FXML UI file and setting up the main stage.
 */
public class NewsApplication extends Application {
    /**
     * Starts the JavaFX application by loading the login screen and setting up the main window.
     *
     * @param stage The primary stage for this application, onto which the scene will be set.
     * @throws IOException If the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NewsApplication.class.getResource("login.fxml"));
        // Create a scene with the loaded FXML content and set its size
        Scene scene = new Scene(fxmlLoader.load(), 800, 520);
        stage.setTitle("News Recommendation App"); // Set the title of the main application window
        stage.setScene(scene); // Set the scene for the primary stage (main window)
        stage.setResizable(false); // Make the window non-resizable
        stage.show(); // Show the application window
    }

    /**
     * The main method that serves as the entry point for the Java application.
     * It connects to the database and then launches the JavaFX application.
     *
     * @param args Command-line arguments, if any.
     */
    public static void main(String[] args) {
        Database.connectToDatabase();
        launch();
    }
}
