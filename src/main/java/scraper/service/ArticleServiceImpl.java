package scraper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scraper.model.Article;
import scraper.repository.ArticleRepository;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public void createArticle(Article article) {
        articleRepository.createArticle(article);
    }

    @Override
    public Article getArticle(Integer key) {
        return articleRepository.getArticle(key);
    }

    @Override
    public void updateArticle(Article article) {
        articleRepository.updateArticle(article);
    }

    @Override
    public void deleteArticle(Integer key) {
        articleRepository.deleteArticle(key);
    }


    @Override
    public List<Article> getArticles() {
        return articleRepository.getArticles();
    }
}
