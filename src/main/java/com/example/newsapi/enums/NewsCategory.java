package com.example.newsapi.enums;

import java.util.Arrays;

/**
 * This enum tracks news categories implemented one or more providers.
 */
public enum NewsCategory {
    TECHNOLOGY;

    public static boolean hasCategory(String queryCategory) {
        return Arrays.stream(NewsCategory.values())
                .anyMatch(newsType -> newsType.name().equalsIgnoreCase(queryCategory));
    }

    public static NewsCategory getFrom(String stringRepresentation) {
        switch (stringRepresentation.toLowerCase()) {
            case "technology":
                return NewsCategory.TECHNOLOGY;
            default:
                return null;
        }
    }
}