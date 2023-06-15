package com.example.newsapi.enums;

import java.util.Arrays;

/**
 * This enum tracks the implemented news providers.
 */
public enum NewsProvider {
    NYT;

    public static NewsProvider getFrom(String stringRepresentation) {
        switch (stringRepresentation.toLowerCase()) {
            case "nyt":
                return NewsProvider.NYT;
            default:
                return null;
        }
    }

    public static boolean hasProvider(String queryProvider) {
        return Arrays.stream(NewsProvider.values())
                .anyMatch(newsType -> newsType.name().equalsIgnoreCase(queryProvider));
    }

    public static NewsCategory[] getSupportedCategories(NewsProvider newsProvider) {
        switch (newsProvider) {
            case NYT:
                NewsCategory[] supportCategories = { NewsCategory.TECHNOLOGY };
                return supportCategories;
            default:
                return new NewsCategory[0];
        }
    }

    public static boolean providerSupportsCategory(NewsProvider newsProvider, NewsCategory newsCategory) {
        NewsCategory[] supportedCategories = NewsProvider.getSupportedCategories(newsProvider);
        return Arrays.stream(supportedCategories)
                .anyMatch(category -> category.name().equals(newsCategory.name()));
    }
}