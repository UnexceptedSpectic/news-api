package com.example.newsapi.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.example.newsapi.models.NewsArticle;

public interface XmlMapper {
    NewsArticle[] mapArticles(String xml) throws Exception;

    default String getElementContentOrNull(Element parentElement, String tag) {
        Node node = parentElement.getElementsByTagName(tag).item(0);
        if (node != null) {
            return node.getTextContent();
        }
        return null;
    }

    default String getAttributeOrNull(Element parentElement, String tag, String attribute) {
        Node node = parentElement.getElementsByTagName(tag).item(0);
        Element element = (Element) node;
        if (element != null) {
            return element.getAttribute("url");
        }
        return null;
    }
}