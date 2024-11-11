package org.news.newsapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.news.newsapp.model.Article;
import org.news.newsapp.service.DatabaseService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {
    @FXML
    public VBox articlePane;

    private void loadArticles(){
        ArrayList<String> categoryList = DatabaseService.getCategoryList();
        articlePane.getChildren().clear();
        for (String category: categoryList){
            VBox categoryPane = new VBox();
            categoryPane.getStyleClass().add("home-page-category-box");
            Label categoryTopic = new Label(category);
            categoryTopic.getStyleClass().add("home-page-category");
            categoryTopic.setPadding(new Insets(4,0,0,20));
            HBox articleBoxes = new HBox();
            articleBoxes.getStyleClass().add("home-page-category-que");
            articleBoxes.setPrefHeight(200);
            articleBoxes.setSpacing(10);
            articleBoxes.setPadding(new Insets(5, 20,5,20));
            categoryPane.getChildren().add(categoryTopic);
            categoryPane.getChildren().add(articleBoxes);
            ArrayList<Article> articlesFromCategory = DatabaseService.getArticlesFromCategory(category);
            for (int i=0; i<3; i++){
                VBox article = new VBox();
                article.setPadding(new Insets(18));
                article.setSpacing(5);
                article.getStyleClass().add("-home-page-article");
                article.setAlignment(Pos.CENTER_LEFT);
                article.setPrefWidth(441);
                Label articleTopic = new Label(articlesFromCategory.get(i).getHeadline());
                articleTopic.getStyleClass().add("home-page-article-title");
                articleTopic.setWrapText(true);
                Label articleAuthorDate;
                if (articlesFromCategory.get(i).getAuthors().isEmpty()){
                    articleAuthorDate = new Label(articlesFromCategory.get(i).getDate());
                } else {
                    articleAuthorDate = new Label(articlesFromCategory.get(i).getAuthors()
                            + "        "
                            + articlesFromCategory.get(i).getDate());
                }
                articleAuthorDate.getStyleClass().add("home-page-article-author-date");
                article.getChildren().add(articleTopic);
                article.getChildren().add(articleAuthorDate);
                articleBoxes.getChildren().add(article);
            }
            articlePane.getChildren().add(categoryPane);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadArticles();
    }
}
