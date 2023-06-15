package com.example.newsapi.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.newsapi.models.RedisEntry;

public interface ArticleCacheRepo extends CrudRepository<RedisEntry, String> {
}