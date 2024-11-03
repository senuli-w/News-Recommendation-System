package org.news.newsapp.controller;

import com.fasterxml.jackson.databind.node.ContainerNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.news.newsapp.model.Article;
import org.news.newsapp.service.DatabaseService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RecommendedController implements Initializable {
    @FXML
    public VBox sideBar1;
    @FXML
    public VBox articleContainer;
    @FXML
    public VBox middleBar;
    @FXML
    public VBox sideBar2;
//    private VBox article

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadArticles();
    }

    private void loadArticles(){
        ArrayList<Article> articles = DatabaseService.getArticles();

        sideBar1.getChildren().clear();
        middleBar.getChildren().clear();

        VBox currentNode = sideBar1;

        int count = 0;
        for (Article article: articles){
            if (count == 3){
                currentNode = middleBar;
            } else if (count == 5) {
                currentNode = sideBar2;
            } else if (count == 8) {
                break;
            }
            count++;
            VBox articleContainer = new VBox();
            articleContainer.setPadding(new Insets(10, 10, 10, 10));

            // ImageView for the article image
            ImageView imageView = new ImageView();

            // Labels for title, description, author, and source
            Label titleLabel = new Label(article.getTitle());
            titleLabel.getStyleClass().add("article-topic");

            Label descriptionLabel = new Label(article.getDescription());
            descriptionLabel.getStyleClass().add("article-description");
            descriptionLabel.setWrapText(true);
            descriptionLabel.setPrefWidth(254);

            Label authorLabel = new Label(article.getAuthor());
            authorLabel.getStyleClass().add("article-author");

            Label sourceLabel = new Label(article.getSource().getName());

            Separator separator = new Separator();
            separator.getStyleClass().add("article-separator");

            if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {

                int barWidth = 253;
                int barHeight = 212;

                if (currentNode == middleBar){
                    barWidth = 714;
                    barHeight = 428;
                }
                try{

                    imageView.setImage(new Image(article.getUrlToImage(), barWidth, barHeight, true, true));

                    sourceLabel.getStyleClass().add("article-source");

                    articleContainer.getChildren().addAll(imageView, titleLabel, descriptionLabel, authorLabel, sourceLabel, separator);
                } catch (Exception e){
                    articleContainer.getStyleClass().add("no-image-article");
                    titleLabel.getStyleClass().add("no-image-article-label");
                    descriptionLabel.getStyleClass().add("no-image-article-label");
                    authorLabel.getStyleClass().add("no-image-article-label");
//                separator.getStyleClass().add('')
                    articleContainer.getChildren().addAll(titleLabel, separator, descriptionLabel, authorLabel);
                }
            }


            // Add the article VBox to the main articleContainer
            currentNode.getChildren().add(articleContainer);


        }
    }

}