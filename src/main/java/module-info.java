module org.news.newsapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires com.fasterxml.jackson.databind;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;


    opens org.news.newsapp to javafx.fxml;
    exports org.news.newsapp;
    exports org.news.newsapp.controllers;
    opens org.news.newsapp.controllers to javafx.fxml;
}