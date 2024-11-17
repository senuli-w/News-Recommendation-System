package org.news.newsapp.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
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
    private int ratingValue;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NormalUser currentUser = (NormalUser) DatabaseService.getCurrentUser();
        String viewedArticleId =  currentUser.getViewedArticles().getLast();
        Article viewedArticle = DatabaseService.getArticle(viewedArticleId);
        assert viewedArticle != null;
        articleTitle.setText(viewedArticle.getHeadline());
        articleContent.setText(viewedArticle.getHeadline()+viewedArticle.getDescription());
        articleAuthor.setText(viewedArticle.getAuthors());
        articleDate.setText(viewedArticle.getDate());

        ratingValue = (int) ratingSlider.getValue();
        ratingLabel.setText(Integer.toString(ratingValue));

        ratingSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                ratingValue = (int) ratingSlider.getValue();
                ratingLabel.setText(Integer.toString(ratingValue));
            }
        });
    }

    @FXML
    public void goToHomePage(ActionEvent event) throws IOException {
        Navigator.goTo(event, "homePage.fxml");
    }

    public void setRatings(int rating){
        NormalUser currentUser = (NormalUser) DatabaseService.getCurrentUser();

    }
}
