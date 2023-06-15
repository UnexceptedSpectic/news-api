package com.example.newsapi.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.newsapi.enums.NewsCategory;
import com.example.newsapi.enums.NewsProvider;
import com.example.newsapi.exceptions.NewsManagerException;
import com.example.newsapi.exceptions.NytServiceException;
import com.example.newsapi.models.NewsArticle;
import com.example.newsapi.models.RedisEntry;

@Service
public class NewsManagerService {

    private static final Logger logger = LoggerFactory.getLogger(NytNewsService.class);

    @Autowired
    private NytNewsService nytNewsService;

    @Autowired
    private RedisCacheService articleCacheServiceImpl;

    @Value("${spring.data.redis.enabled}")
    boolean cachingEnabled;

    public NewsArticle[] fetchNewsArticles(NewsProvider newsProvider, NewsCategory newsCategory) {
        // Return cached results if found
        if (cachingEnabled == true) {
            RedisEntry cachedRedisItem = articleCacheServiceImpl.getValue(newsProvider, newsCategory);
            if (cachedRedisItem != null) {
                logger.info("Using value from cache");
                return cachedRedisItem.getNewsArticles();
            }
        }

        // Fetch news for requested provider
        NewsArticle[] newsArticles;
        switch (newsProvider) {
            case NYT:
                try {
                    newsArticles = nytNewsService.fetchNewsArticles(newsCategory);
                } catch (NytServiceException e) {
                    throw new DataRetrievalFailureException(e.getMessage());
                }
                break;

            default:
                String error = "Unsupported provider requested";
                logger.error(error);
                throw new UnsupportedOperationException(error);

        }

        // Cache articles in redis
        if (newsArticles.length > 0 && cachingEnabled == true) {
            try {
                articleCacheServiceImpl.setValue(NewsProvider.NYT, NewsCategory.TECHNOLOGY,
                        newsArticles);
                logger.info("Cached articles successfullly");
            } catch (Exception e) {
                logger.error("Failed to cache articles: {}", e.getMessage());
            }
        }

        return newsArticles;

    }

    @ExceptionHandler({ UnsupportedOperationException.class, DataRetrievalFailureException.class })
    public NewsArticle[] handleHttpClientErrorException(Exception ex) throws NytServiceException, NewsManagerException {
        throw new NewsManagerException("Failed to fetch articles from target news provider due to " + ex.getMessage());
    }

}
