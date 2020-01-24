import app.ScraperApplication;
import entities.ParsedArticleEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void testGetAllListings() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/articles",
                HttpMethod.GET, entity, String.class);

        assertNotNull(response.getBody());
    }

    @Test
    public void testGetListingById() {
        ParsedArticleEntity parsedArticleEntity = restTemplate.getForObject(getRootUrl() + "/articles/1", ParsedArticleEntity.class);
        assertNotNull(parsedArticleEntity);
    }

    @Test
    public void testCreateParsedListing() {
        ParsedArticleEntity parsedArticleEntity = new ParsedArticleEntity("key", "http://url.com", "title",
                "date", "publishedDate", "mapLink", "author");

        ResponseEntity<ParsedArticleEntity> postResponse = restTemplate.postForEntity(getRootUrl() + "/articles",
                parsedArticleEntity, ParsedArticleEntity.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
    }

    @Test
    public void testUpdateParsedListing() {
        int id = 1;
        ParsedArticleEntity parsedArticleEntity = restTemplate.getForObject(getRootUrl() + "/articles/" + id, ParsedArticleEntity.class);
        parsedArticleEntity.setTitle("title1");
        parsedArticleEntity.setAuthor("author2");

        restTemplate.put(getRootUrl() + "/articles/" + id, parsedArticleEntity);

        ParsedArticleEntity updatedParsedArticleEntity = restTemplate.getForObject(getRootUrl() + "/articles/" + id, ParsedArticleEntity.class);
        assertNotNull(updatedParsedArticleEntity);
    }

    @Test
    public void testDeleteParsedListing() {
        int id = 2;
        ParsedArticleEntity parsedArticleEntity = restTemplate.getForObject(getRootUrl() + "/articles/" + id, ParsedArticleEntity.class);
        assertNotNull(parsedArticleEntity);

        restTemplate.delete(getRootUrl() + "/articles/" + id);

        try {
            restTemplate.getForObject(getRootUrl() + "/articles/" + id, ParsedArticleEntity.class);
        } catch (final HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }
}
