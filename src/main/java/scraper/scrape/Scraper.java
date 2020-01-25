package scraper.scrape;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import scraper.entities.Article;
import scraper.repository.ArticleRepository;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);
    private static final Pattern KEY_PATTERN = Pattern.compile("https://www.scmp.com/(.*?)/article/(.[0-9]+?)/(.*?)");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    @Value("${scraper.url}")
    private String SCRAPER_URL;
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    public Scraper(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getLastTenArticles() {
        return articleRepository.getLastTenArticles();
    }

    public List<Article> getAllArticles() {
        return articleRepository.getAllArticles();
    }

    @Scheduled(fixedDelay = 10000)
    public void scrapeArticles() {
        LOGGER.info("Fixed Delay Task: Current Time - {}" + formatter.format(LocalDateTime.now()));
        try {
            URL feedUrl = new URL(SCRAPER_URL);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            List<SyndEntry> entries = feed.getEntries();
            List<Article> articlesToAdd =
                    articleRepository.getArticlesToAdd(entries.stream().map(this::scrapedArticle).collect(Collectors.toList()));

            articleRepository.saveArticles(articlesToAdd);

        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.error("Error occurred while saving articles: " + ex.getMessage());
        }

    }

    private Article scrapedArticle(SyndEntry entry) {
        String url = entry.getUri();
        String key = "";
        Matcher matcher = KEY_PATTERN.matcher(url);
        if (matcher.matches()) {
            key = matcher.group(2);
        } else {
            LOGGER.error("Error while parsing URL {}, not valid", url);
        }

        String title = entry.getTitle();
        String publishedDate = entry.getPublishedDate().toString();
        String description = entry.getDescription().getValue();
        String author = entry.getAuthor();

        return new Article(Integer.parseInt(key), url, title, publishedDate, description, author);
    }
}
