package org.example.diagramnewsrecommendation.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.example.diagramnewsrecommendation.db.UserService;
import org.example.diagramnewsrecommendation.model.Article;
import org.example.diagramnewsrecommendation.model.Reader;
import org.example.diagramnewsrecommendation.util.Navigator;
import org.example.diagramnewsrecommendation.util.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ArticleController implements Initializable {
    @FXML    public Label title;
    @FXML    public Label author;
    @FXML    public Label dateTime;
    @FXML    public Label category;
    @FXML    public ImageView image;
    @FXML    public Label content;
    @FXML    public Slider ratingSlider;
    @FXML    public ProgressBar progressBar;
    @FXML    public Label userName;
    private int ratingValue;
    private final UserService userService = new UserService();

    public void viewArticle(){
        Reader currentUser = (Reader) SessionManager.getCurrentUser();
        Article viewedArticle = currentUser.getViewedArticles().getLast();
        title.setText(viewedArticle.getTitle());
        author.setText(viewedArticle.getAuthor());
        dateTime.setText(viewedArticle.getDateTime());
        category.setText("Category : " + viewedArticle.getCategory());
        image.setImage(new Image(viewedArticle.getImageUrl()));
        content.setText(viewedArticle.getContent());
    }
    public void rateArticle(int rating){
        Reader currentUser = (Reader) SessionManager.getCurrentUser();
        String userEmail = currentUser.getEmail();
        Article article = currentUser.getViewedArticles().getLast();
        currentUser.getRatedArticles().add(article);
        userService.trackArticleRatings(userEmail, article, rating);
    }

    public void goToHomePage(MouseEvent mouseEvent) throws IOException {
        progressBar.setVisible(true);
        Navigator.goTo(mouseEvent, "home.fxml", "");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Reader currentUser = (Reader) SessionManager.getCurrentUser();
        userName.setText(currentUser.getName());
        viewArticle();
        ratingValue = (int) ratingSlider.getValue();
        ratingSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                ratingValue = (int) ratingSlider.getValue();
                rateArticle(ratingValue);
            }
        });
    }

    public void goToProfilePage(MouseEvent mouseEvent) throws IOException {
        Navigator.goTo(mouseEvent, "profile.fxml", "");
    }

    public void logOut(MouseEvent mouseEvent) throws IOException {
        SessionManager.logout();
        Navigator.goTo(mouseEvent, "login.fxml", "");
    }
}
