module org.news.newsapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires com.fasterxml.jackson.databind;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;


    opens org.news.newsapp to javafx.fxml;
    exports org.news.newsapp;
    exports org.news.newsapp.controller;
    opens org.news.newsapp.controller to javafx.fxml;
    exports org.news.newsapp.service;
    opens org.news.newsapp.service to javafx.fxml;
}