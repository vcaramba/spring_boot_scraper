package scraper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import scraper.model.Article;
import scraper.service.ScraperServiceImpl;

import java.util.List;

@RestController
public class ScraperController {

    private final ScraperServiceImpl scraper;


    @Autowired
    public ScraperController(ScraperServiceImpl scraper) {
        this.scraper = scraper;
    }

    @GetMapping("/scrape_scheduled")
    public void scrape_scheduled() {
        scraper.scrapeArticlesWithDelay();
    }

    public void scrape() {
        scraper.scrapeArticles();
    }

    @GetMapping("/articles")
    public List<Article> getAllArticles() {
        return scraper.getAllArticles();
    }

    @GetMapping("/articles/{key}")
    public Article getArticle(@PathVariable Integer key) {
        return scraper.getArticle(key);
    }

    @DeleteMapping("/articles/{key}")
    public void deleteArticle(@PathVariable Integer key) {
        scraper.deleteArticle(key);
    }

    @PostMapping("/articles/new/")
    public void createArticle(@RequestBody Article article) {
        scraper.createArticle(article);
    }

    @PutMapping("/articles/{key}/{parameter}={newValue}")
    public void updateArticle(@PathVariable("key") Integer key, @PathVariable("parameter") String parameter,
                              @PathVariable("newValue") String newValue) {
        scraper.updateArticle(key, parameter, newValue);
    }

    @GetMapping("/articles/last_10")
    public List<Article> getLastTenArticles() {
        return scraper.getLastTenArticles();
    }
}
