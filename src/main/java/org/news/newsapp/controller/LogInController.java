package org.news.newsapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.news.newsapp.model.User;
import org.news.newsapp.service.DatabaseService;
import org.news.newsapp.util.Navigator;

import java.io.IOException;

public class LogInController {
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
        Navigator.goTo(event, "homePage.fxml");
    }

    @FXML
    public void goToSignUpPage(ActionEvent event) throws IOException {
        Navigator.goTo(event, "signup.fxml");
    }
}
