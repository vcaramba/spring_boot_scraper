package scraper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import scraper.service.ScraperServiceImpl;

@RestController
public class ScraperController {

    @Autowired
    private ScraperServiceImpl scraperService;

    @GetMapping("/scrape")
    public void scrape() {
        scraperService.scrapeArticles();
    }

    public ScraperServiceImpl getScraperService() {
        return scraperService;
    }
}
