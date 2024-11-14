package org.news.newsapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import org.news.newsapp.util.Navigator;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignupController {
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
        if (!User.validateEmail(emailField.getText())) {
            errorLabel.setText("Email is invalid.");
            return false;
        }
        if (passwordField.getText().length() < 7) {
            errorLabel.setText("Password must be at least 7 characters long.");
            return false;
        }
        if (!user.setPassword(passwordField.getText())){
            errorLabel.setText("Invalid Password.");
            return false;
        }
        user.setName(nameField.getText());
        user.setEmail(emailField.getText());
        user.setPassword(passwordField.getText());

        DatabaseService.loginUser(user);
        Navigator.goTo(event, "homePage.fxml");
        return true;
    }

    public void goToLoginPage(ActionEvent event) throws IOException {
        Navigator.goTo(event, "login.fxml");
    }
}
