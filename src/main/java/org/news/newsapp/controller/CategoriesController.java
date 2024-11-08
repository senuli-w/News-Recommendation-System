package org.news.newsapp.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import org.news.newsapp.model.Category;
import org.news.newsapp.model.NormalUser;
import org.news.newsapp.service.DatabaseService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CategoriesController implements Initializable {

    public FlowPane categoryBoxes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<String> categoryList = DatabaseService.getCategoryList();
        categoryBoxes.getChildren().clear();
        for (String category: categoryList){
            Label newlabel = new Label(category.toLowerCase());
            newlabel.getStyleClass().add("category-box");
            newlabel.setOnMouseClicked(event -> {
                select(newlabel);
            });
            categoryBoxes.getChildren().add(newlabel);
        }
    }

    public void select(Label categoryBox) {
        if(categoryBox.getStyleClass().contains("category-box-selected")){
            categoryBox.getStyleClass().remove("category-box-selected");
        } else {
            categoryBox.getStyleClass().add("category-box-selected");
        }
    }

    public void setCategories(ActionEvent event) {
        NormalUser user = (NormalUser) DatabaseService.getCurrentUser();
        Label[] children = categoryBoxes.getChildren().toArray(new Label[0]);
        for (Label label : children){
             if (label.getStyleClass().contains("category-box-selected")) {
                 user.addCategory(label.getText());
             }
        }

        DatabaseService.createAccount(user);
    }
}
