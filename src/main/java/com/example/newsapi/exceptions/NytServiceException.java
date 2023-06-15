package com.example.newsapi.exceptions;

public class NytServiceException extends Exception {
    public NytServiceException(String message) {
        super(message);
    }
}