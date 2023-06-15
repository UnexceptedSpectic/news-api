# News REST API

## About

This is a RESTful API that provides news articles from RSS feeds.

## Features

### Requirements

- Fetch articles by the combination of news provider and news category
- In-memory caching powered by Redis
  - Timeout configurable as `spring.data.redis.storage-timeout` (seconds)

#### Supported Providers and Categories

- `nyt`
  - `technology`

### TODO

- Add news providers and news categories
- Aggregate results across providers based on category

## Usage

### Configuration

1. Rename `application.yml.example` to `application.yml` in `src/main/resources/`
2. Update `application.yml` to configure your redis cache

### Running

For development, run `./gradlew bootRun`

### Interacting

#### Endpoint

- http://`host`:`port`/api/articles

#### Optional query parameters

- `provider`
  - Default value is `nyt`
- `category`
  - Default value is `technology`

### Testing

Run `./gradlew test`
