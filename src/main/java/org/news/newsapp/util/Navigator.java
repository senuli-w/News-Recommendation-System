package org.news.newsapp.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.news.newsapp.NewsApplication;

import java.io.IOException;
import java.util.Objects;

/**
 * @Class: Navigator
 * @Author: Senuli Wickramage
 * @Description: The Navigator class handles the navigation between different scenes in the JavaFX application.
 * It allows users to move between scenes by loading FXML files and switching the current scene.
 * This class contains methods that listen to user events (ActionEvent and MouseEvent) and change scenes accordingly.
 */
public class Navigator {
    /**
     * Method to navigate to a new scene when triggered by an ActionEvent (e.g., button click).
     *
     * @param event The ActionEvent that triggers the scene change (e.g., button click).
     * @param file The FXML file that represents the new scene.
     * @throws IOException If the FXML file cannot be loaded.
     */
    @FXML
    public static void goTo(ActionEvent event, String file) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(NewsApplication.class.getResource(file)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method to navigate to a new scene when triggered by a MouseEvent (e.g., mouse click).
     * This method also accepts an additional 'guess' parameter, though it is not currently in use.
     *
     * @param event The MouseEvent that triggers the scene change (e.g., mouse click).
     * @param file The FXML file that represents the new scene.
     * @param guess An additional string parameter that could be used for future functionality.
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static void goTo(MouseEvent event, String file, String guess) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(NewsApplication.class.getResource(file)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
