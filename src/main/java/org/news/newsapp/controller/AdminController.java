package org.news.newsapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.news.newsapp.db.ArticleService;
import org.news.newsapp.db.UserService;
import org.news.newsapp.model.Admin;
import org.news.newsapp.model.Article;
import org.news.newsapp.model.User;
import org.news.newsapp.util.Navigator;
import org.news.newsapp.util.SessionManager;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    // FXML fields corresponding to the UI elements
    @FXML public Label userName; // Label to display the current logged-in admin's name
    @FXML public TableColumn<User, String> userEmailColumn; // Table column for user email
    @FXML public TableColumn<User, String> userTypeColumn; // Table column for user type (e.g., Admin, Regular)
    @FXML public TableColumn<User, String> userNameColumn; // Table column for username
    @FXML public TextField nameField; // TextField for entering admin's name while creating an account
    @FXML public TextField emailField; // TextField for entering admin's email
    @FXML public PasswordField passwordField; // PasswordField for entering admin's password
    @FXML public TableColumn<Article, Integer> articleIdColumn; // Table column for article ID
    @FXML public TableColumn<Article, String> articleTitleColumn; // Table column for article title
    @FXML public TableColumn<Article, String> articleContentColumn; // Table column for article content
    @FXML public TableColumn<Article, String> articleAuthorColumn; // Table column for article author
    @FXML public TableColumn<Article, String> articleDateTimeColumn; // Table column for article date-time
    @FXML public TableColumn<Article, String> articleCategoryColumn; // Table column for article category
    @FXML public TableView<Article> articleTable; // TableView for displaying articles
    @FXML public TableView<User> userTable; // TableView for displaying users
    @FXML public Label errorLabel; // Label for displaying error messages during account creation
    @FXML public Label articleErrorLabel; // Label for displaying error messages during article creation
    @FXML public TextField titleField; // TextField for entering article title
    @FXML public TextField authorField; // TextField for entering article author
    @FXML public TextField contentField; // TextField for entering article content
    @FXML public TextField categoryField; // TextField for entering article category
    @FXML public TextField dateTimeField; // TextField for entering article date-time
    @FXML public TextField imageUrlField; // TextField for entering article image URL
    @FXML public VBox articleCard; // Vbox for the new added article
    @FXML public Label articleTopic; //Label for the new added article topic
    @FXML public Label articleContent; // Label for the new added article content

    private ObservableList<User> userList; // List of users to be displayed in userTable
    private ObservableList<Article> articleList; // List of articles to be displayed in articleTable
    private final ArticleService articleService = new ArticleService(); // Service for handling article-related operations
    private final UserService userService = new UserService(); // Service for handling user-related operations

    /**
     * Creates a new admin account based on the entered details.
     * @param event the action event triggered by the button click
     * @return boolean indicating success (true) or failure (false)
     */
    @FXML
    public boolean createAdminAccount(ActionEvent event) {
        // Validate account creation input fields using SessionManager
        if (!SessionManager.validateInputs(nameField.getText(),
                emailField.getText(), passwordField.getText(), errorLabel)){
            return false;
        }

        // Create new Admin object and save it using userService
        Admin admin = new Admin(nameField.getText(), emailField.getText(), passwordField.getText());
        userService.createNew(admin);

        // Update the user table to reflect the newly created admin
        updateTableValues();

        // Clear the input fields after account creation
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        return true;
    }

    /**
     * Initializes the controller by setting up table columns and populating data.
     * This method is called automatically when the scene is initialized.
     * @param url the URL location of the FXML file
     * @param resourceBundle the resources used in the FXML
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize error labels
        errorLabel.setText("");
        articleErrorLabel.setText("");

        // Set the current admin's name on the UI
        userName.setText(SessionManager.getCurrentUser().getName());

        // Set the cell value factories for the userTable to display appropriate user data
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Set the cell value factories for the articleTable to display appropriate article data
        articleIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        articleTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        articleAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        articleContentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        articleCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        articleDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        // Update the table values with data from the database
        updateTableValues();
    }

    /**
     * Updates the user and article tables with the latest data from the database.
     */
    private void updateTableValues(){
        // Fetch all users and articles from the database and update the corresponding TableViews
        userList = FXCollections.observableArrayList(userService.getAll());
        articleList = FXCollections.observableArrayList(articleService.getAll());
        userTable.setItems(userList);
        articleTable.setItems(articleList);
    }

    /**
     * Creates a new article based on the entered details.
     * @param event the action event triggered by the button click
     * @return boolean indicating success (true) or failure (false)
     */
    @FXML
    public boolean createNewArticle(ActionEvent event) {
        // Check if any article field is empty, and show an error message if so
        if (authorField.getText().isEmpty()
        || titleField.getText().isEmpty()
        || contentField.getText().isEmpty()
        || categoryField.getText().isEmpty()
        || dateTimeField.getText().isEmpty()
        || imageUrlField.getText().isEmpty()){
            articleErrorLabel.setText("Details cannot be empty!");
            return false;
        }

        // Generate a new article ID based on the last article ID
        int newArticleId = articleList.getLast().getId() + 1;

        // Create new Article object and save it using articleService
        Article article = new Article(newArticleId,
                titleField.getText(), contentField.getText(),
                authorField.getText(), imageUrlField.getText(),
                dateTimeField.getText(), categoryField.getText());

        articleService.createNew(article);

        // Display the new article
        articleCard.setVisible(true);
        articleTopic.setText(article.getTitle());
        articleContent.setText(article.getContent());

        errorLabel.setText("New Article Added.");

        // Update the article table to reflect the newly created article
        updateTableValues();
        return true;
    }

    /**
     * Logs out the current admin and redirects to the login screen.
     * @param mouseEvent the mouse event that triggered the logout action
     * @throws IOException if there is an error while navigating
     */
    public void logOut(MouseEvent mouseEvent) throws IOException {
        // Log out the current user and navigate to the login page
        SessionManager.logout();
        Navigator.goTo(mouseEvent, "login.fxml", "");
    }
}
