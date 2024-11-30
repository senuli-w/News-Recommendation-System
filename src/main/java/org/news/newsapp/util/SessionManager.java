package org.news.newsapp.util;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.news.newsapp.db.UserService;
import org.news.newsapp.model.Admin;
import org.news.newsapp.model.Reader;
import org.news.newsapp.model.User;

public class SessionManager {
    private static User currentUser;
    private static final UserService userService = new UserService();

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void login(User user){
        if (user.getType().equals("ADMIN")){
            System.out.println("Admin login " + user.getEmail());
            currentUser = (Admin) user;
        } else {
            System.out.println("Reader login " + user.getEmail());
            currentUser = (Reader) user;
        }
    }

    public static void logout(){
        System.out.println("User logout " + currentUser.getEmail());
        currentUser = null;
    }

    public static void createLoginUser(String name, String email, String password){
        currentUser = new Reader(name, email, password);
        userService.createNew(currentUser);
        System.out.println("New reader " + currentUser.getEmail());
    }

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
