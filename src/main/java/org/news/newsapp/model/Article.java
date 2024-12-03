package org.news.newsapp.model;

import org.bson.Document;

/**
 * Class: Article
 * Author: Senuli Wickramage
 * Description:
 * The Article class represents a news article in the application. It contains properties such as title, content,
 * author, image URL, date and time of publication, and the category of the article. The class provides methods to
 * convert an article object to a MongoDB-compatible Document for storage and provides getter and setter methods
 * for each property.
 */
public class Article {
    private int id;
    private String title;
    private String content;
    private String author;
    private String imageUrl;
    private String dateTime;
    private String category;

    /**
     * Constructor to initialize an Article object with the given properties.
     *
     * @param id The unique identifier for the article.
     * @param title The title of the article.
     * @param content The content of the article.
     * @param author The author of the article.
     * @param imageUrl The URL of the image associated with the article.
     * @param dateTime The date and time the article was published.
     * @param category The category of the article (e.g., Politics, Sports, etc.).
     */
    public Article(int id, String title, String content, String author, String imageUrl, String dateTime, String category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.imageUrl = imageUrl;
        this.dateTime = dateTime;
        this.category = category;
    }

    /**
     * Converts the Article object to a string representation for easy display.
     *
     * @return A string representing the Article object.
     */
    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    /**
     * Converts the Article object into a MongoDB-compatible Document for storage.
     *
     * @return A Document representation of the Article object.
     */
    public Document toDocument() {
        Document document = new Document();
        document.append("id", this.id);
        document.append("title", this.title);
        document.append("content", this.content);
        document.append("author", this.author);
        document.append("imageUrl", this.imageUrl);
        document.append("dateTime", this.dateTime);
        document.append("category", this.category);
        return document;
    }

    // Getter and Setter Methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
