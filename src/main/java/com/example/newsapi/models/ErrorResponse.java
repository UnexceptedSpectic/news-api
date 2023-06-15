package com.example.newsapi.models;

/**
 * This is the model for an HTTP response body on service failure
 */
public record ErrorResponse(int status, String message) {
}
