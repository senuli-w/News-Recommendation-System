package org.news.newsapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
