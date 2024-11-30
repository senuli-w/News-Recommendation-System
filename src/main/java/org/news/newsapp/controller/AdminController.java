package org.news.newsapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
    @FXML    public Label userName;
    @FXML    public TableColumn<User, String> userEmailColumn;
    @FXML    public TableColumn<User, String> userTypeColumn;
    @FXML    public TableColumn<User, String> userNameColumn;
    @FXML    public TextField nameField;
    @FXML    public TextField emailField;
    @FXML    public PasswordField passwordField;
    @FXML    public TableColumn<Article, Integer> articleIdColumn;
    @FXML    public TableColumn<Article, String> articleTitleColumn;
    @FXML    public TableColumn<Article, String> articleContentColumn;
    @FXML    public TableColumn<Article, String> articleAuthorColumn;
    @FXML    public TableColumn<Article, String> articleDateTimeColumn;
    @FXML    public TableColumn<Article, String> articleCategoryColumn;
    @FXML    public TableView<Article> articleTable;
    @FXML    public TableView<User> userTable;
    @FXML    public Label errorLabel;
    @FXML    public Label articleErrorLabel;
    @FXML    public TextField titleField;
    @FXML    public TextField authorField;
    @FXML    public TextField contentField;
    @FXML    public TextField categoryField;
    @FXML    public TextField dateTimeField;
    @FXML    public TextField imageUrlField;
    private ObservableList<User> userList;
    private ObservableList<Article> articleList;
    private final ArticleService articleService = new ArticleService();
    private final UserService userService = new UserService();

    @FXML
    public boolean createAdminAccount(ActionEvent event) {
        if (!SessionManager.validateCreateAccount(nameField, emailField, passwordField, errorLabel)){
            return false;
        }
        Admin admin = new Admin(nameField.getText(), emailField.getText(), passwordField.getText());
        userService.createNew(admin);
        updateTableValues();
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLabel.setText("");
        articleErrorLabel.setText("");
        userName.setText(SessionManager.getCurrentUser().getName());

        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        articleIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        articleTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        articleAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        articleContentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        articleCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        articleDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        updateTableValues();
    }

    private void updateTableValues(){
        userList = FXCollections.observableArrayList(userService.getAll());
        articleList = FXCollections.observableArrayList(articleService.getAll());
        userTable.setItems(userList);
        articleTable.setItems(articleList);
    }

    @FXML
    public boolean createNewArticle(ActionEvent event) {
        if (authorField.getText().isEmpty()
        || titleField.getText().isEmpty()
        || contentField.getText().isEmpty()
        || categoryField.getText().isEmpty()
        || dateTimeField.getText().isEmpty()
        || imageUrlField.getText().isEmpty()){
            articleErrorLabel.setText("Details cannot be empty!");
            return false;
        }

        int newArticleId = articleList.getLast().getId() + 1;
        Article article = new Article(newArticleId,
                titleField.getText(), contentField.getText(),
                authorField.getText(), imageUrlField.getText(),
                dateTimeField.getText(), categoryField.getText());

        articleService.createNew(article);
        updateTableValues();
        return true;
    }

    public void logOut(MouseEvent mouseEvent) throws IOException {
        SessionManager.logout();
        Navigator.goTo(mouseEvent, "login.fxml", "");
    }
}
