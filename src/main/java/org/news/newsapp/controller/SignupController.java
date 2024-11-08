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
import org.news.newsapp.model.NormalUser;
import org.news.newsapp.model.User;
import org.news.newsapp.service.DatabaseService;

import java.io.IOException;
import java.util.Objects;

public class SignupController {
    @FXML
    public Label loginSignupPageTitle;
    @FXML
    public Button loginSignupPageButton;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label errorLabel;
    @FXML
    public TextField nameField;

    @FXML
    public boolean signUp(ActionEvent event) throws IOException {
        errorLabel.setText("");
        User user = new NormalUser();
        if (!user.setEmail(emailField.getText())) {
            errorLabel.setText("Email is invalid.");
            return false;
        }
        if (passwordField.getText().length() < 7) {
            errorLabel.setText("Password must be at least 7 characters long.");
        }
        if (!user.setPassword(passwordField.getText())){
            errorLabel.setText("Invalid Password.");
        }
        user.setName(nameField.getText());
        user.setEmail(emailField.getText());
        user.setPassword(passwordField.getText());

        DatabaseService.loginUser(user);
        goToCategoryPage(event);
        return true;
    }

    @FXML
    public void goToCategoryPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("categories.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
