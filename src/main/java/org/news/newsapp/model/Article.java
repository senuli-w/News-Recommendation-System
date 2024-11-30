package org.example.diagramnewsrecommendation.model;

import org.bson.Document;

public class Article {
    private int id;
    private String title;
    private String content;
    private String author;
    private String imageUrl;
    private String dateTime;
    private String category;

    public Article(int id, String title, String content, String author, String imageUrl, String dateTime, String category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.imageUrl = imageUrl;
        this.dateTime = dateTime;
        this.category = category;
    }

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
