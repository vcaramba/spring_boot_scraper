package scraper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scraper.entities.Article;
import scraper.scrape.Scraper;

import java.util.List;

@RestController
public class ScraperController {
    private final Scraper scraper;

    @Autowired
    public ScraperController(Scraper scraper) {
        this.scraper = scraper;
    }

    @RequestMapping("/scrape")
    public List<Article> scrapeArticles() {
        scraper.scrapeArticles();
        return scraper.getAllArticles();
    }

    @RequestMapping("/last_10")
    public List<Article> getLastTenArticles() {
        return scraper.getLastTenArticles();
    }
}
