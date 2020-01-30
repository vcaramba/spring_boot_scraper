package scraper.service;

import scraper.model.Article;

import java.util.List;

public interface ScraperService {
    void scrapeArticles();

    List<Article> getScrapedArticlesToAdd();
}
