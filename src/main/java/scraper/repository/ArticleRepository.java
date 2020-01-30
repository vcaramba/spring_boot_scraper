package scraper.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import scraper.model.Article;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class ArticleRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;


    public Article getArticle(Integer key) {
        String querySql = "SELECT * FROM ARTICLES where key = " + key;
        return jdbcTemplate.queryForObject(querySql, BeanPropertyRowMapper.newInstance(Article.class));
    }

    public void deleteArticle(Integer key) {
        String deleteQuery = "DELETE from ARTICLES where key = ?";
        jdbcTemplate.update(deleteQuery, key);
    }

    public void createArticle(Article article) {
        String insertQuery = "INSERT INTO ARTICLES (key, url, title, publishedDate, description, author) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertQuery, article.getKey(), article.getUrl(),
                article.getTitle(), article.getPublishedDate(),
                article.getDescription(), article.getAuthor());

    }

    public List<Article> getArticles() {
        return jdbcTemplate.query("SELECT * FROM ARTICLES ORDER BY key DESC LIMIT 10",
                BeanPropertyRowMapper.newInstance(Article.class));
    }

    public List<Article> getArticlesToAdd(List<Article> articles) {
        List<Integer> allKeys = articles.stream().map(Article::getKey).collect(Collectors.toList());
        String getSavedSqlKeys = "SELECT key from ARTICLES where key in (:keys)";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("keys", allKeys);

        List<Integer> existingKeysList = namedJdbcTemplate.queryForList(getSavedSqlKeys, parameters, Integer.class);
        Set<Integer> existingKeysSet = new HashSet<>(existingKeysList);

        return articles.stream()
                .filter((article) -> !existingKeysSet.contains(article.getKey()))
                .collect(Collectors.toList());
    }

    public void saveArticles(List<Article> articlesToAdd) {
        String insertQuery = "INSERT INTO ARTICLES (key, url, title, publishedDate, description, author) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(insertQuery, articlesToAdd, articlesToAdd.size(),
                (ps, article) -> {
                    ps.setInt(1, article.getKey());
                    ps.setString(2, article.getUrl());
                    ps.setString(3, article.getTitle());
                    ps.setString(4, article.getPublishedDate());
                    ps.setString(5, article.getDescription());
                    ps.setString(6, article.getAuthor());
                });
    }

    public void updateArticle(Article article) {
        String updateQuery = "UPDATE articles SET " +
                "url = ?, title = ?, publishedDate = ?, description = ?, author = ? " +
                "WHERE key = ?";
        jdbcTemplate.update(updateQuery
                , article.getUrl()
                , article.getTitle()
                , article.getPublishedDate()
                , article.getDescription()
                , article.getAuthor()
                , article.getKey());
    }
}
