package org.news.newsapp.util;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.news.newsapp.db.UserService;
import org.news.newsapp.model.Admin;
import org.news.newsapp.model.Reader;
import org.news.newsapp.model.User;

/**
 * @Class: SessionManager
 * @Author: Senuli Wickramage
 * @Description: The SessionManager class handles user login, logout, and account creation for the application.
 * It manages the current user session and validates the user inputs during login and account creation.
 * This class provides functionality to create a new user, authenticate users, and manage user data.
 * Note: If multi-threading is implemented, this class could be extended to create new threads for each logging-in user,
 * allowing concurrent handling of user logins. Each thread would manage its own user session independently, improving
 * performance and responsiveness when handling multiple users at once.
 */
public class SessionManager {
    private static User currentUser; // The current logged-in user
    private static final UserService userService = new UserService(); // UserService instance to interact with the database

    /**
     * Method to retrieve the current logged-in user.
     *
     * @return The current User object representing the logged-in user.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Method to log in a user. Depending on the user type (ADMIN or READER), it sets the current user.
     *
     * @param user The user object representing the user attempting to log in.
     */
    public static void login(User user){
        if (user.getType().equals("ADMIN")){
            System.out.println("Admin login " + user.getEmail());
            currentUser = (Admin) user;
        } else {
            System.out.println("Reader login " + user.getEmail());
            currentUser = (Reader) user;
        }
    }

    /**
     * Method to log out the current user by setting the currentUser to null.
     */
    public static void logout(){
        System.out.println("User logout " + currentUser.getEmail());
        currentUser = null;
    }

    /**
     * Method to create a new Reader user. It sets the current user and saves it in the database.
     *
     * @param name The name of the new reader.
     * @param email The email of the new reader.
     * @param password The password of the new reader.
     */
    public static void createLoginUser(String name, String email, String password){
        currentUser = new Reader(name, email, password);
        userService.createNew(currentUser);
        System.out.println("New reader " + currentUser.getEmail());
    }

    /**
     * Method to validate the user inputs during account creation (name, email, password).
     * It checks for empty fields, account existence, password strength, and email validity.
     *
     * @param nameField The TextField for the name input.
     * @param emailField The TextField for the email input.
     * @param passwordField The PasswordField for the password input.
     * @param errorLabel The Label to display error messages.
     * @return true if the inputs are valid, false otherwise.
     */
    public static boolean validateCreateAccount(TextField nameField, TextField emailField, PasswordField passwordField, Label errorLabel){
        errorLabel.setText("");
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // empty checking
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
            errorLabel.setText("Account Details can't be empty.");
            return false;
        }
        if (userService.isAccountTaken(email)){
            errorLabel.setText("Email already exists.");
            return false;
        }

        // password validation
        if (password.length() < 4){
            errorLabel.setText("Use a strong password.");
            return false;
        }

        // email validation
        if (email.contains(" ")) {
            errorLabel.setText("Invalid email");
            return false;
        }
        if (email.split("@").length != 2) {
            errorLabel.setText("Invalid email");
            return false;
        }
        if (!email.split("@")[1].contains(".")) {
            errorLabel.setText("Invalid email");
            return false;
        }

        // all inputs are valid
        return true;
    }
}
