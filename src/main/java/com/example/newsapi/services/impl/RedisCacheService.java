package com.example.newsapi.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.newsapi.enums.NewsCategory;
import com.example.newsapi.enums.NewsProvider;
import com.example.newsapi.models.NewsArticle;
import com.example.newsapi.models.RedisEntry;
import com.example.newsapi.repositories.ArticleCacheRepo;

/**
 * Service for caching news articles in redis
 */
@Service
public class RedisCacheService {

    private final ArticleCacheRepo articleCacheRepo;

    @Value("${spring.data.redis.storage-timeout}")
    private Long timeout;

    public RedisCacheService(ArticleCacheRepo articleCacheRepo) {
        this.articleCacheRepo = articleCacheRepo;
    }

    public void setValue(NewsProvider newsProvider, NewsCategory newsCategory, NewsArticle[] newsArticles) {
        String key = this.createKey(newsProvider, newsCategory);
        RedisEntry redisItem = new RedisEntry(key, newsArticles, this.timeout);
        articleCacheRepo.save(redisItem);
    }

    public RedisEntry getValue(NewsProvider newsProvider, NewsCategory newsCategory) {
        String key = this.createKey(newsProvider, newsCategory);
        return articleCacheRepo.findById(key).orElse(null);
    }

    private String createKey(NewsProvider newsProvider, NewsCategory newsCategory) {
        return newsProvider.name().toLowerCase() + "_" + newsCategory.name().toLowerCase();
    }
}
