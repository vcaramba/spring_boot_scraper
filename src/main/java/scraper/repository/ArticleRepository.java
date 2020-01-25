package scraper.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import scraper.entities.Article;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ArticleRepository {
    private static final String INSERT_ARTICLE = "INSERT INTO ARTICLES (key, url, title, publishedDate, description, author) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public ArticleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
    }


    public Article loadArticle(String key) {
        String querySql = "SELECT * FROM ARTICLES L where L.key = ?";
        return jdbcTemplate.queryForObject(querySql, Article.class, key);
    }

    public void saveArticle(Article article) {
        String checkIfEntryExistsSql = "SELECT count(*) from ARTICLES L WHERE L.key = ?";
        Integer entriesAmount = jdbcTemplate.queryForObject(checkIfEntryExistsSql, Integer.class, article.getKey());
        if (entriesAmount == 0) {
            jdbcTemplate.update(INSERT_ARTICLE, article.getKey(), article.getUrl(), article.getTitle(),
                    article.getPublishedDate(), article.getDescription(), article.getAuthor());
        }
    }

    private List<Article> getArticlesListForQuery(String sqlQuery) {
        List<Article> articles = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlQuery);

        for (Map row : rows) {
            Article article = new Article((Integer) row.get("KEY"),
                    (String) row.get("URL"),
                    (String) row.get("TITLE"),
                    (String) row.get("PUBLISHEDDATE"),
                    (String) row.get("DESCRIPTION"),
                    (String) row.get("AUTHOR"));

            articles.add(article);
        }

        return articles;
    }

    public List<Article> getAllArticles() {
        return getArticlesListForQuery("SELECT * FROM ARTICLES");
    }

    public List<Article> getLastTenArticles() {
        return getArticlesListForQuery("SELECT * FROM ARTICLES ORDER BY key DESC LIMIT 10");
    }

    public List<Article> getArticlesToAdd(List<Article> articles) {
        List<Integer> allKeys = articles.stream().map(Article::getKey).collect(Collectors.toList());
        String getSavedSqlKeys = "SELECT key from ARTICLES L where L.key in (:keys)";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("keys", allKeys);

        List<Integer> existingKeysList = namedJdbcTemplate.queryForList(getSavedSqlKeys, parameters, Integer.class);
        Set<Integer> existingKeysSet = new HashSet<>(existingKeysList);

        return articles.stream()
                .filter((article) -> !existingKeysSet.contains(article.getKey()))
                .collect(Collectors.toList());
    }

    public void saveArticles(List<Article> articlesToAdd) {
        jdbcTemplate.batchUpdate(INSERT_ARTICLE, articlesToAdd, articlesToAdd.size(),
                (ps, article) -> {
                    ps.setInt(1, article.getKey());
                    ps.setString(2, article.getUrl());
                    ps.setString(3, article.getTitle());
                    ps.setString(4, article.getPublishedDate());
                    ps.setString(5, article.getDescription());
                    ps.setString(6, article.getAuthor());

                });

    }
}
