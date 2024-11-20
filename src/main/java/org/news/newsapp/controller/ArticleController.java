package org.news.newsapp.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.news.newsapp.model.Article;
import org.news.newsapp.model.NormalUser;
import org.news.newsapp.service.DatabaseService;
import org.news.newsapp.util.Navigator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ArticleController implements Initializable {
    @FXML
    public Label articleTitle;
    @FXML
    public Label articleAuthor;
    @FXML
    public Label articleDate;
    @FXML
    public Label articleContent;
    @FXML
    public Slider ratingSlider;
    @FXML
    public Label ratingLabel;
    @FXML
    public ImageView articleImage;
    private int ratingValue;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NormalUser currentUser = (NormalUser) DatabaseService.getCurrentUser();
        String viewedArticleId =  currentUser.getViewedArticles().getLast();
        Article viewedArticle = DatabaseService.getArticle(viewedArticleId);
        assert viewedArticle != null;
        articleTitle.setText(viewedArticle.getTitle());
        articleContent.setText(viewedArticle.getDescription() + " " +viewedArticle.getContent());
        articleAuthor.setText(viewedArticle.getAuthor());
        articleDate.setText(viewedArticle.getDate());
        articleImage.setImage(new Image(viewedArticle.getUrlToImage()));

        ratingValue = (int) ratingSlider.getValue();
        ratingLabel.setText(Integer.toString(ratingValue));

        ratingSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                ratingValue = (int) ratingSlider.getValue();
                ratingLabel.setText(Integer.toString(ratingValue));
                setRatings(ratingValue);
            }
        });
    }

    @FXML
    public void goToHomePage(ActionEvent event) throws IOException {
        Navigator.goTo(event, "homePage.fxml");
    }

    public void setRatings(int rating){
        NormalUser currentUser = (NormalUser) DatabaseService.getCurrentUser();
        String userEmail = currentUser.getEmail();
        String articleId = currentUser.getViewedArticles().getLast();
        currentUser.getRatedArticles().add(articleId);
        DatabaseService.trackArticleRatings(userEmail, articleId, rating);
    }
}
