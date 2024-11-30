package org.example.diagramnewsrecommendation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import org.example.diagramnewsrecommendation.db.ArticleService;
import org.example.diagramnewsrecommendation.db.UserService;
import org.example.diagramnewsrecommendation.model.Reader;
import org.example.diagramnewsrecommendation.util.Navigator;
import org.example.diagramnewsrecommendation.util.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
public class signupController implements Initializable {
    @FXML    public ProgressBar progressBar;
    @FXML    private TextField nameField;
    @FXML    private TextField emailField;
    @FXML    private PasswordField passwordField;
    @FXML    private Label errorLabel;
    @FXML    public FlowPane categoryBoxes;
    private final UserService userService = new UserService();
    private final ArticleService articleService = new ArticleService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLabel.setText("");
        ArrayList<String> categories = articleService.getCategories();
        categoryBoxes.getChildren().clear();
        for (String category: categories){
            Label newlabel = new Label(category.toLowerCase());
            newlabel.getStyleClass().add("category");
            newlabel.setOnMouseClicked(event -> {
                select(newlabel);
            });
            categoryBoxes.getChildren().add(newlabel);
        }
    }

    @FXML
    public boolean createReaderAccount(ActionEvent event) throws IOException {
        if (!SessionManager.validateCreateAccount(nameField, emailField, passwordField, errorLabel)){
            return false;
        }
        SessionManager.createLoginUser(nameField.getText(), emailField.getText(), passwordField.getText());
        setCategories();
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        progressBar.setVisible(true);
        Navigator.goTo(event, "home.fxml");
        return true;
    }


    public void select(Label categoryBox) {
        if(categoryBox.getStyleClass().contains("category-selected")){
            categoryBox.getStyleClass().remove("category-selected");
        } else {
            categoryBox.getStyleClass().add("category-selected");
        }
    }

    public void setCategories() {
        Reader user = (Reader) SessionManager.getCurrentUser();
        Label[] children = categoryBoxes.getChildren().toArray(new Label[0]);
        for (Label label : children){
            if (label.getStyleClass().contains("category-selected")) {
                user.addCategory(label.getText());
                label.getStyleClass().remove("category-selected");
            }
        }
        userService.trackUserCategories(user.getEmail(),user.getCategories());
    }

    public void goToLoginPage(MouseEvent mouseEvent) throws IOException {
        Navigator.goTo(mouseEvent, "login.fxml", "");
    }
}
