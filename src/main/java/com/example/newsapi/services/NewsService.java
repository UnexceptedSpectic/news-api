package com.example.newsapi.services;

import com.example.newsapi.enums.NewsCategory;
import com.example.newsapi.exceptions.NytServiceException;
import com.example.newsapi.models.NewsArticle;

/**
 * This interface represents a news service.
 * It should be implemented for each news provider.
 */
public interface NewsService {
    NewsArticle[] fetchNewsArticles(NewsCategory newsCategory)
            throws NytServiceException;
}
