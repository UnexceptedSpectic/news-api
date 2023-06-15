package com.example.newsapi.xml.impl;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.example.newsapi.exceptions.XmlMapperException;
import com.example.newsapi.models.NewsArticle;
import com.example.newsapi.xml.XmlMapper;

@Component
public class NytXmlMapper implements XmlMapper {

    @Override
    public NewsArticle[] mapArticles(String xml) throws XmlMapperException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            throw new XmlMapperException("Failed to parse XML due to: " + e.getMessage());
        }

        NodeList itemNodes = document.getElementsByTagName("item");
        NewsArticle[] newsArticles = new NewsArticle[itemNodes.getLength()];

        for (int i = 0; i < itemNodes.getLength(); i++) {
            Element itemElement = (Element) itemNodes.item(i);
            String title = getElementContentOrNull(itemElement, "title");
            String date = getElementContentOrNull(itemElement, "pubDate");
            String description = getElementContentOrNull(itemElement, "description");
            String author = getElementContentOrNull(itemElement, "dc:creator");
            String imageUrl = getAttributeOrNull(itemElement, "media:content", "url");
            String imageDescription = getElementContentOrNull(
                    itemElement, "media:description");
            String url = getElementContentOrNull(itemElement, "link");
            NewsArticle newsArticle = new NewsArticle(title, date, description, author, imageUrl, imageDescription,
                    url);
            newsArticles[i] = newsArticle;
        }

        return newsArticles;

    }

}
