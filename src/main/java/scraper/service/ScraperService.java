package scraper.service;

import scraper.model.Article;

import java.util.List;

public interface ScraperService {
    void createArticle(Article article);

    Article getArticle(Integer key);

    void updateArticle(Integer key, String parameter, String newValue);

    void deleteArticle(Integer key);

    void scrapeArticlesWithDelay();

    List<Article> getScrapedArticlesToAdd();

    List<Article> getAllArticles();
}
