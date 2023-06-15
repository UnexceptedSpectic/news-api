package com.example.newsapi.services.impl;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.UncategorizedMappingException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.newsapi.enums.NewsCategory;
import com.example.newsapi.exceptions.NytServiceException;
import com.example.newsapi.exceptions.XmlMapperException;
import com.example.newsapi.models.NewsArticle;
import com.example.newsapi.services.NewsService;
import com.example.newsapi.xml.impl.NytXmlMapper;

/**
 * Service for obtaining news from the NYT
 */
@Service
public class NytNewsService implements NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NytNewsService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NytXmlMapper nytXmlMapperImpl;

    @Value("${rss.nyt.technology}")
    String nytTechnologyRssUrl;

    @Override
    public NewsArticle[] fetchNewsArticles(NewsCategory newsCategory)
            throws NytServiceException {

        switch (newsCategory) {
            case TECHNOLOGY:
                // Request RSS content
                ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(nytTechnologyRssUrl,
                        byte[].class);
                if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                    logger.error("Received a non-200 response from the NYT API");
                    throw new HttpServerErrorException(responseEntity.getStatusCode(),
                            "HTTP request for NYT RSS data failed");
                }
                String xmlContent = new String(responseEntity.getBody(), StandardCharsets.UTF_8);

                // Map RSS xml to articles model
                try {
                    return nytXmlMapperImpl.mapArticles(xmlContent);
                } catch (XmlMapperException e) {
                    logger.error("Failed to map xml to articles model");
                    throw new UncategorizedMappingException(e.getMessage(), e);
                }
            default:
                String error = "Unhandled category encountered for NYT";
                logger.error(error);
                throw new UnsupportedOperationException(error);
        }

    }

    @ExceptionHandler({ HttpServerErrorException.class, UncategorizedMappingException.class,
            UnsupportedOperationException.class
    })
    public NewsArticle[] handleHttpClientErrorException(Exception ex) throws NytServiceException {
        throw new NytServiceException("The NYT service failed to fetch articles due to " + ex.getMessage());
    }

}
