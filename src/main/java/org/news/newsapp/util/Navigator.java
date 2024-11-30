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

public class Navigator {
    @FXML
    public static void goTo(ActionEvent event, String file) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(NewsApplication.class.getResource(file)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void goTo(MouseEvent event, String file, String guess) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(NewsApplication.class.getResource(file)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
