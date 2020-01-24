package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scrape.Scraper;

import java.util.List;

@RestController
public class ScraperController {
    private final Scraper scraper;

    @Autowired
    public ScraperController(Scraper scraper) {
        this.scraper = scraper;
    }

    @RequestMapping("/scrape")
    public List<String> index() {
        return scraper.getScrapedArticles();
    }
}
