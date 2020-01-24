package scrape;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import entities.RawArticleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import repository.ArticleRepository;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);
    @Value("${scraper.url}")
    private String SCRAPER_URL;
    private static final Pattern KEY_PATTERN = Pattern.compile("<item>[\\S]+<link>[\\S]+</link>[\\S]+</item>");
    private final ArticleRepository articleRepository;

    @Autowired
    public Scraper(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<String> getScrapedArticles() {
        try {
            URL feedUrl = new URL(SCRAPER_URL);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            List<SyndEntry> entries = feed.getEntries();
            List<RawArticleEntity> rawArticleEntities = entries.stream().map(this::scrapeListing).collect(Collectors.toList());

            return articleRepository.saveRawArticles(rawArticleEntities);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: " + ex.getMessage());
        }
        return Collections.emptyList();
    }

    private RawArticleEntity scrapeListing(SyndEntry entry) {
        String url = entry.getUri();
        String key = "";
        Matcher matcher = KEY_PATTERN.matcher(url);
        if (matcher.matches()) {
            key = matcher.group(1);
        } else {
            LOGGER.error("Error while parsing URL {}, not valid", url);
        }

        String title = entry.getTitle();
        String publishedDate = entry.getPublishedDate().toString();
        String description = entry.getDescription().getValue();
        String author = entry.getAuthor();
        System.out.println("AUTHOR: " + author);

        return new RawArticleEntity(key, url, title, publishedDate, description, author);
    }
}
