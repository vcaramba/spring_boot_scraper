import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import scraper.ScraperApplication;
import scraper.controller.ScraperController;
import scraper.model.Article;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ScraperControllerTest {

    @Autowired
    private ScraperController controller;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    private HttpHeaders headers = new HttpHeaders();
    private HttpEntity<String> entity = new HttpEntity<>(null, headers);

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Before
    public void setup() throws JSONException {
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/articles",
                HttpMethod.GET, entity, String.class);

        if (new JSONArray(response.getBody()).length() == 0) {
            controller.scrape();
        }
    }

    @Test
    public void testGetAllArticles() throws JSONException {
        List<Article> articles = controller.getAllArticles();

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/articles",
                HttpMethod.GET, entity, String.class);

        assertEquals(articles.size(), new JSONArray(response.getBody()).length());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetArticleById() throws JSONException {
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/articles",
                HttpMethod.GET, entity, String.class);

        JSONObject firstArticle = new JSONArray(response.getBody()).getJSONObject(0);

        ResponseEntity<Article> getResponse = restTemplate.getForEntity(getRootUrl() + "/articles/" + firstArticle.get("key"),
                Article.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
    }

    @Test
    public void testGetLastTenArticles() throws JSONException {
        List<Article> articles = controller.getLastTenArticles();

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/articles/last_10",
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(articles.size(), new JSONArray(response.getBody()).length());
        assertEquals(10, new JSONArray(response.getBody()).length());
    }

    @Test
    public void testDeleteArticle() throws JSONException {
        ResponseEntity<String> getResponse = restTemplate.exchange(getRootUrl() + "/articles",
                HttpMethod.GET, entity, String.class);

        int initialArticlesAmount = new JSONArray(getResponse.getBody()).length();
        JSONObject firstArticle = new JSONArray(getResponse.getBody()).getJSONObject(0);

        ResponseEntity<String> deleteResponse = restTemplate.exchange(getRootUrl() + "/articles/" + firstArticle.get("key"),
                HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        getResponse = restTemplate.exchange(getRootUrl() + "/articles",
                HttpMethod.GET, entity, String.class);
        assertEquals(initialArticlesAmount - 1, new JSONArray(getResponse.getBody()).length());
    }

    @Test
    public void testCreateArticle() {
        Article article = new Article(123, "http://url.com", "title",
                "date", "publishedDate", "author");

        ResponseEntity<Article> postResponse = restTemplate.postForEntity(getRootUrl() + "/articles/new/",
                article, Article.class);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());

        postResponse = restTemplate.getForEntity(getRootUrl() + "/articles/" + article.getKey(),
                Article.class);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
    }

    @Test
    public void testUpdateArticle() {
        List<Article> articles = controller.getLastTenArticles();
        ResponseEntity<String> getResponse = restTemplate.exchange(getRootUrl() + "/articles/last_10",
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        int tenthArticleKey = articles.get(articles.size() - 1).getKey();

        Article article = restTemplate.getForObject(getRootUrl() + "/articles/" + tenthArticleKey,
                Article.class);
        article.setTitle(RandomStringUtils.randomAlphanumeric(20));

        ResponseEntity<Article> putResponse = restTemplate.exchange(getRootUrl() + "/articles/" + article.getKey() +
                "/title=" + article.getTitle(), HttpMethod.PUT, entity, Article.class);
        assertEquals(HttpStatus.OK, putResponse.getStatusCode());

        Article updatedArticle = restTemplate.getForObject(getRootUrl() + "/articles/" + tenthArticleKey,
                Article.class);
        assertEquals(article.getTitle(), updatedArticle.getTitle());
    }

}
