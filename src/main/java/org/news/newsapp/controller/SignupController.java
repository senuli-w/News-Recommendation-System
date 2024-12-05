package org.news.newsapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
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

public class SignupController implements Initializable {
    @FXML public ProgressBar progressBar; // Progress bar to indicate processing during signup
    @FXML private TextField nameField; // Field for user's name input
    @FXML private TextField emailField; // Field for user's email input
    @FXML private PasswordField passwordField; // Field for user's password input
    @FXML private Label errorLabel; // Label for displaying error messages
    @FXML public FlowPane categoryBoxes; // FlowPane for displaying selectable categories
    private final UserService userService = new UserService(); // Service for user-related operations
    private final ArticleService articleService = new ArticleService(); // Service for article-related operations

    /**
     * Initializes the Signup page by setting up category options and clearing error labels.
     *
     * @param url URL location of the FXML file
     * @param resourceBundle Resources for localization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLabel.setText(""); // clear any error messages
        ArrayList<String> categories = articleService.getCategories(); // Fetch categories
        categoryBoxes.getChildren().clear();

        // Create labels for categories
        for (String category: categories){
            Label newlabel = new Label(category.toLowerCase());
            newlabel.getStyleClass().add("category");
            newlabel.setOnMouseClicked(event -> {
                select(newlabel);
            });
            categoryBoxes.getChildren().add(newlabel); // Handle selection toggle
        }
    }

    /**
     * Handles the creation of a new Reader account and navigates to the home page on success.
     *
     * @param event ActionEvent triggered by the "Sign Up" button
     * @return true if the account is created successfully, false otherwise
     * @throws IOException If navigation or database interactions fail
     */
    @FXML
    public boolean createReaderAccount(ActionEvent event) throws IOException {
        // Validate inputs
        if (!SessionManager.validateInputs(nameField.getText(),
                emailField.getText(), passwordField.getText(), errorLabel)){
            return false; // Stop processing if validation fails
        }

        // Create new user and save selected categories
        SessionManager.createLoginUser(nameField.getText(), emailField.getText(), passwordField.getText());
        setCategories();

        // Clear form fields for a fresh start
        nameField.clear();
        emailField.clear();
        passwordField.clear();

        Navigator.goTo(event, "home.fxml");
        return true;
    }


    /**
     * Toggles the selection state of a category label.
     *
     * @param categoryBox Label representing the category box
     */
    public void select(Label categoryBox) {
        if(categoryBox.getStyleClass().contains("category-selected")){
            categoryBox.getStyleClass().remove("category-selected"); // Deselect category
        } else {
            categoryBox.getStyleClass().add("category-selected"); //Select category
        }
    }

    /**
     * Sets the selected categories for the current user and saves them in the database.
     */
    public void setCategories() {
        Reader user = (Reader) SessionManager.getCurrentUser();
        Label[] children = categoryBoxes.getChildren().toArray(new Label[0]);

        // Add selected categories to user's preferences
        for (Label label : children){
            if (label.getStyleClass().contains("category-selected")) {
                user.addCategory(label.getText());
                label.getStyleClass().remove("category-selected");// Reset style after saving
            }
        }
        // Save categories in the database
        userService.trackUserCategories(user.getEmail(),user.getCategories());
    }

    /**
     * Navigates to the login page.
     *
     * @param mouseEvent MouseEvent triggered by the "Login" button
     * @throws IOException If navigation fails
     */
    public void goToLoginPage(MouseEvent mouseEvent) throws IOException {
        Navigator.goTo(mouseEvent, "login.fxml", "");
    }
}
