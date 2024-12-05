package org.news.newsapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.news.newsapp.db.ArticleService;
import org.news.newsapp.db.UserService;
import org.news.newsapp.model.Article;
import org.news.newsapp.model.Reader;
import org.news.newsapp.util.CategorizationEngine;
import org.news.newsapp.util.Navigator;
import org.news.newsapp.util.RecommendationEngine;
import org.news.newsapp.util.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML public FlowPane cardArea; // Layout container for displaying article cards
    @FXML public Label userName; // Label to display the current user's name
    private ArrayList<Article> finalArticles; // List of articles to display
    private final ArticleService articleService = new ArticleService(); // Service to interact with article data
    private final UserService userService = new UserService(); // Service to interact with user data

    /**
     * Loads articles into the UI by creating cards for each article.
     */
    public void loadArticles(){
        // Clear the existing article cards
        cardArea.getChildren().clear();

        // Loop through the list of articles and create UI components for each
        for (Article article: finalArticles){
            VBox card = new VBox();
            card.setPrefWidth(100);
            card.setPrefHeight(255);
            card.setSpacing(8);
            card.getStyleClass().add("card");

            // Set up the article image
            ImageView cardImage = new ImageView();
            cardImage.setPreserveRatio(false);
            cardImage.setFitWidth(240);
            cardImage.setFitHeight(150);
            cardImage.setImage(new Image(article.getImageUrl()));

            // Set up the article title
            Label cardTopic = new Label();
            cardTopic.getStyleClass().add("card-topic");
            cardTopic.setText(article.getTitle());
            cardTopic.setPadding(new Insets(0,3,0,8));

            // Add image and title to the card
            card.getChildren().add(cardImage);
            card.getChildren().add(cardTopic);

            // Set user data and add click event for navigation
            card.setUserData(article);
            card.setOnMouseClicked(event -> {
                try {
                    goToArticle(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // Add the card to the FlowPane
            cardArea.getChildren().add(card);

            // Stop adding cards if the limit is reached
            if (cardArea.getChildren().size() == 6) {
                break;
            }
        }

    }

    /**
     * Fetches article recommendations for the current user.
     * Uses a Recommendation Engine if sufficient data is available.
     */
    public void getRecommendations(){
        // Get the current user from the session
        Reader currentUser = (Reader) SessionManager.getCurrentUser();
        userName.setText(currentUser.getName());
        finalArticles = new ArrayList<>();
        Random random = new Random();

        // If the user has rated fewer than 4 articles, fetch random articles
        if (currentUser.getRatedArticles().size() < 4){
            System.out.print("Fetched - ");
            for (int i = 0; i>=0; i++){
                int randomNumber = random.nextInt(600); // Generate random article ID
                Article article = articleService.get(String.valueOf(randomNumber));

                // Check if the article has not been viewed and matches user preferences
                if (!currentUser.getViewedArticles().contains(article)){
                    if (currentUser.getCategories().contains(article.getCategory())){
                        finalArticles.addFirst(article); // Add preferred articles at the start
                    } else {
                        finalArticles.add(article);
                    }
                    System.out.print(article.getId() + " ");
                }
                // Stop fetching if enough articles are collected
                if (finalArticles.size()==6){
                    System.out.println(" - " + finalArticles.size());
                    break;
                }
            }
        } else {
            // Use the Recommendation Engine for personalized recommendations
            RecommendationEngine recommendationEngine = new RecommendationEngine((Reader) SessionManager.getCurrentUser());
            List<Integer> recommendations = recommendationEngine.getRecommendations();
            finalArticles = CategorizationEngine.categorizeArticles(recommendations);
        }
    }

    /**
     * Initializes the HomeController.
     * Loads article recommendations and populates the UI.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getRecommendations();
        loadArticles();
    }

    /**
     * Navigates to the selected article page when a card is clicked.
     * Updates the user's viewed articles in the database.
     *
     * @param event Mouse click event on the article card
     * @throws IOException If navigation fails
     */
    @FXML
    public void goToArticle(MouseEvent event) throws IOException {
        VBox source = (VBox) event.getSource();
        Article selectedArticle = (Article) source.getUserData();

        // Update viewed articles for the current user
        Reader currentUser = (Reader) SessionManager.getCurrentUser();
        currentUser.getViewedArticles().add(selectedArticle);
        userService.trackArticleView(currentUser.getEmail(), selectedArticle); //Updating view Articles in the DB
        Navigator.goTo(event, "article.fxml", "");
    }

    /**
     * Navigates to the user's profile page.
     *
     * @param mouseEvent Mouse click event
     * @throws IOException If navigation fails
     */
    public void goToProfilePage(MouseEvent mouseEvent) throws IOException {
        Navigator.goTo(mouseEvent, "profile.fxml", "");
    }

    /**
     * Logs out the user and navigates to the login page.
     *
     * @param mouseEvent Mouse click event
     * @throws IOException If navigation fails
     */
    public void logOut(MouseEvent mouseEvent) throws IOException {
        SessionManager.logout();
        Navigator.goTo(mouseEvent, "login.fxml", "");
    }
}
