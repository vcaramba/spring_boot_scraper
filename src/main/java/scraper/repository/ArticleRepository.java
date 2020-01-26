package scraper.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.stereotype.Repository;
import scraper.model.Article;

import java.sql.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
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


    public Article getArticle(Integer key) {
        String querySql = "SELECT * FROM ARTICLES where key = " + key;
        return jdbcTemplate.queryForObject(querySql, new ArticleRowMapper());
    }

    public void deleteArticle(Integer key) {
        String deleteQuery = "DELETE from ARTICLES where key = ?";
        jdbcTemplate.update(deleteQuery, key);
    }

    public void createArticle(Article article) {
        String checkIfEntryExistsSql = "SELECT count(*) from ARTICLES WHERE key = ?";
        Integer entriesAmount = jdbcTemplate.queryForObject(checkIfEntryExistsSql, Integer.class, article.getKey());
        if (entriesAmount == 0) {
            jdbcTemplate.update(INSERT_ARTICLE, article.getKey(), article.getUrl(),
                    article.getTitle(), article.getPublishedDate(),
                    article.getDescription(), article.getAuthor());
        }
    }

    public List<Article> getAllArticles() {
        return jdbcTemplate.query("SELECT * FROM ARTICLES", new ArticleRowMapper());
    }

    public List<Article> getLastTenArticles() {
        return jdbcTemplate.query("SELECT * FROM ARTICLES ORDER BY key DESC LIMIT 10", new ArticleRowMapper());
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

    public void updateArticle(Integer key, String parameter, String newValue) {
        String SQL = "UPDATE ARTICLES SET " + parameter + " = ? WHERE key = ?";

        SqlUpdate sqlUpdate = new SqlUpdate(jdbcTemplate.getDataSource(), SQL);
        sqlUpdate.declareParameter(new SqlParameter(parameter, Types.VARCHAR));
        sqlUpdate.declareParameter(new SqlParameter("key", Types.INTEGER));
        sqlUpdate.compile();

        sqlUpdate.update(newValue, key);

    }
}
