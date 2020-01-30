package scraper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import scraper.model.Article;
import scraper.service.ArticleServiceImpl;

import java.util.List;

@RestController
public class ArticleController {

    @Autowired
    private ArticleServiceImpl articleService;

    @GetMapping("/articles")
    public List<Article> getArticles() {
        return articleService.getArticles();
    }

    @GetMapping("/articles/{key}")
    public Article getArticle(@PathVariable Integer key) {
        return articleService.getArticle(key);
    }

    @DeleteMapping("/articles/{key}")
    public void deleteArticle(@PathVariable Integer key) {
        articleService.deleteArticle(key);
    }

    @PostMapping("/articles/new/")
    public void createArticle(@RequestBody Article article) {
        articleService.createArticle(article);
    }

    @PutMapping("/articles/update/")
    public void updateArticle(@RequestBody Article article) {
        articleService.updateArticle(article);
    }
}
