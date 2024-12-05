package org.news.newsapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.news.newsapp.model.Admin;
import org.news.newsapp.model.Reader;
import org.news.newsapp.db.UserService;
import org.news.newsapp.util.Navigator;
import org.news.newsapp.util.SessionManager;

import java.io.IOException;
public class LoginController{
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    private final UserService userService = new UserService();

    @FXML
    public boolean login(ActionEvent event) throws IOException, InterruptedException {
        errorLabel.setText("");
        if (!userService.isAccountTaken(emailField.getText())){
            errorLabel.setText("Invalid Email");
            return false;
        }
        if (!userService.get(emailField.getText()).getPassword().equals(passwordField.getText())) {
            errorLabel.setText("Incorrect password");
            return false;
        }
        if (userService.get(emailField.getText()).getType().equals("ADMIN")) {
            Admin admin = (Admin) userService.get(emailField.getText());
            SessionManager.login(admin);
        } else {
            Reader reader = (Reader) userService.get(emailField.getText());
            SessionManager.login(reader);
        }
        if (SessionManager.getCurrentUser().getType().equals("ADMIN")){
            Navigator.goTo(event, "admin.fxml");
        } else {
            Navigator.goTo(event, "home.fxml");
        }
        return true;
    }

    public void goToSignupPage(MouseEvent mouseEvent) throws IOException {
        Navigator.goTo(mouseEvent, "signup.fxml", "");
    }
}
