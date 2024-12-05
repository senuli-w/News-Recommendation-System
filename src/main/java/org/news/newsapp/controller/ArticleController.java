package org.news.newsapp.controller;

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
import org.news.newsapp.db.UserService;
import org.news.newsapp.model.Article;
import org.news.newsapp.model.Reader;
import org.news.newsapp.util.Navigator;
import org.news.newsapp.util.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ArticleController implements Initializable {
    // FXML fields corresponding to UI elements
    @FXML public Label title; // Label to display article title
    @FXML public Label author; // Label to display article author
    @FXML public Label dateTime; // Label to display article publication date and time
    @FXML public Label category; // Label to display article category
    @FXML public ImageView image; // ImageView to display article's image
    @FXML public Label content; // Label to display article content
    @FXML public Slider ratingSlider; // Slider to allow the user to rate the article
    @FXML public Label userName; // Label to display the current user's name
    private int ratingValue; // Holds the current rating value selected by the user
    private final UserService userService = new UserService(); // Service for handling user-related operations

    /**
     * Displays the article details such as title, author, date, category, content, and image.
     */
    public void viewArticle(){
        // Retrieve the current user and the last viewed article
        Reader currentUser = (Reader) SessionManager.getCurrentUser();
        Article viewedArticle = currentUser.getViewedArticles().getLast();

        // Set the UI components with the article's details
        title.setText(viewedArticle.getTitle());
        author.setText(viewedArticle.getAuthor());
        dateTime.setText(viewedArticle.getDateTime());
        category.setText("Category : " + viewedArticle.getCategory());
        image.setImage(new Image(viewedArticle.getImageUrl()));
        content.setText(viewedArticle.getContent());
    }

    /**
     * Rates the current viewed article based on the user's rating.
     * @param rating the rating value given by the user
     */
    public void rateArticle(int rating){
        // Get the current user and the article being rated
        Reader currentUser = (Reader) SessionManager.getCurrentUser();
        String userEmail = currentUser.getEmail();
        Article article = currentUser.getViewedArticles().getLast();

        // Mark the article as rated and track the rating in the user service
        currentUser.getRatedArticles().add(article);
        userService.trackArticleRatings(userEmail, article, rating);
    }

    /**
     * Navigates to the home page when the home button is clicked.
     * @param mouseEvent the mouse event triggered by the button click
     * @throws IOException if there is an error while navigating
     */
    public void goToHomePage(MouseEvent mouseEvent) throws IOException {
        Navigator.goTo(mouseEvent, "home.fxml", "");
    }

    /**
     * Initializes the controller by setting up the user's name, displaying the article,
     * and setting up the rating slider.
     * @param url the URL location of the FXML file
     * @param resourceBundle the resources used in the FXML
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Retrieve the current user and set their name on the UI
        Reader currentUser = (Reader) SessionManager.getCurrentUser();
        userName.setText(currentUser.getName());

        // Display the article content
        viewArticle();

        // Initialize rating value based on slider's value
        ratingValue = (int) ratingSlider.getValue();

        // Add listener to the rating slider to update the rating value when the slider is moved
        ratingSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                ratingValue = (int) ratingSlider.getValue(); // Update the rating value
                rateArticle(ratingValue); // Rate the article based on the new value
            }
        });
    }

    /**
     * Navigates to the profile page when the profile button is clicked.
     * @param mouseEvent the mouse event triggered by the button click
     * @throws IOException if there is an error while navigating
     */
    public void goToProfilePage(MouseEvent mouseEvent) throws IOException {
        Navigator.goTo(mouseEvent, "profile.fxml", "");
    }

    /**
     * Logs out the current user and navigates to the login page.
     * @param mouseEvent the mouse event triggered by the logout action
     * @throws IOException if there is an error while navigating
     */
    public void logOut(MouseEvent mouseEvent) throws IOException {
        // Log out the current user and navigate to the login page
        SessionManager.logout();
        Navigator.goTo(mouseEvent, "login.fxml", "");
    }
}
