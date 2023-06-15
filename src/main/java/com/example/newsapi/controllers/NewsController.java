package com.example.newsapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.example.newsapi.enums.NewsCategory;
import com.example.newsapi.enums.NewsProvider;
import com.example.newsapi.models.ErrorResponse;
import com.example.newsapi.models.NewsArticle;
import com.example.newsapi.models.SuccessResponse;
import com.example.newsapi.services.impl.NewsManagerService;

/**
 * This is a controller defining the news REST API.
 */
@RestController
@RequestMapping("/api")
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private NewsManagerService newsManagerService;

    // TODO: Add language query parameter for Spanish support
    /**
     * The articles endpoint
     * Request format: GET
     * {@code HOSTNAME}/api/articles?provider={@code PROVIDER}&category={@code CATEGORY}
     * Verify parameter support in {@code NewsProvider} and {@code NewsCategory}
     * 
     * @param newsProvider The news provider e.g. 'nyt'
     * @param newsCategory The news category e.g. 'technology'
     * @return
     */
    @GetMapping("/articles")
    public ResponseEntity<SuccessResponse> getArticles(
            @RequestParam(value = "provider", defaultValue = "nyt") String newsProvider,
            @RequestParam(value = "category", defaultValue = "technology") String newsCategory) {
        logger.info("Incoming /news request");
        // Fail requests with invalid query params
        if (!NewsProvider.hasProvider(newsProvider)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "An invalid news provider was requested");
        }
        if (!NewsCategory.hasCategory(newsCategory)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "An invalid news category was requested");
        }
        NewsProvider mappedNewsProvider = NewsProvider.getFrom(newsProvider);
        NewsCategory mappedNewsCategory = NewsCategory.getFrom(newsCategory);
        if (!NewsProvider.providerSupportsCategory(mappedNewsProvider, mappedNewsCategory)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                    "An invalid combination of provider and category was requested");
        }
        // Fetch articles for the requested news provider and category
        NewsArticle[] newsArticles = newsManagerService.fetchNewsArticles(mappedNewsProvider, mappedNewsCategory);

        SuccessResponse successResponse = new SuccessResponse(newsProvider, newsCategory, newsArticles);
        if (newsArticles.length == 0) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "No news articles found");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(successResponse, headers, HttpStatus.OK);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleBadRequests(HttpClientErrorException ex) {
        HttpStatusCode status = ex.getStatusCode();
        ErrorResponse errorResponse = new ErrorResponse(status.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleServerErrors(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An internal server error occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
