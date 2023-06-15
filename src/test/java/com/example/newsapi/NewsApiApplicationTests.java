package com.example.newsapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.newsapi.models.NewsArticle;
import com.example.newsapi.xml.impl.NytXmlMapper;

@SpringBootTest
class NewsApiApplicationTests {

	@Autowired
	NytXmlMapper nytXmlMapper;

	@Test
	void testNytXmlMapper() {
		// Input
		String input = """
				<rss xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:media=\"http://search.yahoo.com/mrss/\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:nyt=\"http://www.nytimes.com/namespaces/rss/2.0\" version=\"2.0\">
				<channel>
				  <title>NYT > Technology</title>
				  <link>https://www.nytimes.com/section/technology</link>
				  <atom:link href=\"https://rss.nytimes.com/services/xml/rss/nyt/Technology.xml\" rel=\"self\" type=\"application/rss+xml\"/>
				  <description/>
				  <language>en-us</language>
				  <copyright>Copyright 2023 The New York Times Company</copyright>
				  <lastBuildDate>Thu, 08 Jun 2023 16:10:54 +0000</lastBuildDate>
				  <pubDate>Thu, 08 Jun 2023 16:00:32 +0000</pubDate>
				  <image>
					<title>NYT > Technology</title>
					<url>https://static01.nyt.com/images/misc/NYT_logo_rss_250x40.png</url>
					<link>https://www.nytimes.com/section/technology</link>
				  </image>
				  <item>
					<title>A First Try of Apple’s $3,500 Vision Pro Headset</title>
					<link>https://www.nytimes.com/2023/06/06/technology/personaltech/apple-vision-pro-headset-try.html</link>
					<guid isPermaLink=\"true\">https://www.nytimes.com/2023/06/06/technology/personaltech/apple-vision-pro-headset-try.html</guid>
					<atom:link href=\"https://www.nytimes.com/2023/06/06/technology/personaltech/apple-vision-pro-headset-try.html\" rel=\"standout\"/>
					<description>I briefly got my hands on Apple’s new high-tech goggles, which impressed and creeped me out and raised a question: Why do we need these?</description>
					<dc:creator>Brian X. Chen</dc:creator>
					<pubDate>Tue, 06 Jun 2023 18:18:38 +0000</pubDate>
					<category domain=\"http://www.nytimes.com/namespaces/keywords/nyt_org\">Apple Inc</category>
					<category domain=\"http://www.nytimes.com/namespaces/keywords/des\">Virtual Reality (Computers)</category>
					<category domain=\"http://www.nytimes.com/namespaces/keywords/des\">Headphones and Headsets</category>
					<category domain=\"http://www.nytimes.com/namespaces/keywords/des\">Videophones and Videoconferencing</category>
					<media:content height=\"151\" medium=\"image\" url=\"https://static01.nyt.com/images/2023/06/06/multimedia/06techfix-top-fzqj/06techfix-top-fzqj-moth.jpg\" width=\"151\"/>
					<media:credit>Jim Wilson/The New York Times</media:credit>
					<media:description>Apple unveiled the new Vision Pro on Monday. The device will be available next year.</media:description>
				  </item>
				</channel>
				 </rss>
					""";
		;

		// Expected Output
		String expectedTitle = "A First Try of Apple’s $3,500 Vision Pro Headset";
		String expectedDate = "Tue, 06 Jun 2023 18:18:38 +0000";
		String expectedDescription = "I briefly got my hands on Apple’s new high-tech goggles, which impressed and creeped me out and raised a question: Why do we need these?";
		String expectedAuthor = "Brian X. Chen";
		String expectedImageUrl = "https://static01.nyt.com/images/2023/06/06/multimedia/06techfix-top-fzqj/06techfix-top-fzqj-moth.jpg";
		String expectedImageDescription = "Apple unveiled the new Vision Pro on Monday. The device will be available next year.";
		String expectedUrl = "https://www.nytimes.com/2023/06/06/technology/personaltech/apple-vision-pro-headset-try.html";

		Assertions.assertDoesNotThrow(() -> {
			NewsArticle article = nytXmlMapper.mapArticles(input)[0];
			assertEquals(expectedTitle, article.getTitle());
			assertEquals(expectedDate, article.getDate());
			assertEquals(expectedDescription, article.getDescription());
			assertEquals(expectedAuthor, article.getAuthor());
			assertEquals(expectedImageUrl, article.getImageUrl());
			assertEquals(expectedImageDescription, article.getImageDescription());
			assertEquals(expectedUrl, article.getUrl());
		});
	}

}
