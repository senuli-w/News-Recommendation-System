package org.example.diagramnewsrecommendation.controller;

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
import org.example.diagramnewsrecommendation.db.ArticleService;
import org.example.diagramnewsrecommendation.db.UserService;
import org.example.diagramnewsrecommendation.model.Article;
import org.example.diagramnewsrecommendation.model.Reader;
import org.example.diagramnewsrecommendation.util.CategorizationEngine;
import org.example.diagramnewsrecommendation.util.Navigator;
import org.example.diagramnewsrecommendation.util.RecommendationEngine;
import org.example.diagramnewsrecommendation.util.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    public FlowPane cardArea;
    @FXML
    public ProgressBar progressBar;
    @FXML
    public Label userName;
    private ArrayList<Article> finalArticles;
    private final ArticleService articleService = new ArticleService();
    private final UserService userService = new UserService();

    public void loadArticles(){
        System.out.println(finalArticles.size()+ " RC33");

        cardArea.getChildren().clear();
        for (Article article: finalArticles){
            VBox card = new VBox();
            card.setPrefWidth(100);
            card.setPrefHeight(255);
            card.setSpacing(8);
            card.getStyleClass().add("card");

            ImageView cardImage = new ImageView();
            cardImage.setPreserveRatio(false);
            cardImage.setFitWidth(240);
            cardImage.setFitHeight(150);
            cardImage.setImage(new Image(article.getImageUrl()));

            Label cardTopic = new Label();
            cardTopic.getStyleClass().add("card-topic");
            cardTopic.setText(article.getTitle());
            cardTopic.setPadding(new Insets(0,3,0,8));

            card.getChildren().add(cardImage);
            card.getChildren().add(cardTopic);

            card.setUserData(article);
            card.setOnMouseClicked(event -> {
                try {
                    goToArticle(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            cardArea.getChildren().add(card);
            if (cardArea.getChildren().size() == 6) {
                break;
            }
        }

    }
    public void getRecommendations(){
        Reader currentUser = (Reader) SessionManager.getCurrentUser();
        userName.setText(currentUser.getName());
        finalArticles = new ArrayList<>();
        Random random = new Random();

        if (currentUser.getRatedArticles().size() < 4){
            System.out.print("Fetched - ");
            for (int i = 0; i>=0; i++){
                int randomNumber = random.nextInt(600);
                Article article = articleService.get(String.valueOf(randomNumber));
                if (!currentUser.getViewedArticles().contains(article)){
                    if (currentUser.getCategories().contains(article.getCategory())){
                        finalArticles.addFirst(article);
                    } else {
                        finalArticles.add(article);
                    }
                    System.out.print(article.getId() + " ");
                }
                if (finalArticles.size()==6){
                    System.out.println("");
                    break;
                }
            }
        } else {
            RecommendationEngine recommendationEngine = new RecommendationEngine((Reader) SessionManager.getCurrentUser());
            List<Integer> recommendations = recommendationEngine.getRecommendations();
            finalArticles = CategorizationEngine.CategorizeArticles(recommendations);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getRecommendations();
        loadArticles();
    }

    @FXML
    public void goToArticle(MouseEvent event) throws IOException {
        VBox source = (VBox) event.getSource();
        Article selectedArticle = (Article) source.getUserData();

        Reader currentUser = (Reader) SessionManager.getCurrentUser();
        currentUser.getViewedArticles().add(selectedArticle);
        userService.trackArticleView(currentUser.getEmail(), selectedArticle); //Updating view Articles in the DB
        progressBar.setVisible(true);
        Navigator.goTo(event, "article.fxml", "");
    }

    public void goToProfilePage(MouseEvent mouseEvent) throws IOException {
        Navigator.goTo(mouseEvent, "profile.fxml", "");
    }

    public void logOut(MouseEvent mouseEvent) throws IOException {
        SessionManager.logout();
        Navigator.goTo(mouseEvent, "login.fxml", "");
    }
}
