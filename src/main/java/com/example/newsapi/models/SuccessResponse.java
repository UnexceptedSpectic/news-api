package com.example.newsapi.models;

/**
 * This is the model for an HTTP response body on service success
 */
public record SuccessResponse(String provider, String category, NewsArticle[] articles) {
}