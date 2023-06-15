package com.example.newsapi.models;

/**
 * This is main article model that is generated from RSS
 */
public class NewsArticle {
    private String title;
    private String date;
    private String description;
    private String author;
    private String imageUrl;
    private String imageDescription;
    private String url;

    public NewsArticle(
            String title,
            String date,
            String description,
            String author,
            String imageUrl,
            String imageDescription,
            String url) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.author = author;
        this.imageUrl = imageUrl;
        this.imageDescription = imageDescription;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public String getUrl() {
        return url;
    }

}
