module org.news.newsapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.news.newsapp to javafx.fxml;
    exports org.news.newsapp;
}