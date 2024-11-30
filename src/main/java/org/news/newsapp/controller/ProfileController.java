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
    @FXML
    public TextField nameField;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label errorLabel;
    @FXML
    public FlowPane categoryBoxes;
    @FXML
    public Label userName;
    private final UserService userService = new UserService();
    private final ArticleService articleService = new ArticleService();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Reader currentUser = (Reader) SessionManager.getCurrentUser();
        userName.setText(userName.getText());
        nameField.setText(currentUser.getName());
        emailField.setText(currentUser.getEmail());
        passwordField.setText(currentUser.getPassword());
        errorLabel.setText("");
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

    @FXML
    public boolean updateAccount(ActionEvent event) throws IOException {
        if (!validateInput()){
            return false;
        }
        userService.update(nameField.getText(), emailField.getText(), passwordField.getText());
        setCategories();

//        Navigator.goTo(event, "home.fxml");
        System.out.println("Reader "+ emailField.getText() + " updated details PC62");
        errorLabel.setText("Updated Account Successfully");
        return true;
    }


    public boolean validateInput(){
        errorLabel.setText("");
        String name = nameField.getText().trim();
        String password = passwordField.getText().trim();

        // empty checking
        if (name.isEmpty() || password.isEmpty()){
            errorLabel.setText("Account Details can't be empty.");
            return false;
        }

        // password validation
        if (password.length() < 4){
            errorLabel.setText("Use a strong password.");
            return false;
        }

        // all inputs are valid
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
//                label.getStyleClass().remove("category-selected");
            }
        }
        userService.trackUserCategories(user.getEmail(),user.getCategories());
    }

    public void goToHomePage(MouseEvent mouseEvent) throws IOException {
        Navigator.goTo(mouseEvent, "home.fxml", "");
    }

    public void logOut(MouseEvent mouseEvent) throws IOException {
        SessionManager.logout();
        Navigator.goTo(mouseEvent, "login.fxml", "");
    }
}
