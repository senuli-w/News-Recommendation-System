package org.news.newsapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import org.news.newsapp.db.ArticleService;
import org.news.newsapp.db.UserService;
import org.news.newsapp.model.Reader;
import org.news.newsapp.util.Navigator;
import org.news.newsapp.util.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML public TextField nameField; // Field for displaying and updating user's name
    @FXML public TextField emailField; // Field for displaying and updating user's email
    @FXML public PasswordField passwordField; // Field for displaying and updating user's password
    @FXML public Label errorLabel; // Label for displaying error or success messages
    @FXML public FlowPane categoryBoxes; // FlowPane to display category selection boxes
    @FXML public Label userName; // Label to display the user's name
    private final UserService userService = new UserService(); // Service for user-related operations
    private final ArticleService articleService = new ArticleService(); // Service for article-related operations

    /**
     * Initializes the Profile page by loading user details and category preferences.
     *
     * @param url URL location of the FXML file
     * @param resourceBundle Resources for localization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Reader currentUser = (Reader) SessionManager.getCurrentUser();
        userName.setText(currentUser.getName()); // Set user's name in the profile
        nameField.setText(currentUser.getName());
        emailField.setText(currentUser.getEmail());
        passwordField.setText(currentUser.getPassword());
        errorLabel.setText("");

        // Load and display categories
        ArrayList<String> categories = articleService.getCategories();
        categoryBoxes.getChildren().clear();
        for (String category: categories){
            Label newlabel = new Label(category.toLowerCase());
            if (currentUser.getCategories().contains(newlabel.getText())) {
                newlabel.getStyleClass().add("category-selected");
            }
            newlabel.getStyleClass().add("category");
            newlabel.setOnMouseClicked(event -> {
                select(newlabel);
            });
            categoryBoxes.getChildren().add(newlabel);
        }
    }

    /**
     * Updates the user's account details and preferences.
     *
     * @param event ActionEvent triggered by the "Update Account" button
     * @return true if update is successful, false otherwise
     * @throws IOException If an error occurs while interacting with the database or UI
     */
    @FXML
    public boolean updateAccount(ActionEvent event) throws IOException {
        if (!validateInput()){
            return false; // validation failed
        }

        // Update user details and categories in the database
        userService.update(nameField.getText(), emailField.getText(), passwordField.getText());
        setCategories();

        System.out.println("Reader "+ emailField.getText() + " updated details PC62");
        errorLabel.setText("Updated Account Successfully");  // Indicate success
        return true;
    }

    /**
     * Validates the input fields for updating account details.
     *
     * @return true if all inputs are valid, false otherwise
     */
    public boolean validateInput(){
        errorLabel.setText("");
        String name = nameField.getText().trim();
        String password = passwordField.getText().trim();

        // Check for empty fields
        if (name.isEmpty() || password.isEmpty()){
            errorLabel.setText("Account Details can't be empty.");
            return false;
        }

        // Validate password strength
        if (password.length() < 4){
            errorLabel.setText("Use a strong password.");
            return false;
        }

        return true; // all inputs are valid
    }

    /**
     * Toggles the selection state of a category box.
     *
     * @param categoryBox Label representing the category box
     */
    public void select(Label categoryBox) {
        if(categoryBox.getStyleClass().contains("category-selected")){
            categoryBox.getStyleClass().remove("category-selected"); // Deselect category
        } else {
            categoryBox.getStyleClass().add("category-selected"); // Select category
        }
    }

    /**
     * Updates the user's selected categories and saves them in the database.
     */
    public void setCategories() {
        Reader user = (Reader) SessionManager.getCurrentUser();
        Label[] children = categoryBoxes.getChildren().toArray(new Label[0]);
        for (Label label : children){
            if (label.getStyleClass().contains("category-selected")) {
                user.addCategory(label.getText()); // Add selected categories to user's preferences
//                label.getStyleClass().remove("category-selected");
            }
        }
        userService.trackUserCategories(user.getEmail(),user.getCategories()); // Save to database
    }

    /**
     * Navigates to the home page.
     *
     * @param mouseEvent MouseEvent triggered by the "Home" button
     * @throws IOException If navigation fails
     */
    public void goToHomePage(MouseEvent mouseEvent) throws IOException {
        Navigator.goTo(mouseEvent, "home.fxml", "");
    }

    /**
     * Logs out the user and navigates to the login page.
     *
     * @param mouseEvent MouseEvent triggered by the "Log Out" button
     * @throws IOException If navigation fails
     */
    public void logOut(MouseEvent mouseEvent) throws IOException {
        SessionManager.logout(); // Clear user session
        Navigator.goTo(mouseEvent, "login.fxml", "");
    }
}
