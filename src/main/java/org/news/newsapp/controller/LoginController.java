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
    @FXML private TextField emailField; // Input field for user email
    @FXML private PasswordField passwordField; // Input field for user password
    @FXML private Label errorLabel; // Label to display error messages
    private final UserService userService = new UserService(); // Service to handle user-related operations

    /**
     * Handles the login functionality.
     * Validates the entered email and password, and redirects the user based on their account type.
     *
     * @param event ActionEvent triggered by the login button
     * @return true if login is successful, false otherwise
     * @throws IOException If navigation to another page fails
     * @throws InterruptedException If the thread is interrupted during login
     */
    @FXML
    public boolean login(ActionEvent event) throws IOException, InterruptedException {
        // Clear any previous error messages
        errorLabel.setText("");

        // Check if the entered email is associated with an account
        if (!userService.isAccountTaken(emailField.getText())) {
            errorLabel.setText("Invalid Email"); // Display error if email is not registered
            return false;
        }

        // Validate the password for the provided email
        if (!userService.get(emailField.getText()).getPassword().equals(passwordField.getText())) {
            errorLabel.setText("Incorrect password"); // Display error if password is incorrect
            return false;
        }

        // Determine user type and set up session accordingly
        if (userService.get(emailField.getText()).getType().equals("ADMIN")) {
            // If user is an admin, cast to Admin and login
            Admin admin = (Admin) userService.get(emailField.getText());
            SessionManager.login(admin);
        } else {
            // If user is a reader, cast to Reader and login
            Reader reader = (Reader) userService.get(emailField.getText());
            SessionManager.login(reader);
        }

        // Navigate to the appropriate page based on user type
        if (SessionManager.getCurrentUser().getType().equals("ADMIN")) {
            Navigator.goTo(event, "admin.fxml"); // Navigate to admin dashboard
        } else {
            Navigator.goTo(event, "home.fxml"); // Navigate to home page for readers
        }

        return true; // Login successful
    }

    /**
     * Navigates to the signup page when the "Sign Up" link is clicked.
     *
     * @param mouseEvent MouseEvent triggered by clicking the signup link
     * @throws IOException If navigation to the signup page fails
     */
    public void goToSignupPage(MouseEvent mouseEvent) throws IOException {
        Navigator.goTo(mouseEvent, "signup.fxml", "");
    }
}
