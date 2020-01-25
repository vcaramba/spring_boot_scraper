import scraper.ScraperApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import scraper.entities.Article;

import static org.junit.Assert.*;

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
    public void testGetAllArticles() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/articles",
                HttpMethod.GET, entity, String.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
    }

    @Test
    public void testGetArticleById() {
        int id = 1;
        restTemplate.getForObject(getRootUrl() + "/articles/" + id, Article.class);
        ResponseEntity<Article> postResponse = restTemplate.getForEntity(getRootUrl() + "/articles/" + id,
                Article.class);
        assertEquals(HttpStatus.FOUND, postResponse.getStatusCode());
    }

    @Test
    public void testCreateArticle() {
        Article article = new Article(123, "http://url.com", "title",
                "date", "publishedDate", "author");

        ResponseEntity<Article> postResponse = restTemplate.postForEntity(getRootUrl() + "/articles",
                article, Article.class);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
    }

//    @Test
//    public void testUpdateArticle() {
//        int id = 1;
//        ParsedArticleEntity parsedArticleEntity = restTemplate.getForObject(getRootUrl() + "/articles/" + id, ParsedArticleEntity.class);
//        parsedArticleEntity.setTitle("title1");
//        parsedArticleEntity.setAuthor("author2");
//
//        restTemplate.put(getRootUrl() + "/articles/" + id, parsedArticleEntity);
//
//        restTemplate.getForObject(getRootUrl() + "/articles/" + id, ParsedArticleEntity.class);
//        ResponseEntity<ParsedArticleEntity> postResponse = restTemplate.getForEntity(getRootUrl() + "/articles/" + id,
//                ParsedArticleEntity.class);
//        assertEquals(postResponse.getStatusCode(), HttpStatus.FOUND);
//    }

    @Test
    public void testDeleteArticle() {
        int id = 2;
        restTemplate.getForObject(getRootUrl() + "/articles/" + id, Article.class);
        ResponseEntity<Article> postResponse = restTemplate.getForEntity(getRootUrl() + "/articles/" + id,
                Article.class);
        assertEquals(HttpStatus.FOUND, postResponse.getStatusCode());

        restTemplate.delete(getRootUrl() + "/articles/" + id);

        postResponse = restTemplate.getForEntity(getRootUrl() + "/articles/" + id,
                Article.class);
        assertEquals(HttpStatus.NOT_FOUND, postResponse.getStatusCode());
    }
}
