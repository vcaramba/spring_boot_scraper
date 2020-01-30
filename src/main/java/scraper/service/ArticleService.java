package scraper.service;

import scraper.model.Article;

import java.util.List;

public interface ArticleService {
    void createArticle(Article article);

    Article getArticle(Integer key);

    void updateArticle(Article article);

    void deleteArticle(Integer key);

    List<Article> getArticles();
}
