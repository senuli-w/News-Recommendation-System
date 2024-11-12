package org.news.newsapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.news.newsapp.HelloApplication;
import org.news.newsapp.model.User;
import org.news.newsapp.service.DatabaseService;

import java.io.IOException;
import java.util.Objects;

public class SignInController {
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label errorLabel;
    @FXML
    public Button loginSignupPageButton;

    @FXML
    public boolean logIn(ActionEvent event) throws IOException {
        errorLabel.setText("");
        if(!User.validateEmail(emailField.getText())){
            errorLabel.setText("Invalid email.");
            return false;
        }
        User user = DatabaseService.getUser(emailField.getText());
        if (user == null){
            errorLabel.setText("Invalid email.");
            return false;
        }
        if (user.getPassword().equals(passwordField.getText())){
            DatabaseService.loginUser(user);
            goToHomePage(event);
            return true;
        }
        errorLabel.setText("Incorrect password.");
        return false;
    }

    @FXML
    public void goToHomePage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("homePage.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
