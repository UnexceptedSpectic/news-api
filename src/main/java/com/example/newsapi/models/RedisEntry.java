package com.example.newsapi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * This is the model that is used to cache articles in redis
 */
@RedisHash("RedisObject")
public class RedisEntry {
    @Id
    private String id;

    @TimeToLive
    private Long timeout;

    private NewsArticle[] newsArticles;

    public RedisEntry(String id, NewsArticle[] newsArticles, Long timeout) {
        this.id = id;
        this.newsArticles = newsArticles;
        this.timeout = timeout;
    }

    public NewsArticle[] getNewsArticles() {
        return newsArticles;
    }
}
