package repository;

import entities.RawArticleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ArticleRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public ArticleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
    }

    public RawArticleEntity loadRawArticle(String key) {
        String querySql = "SELECT * FROM ARTICLES.RAW_NTITIES L where L.key = ?";
        return jdbcTemplate.queryForObject(querySql, RawArticleEntity.class, key);
    }

    public void saveRawArticle(RawArticleEntity rawArticleEntity) {
        String checkIfListingExistsSql = "SELECT count(*) from ARTICLES.RAW_ENTITIES L WHERE L.key = ?";
        Integer listingCount = jdbcTemplate.queryForObject(checkIfListingExistsSql, Integer.class, rawArticleEntity.getKey());
        if (listingCount == 0) {
            String insertListingSql = "INSERT INTO ARTICLES.RAW_ENTITIES (key, url, title, publishedDate, description, author) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertListingSql, rawArticleEntity.getKey(), rawArticleEntity.getUrl(), rawArticleEntity.getTitle(),
                    rawArticleEntity.getPublishedDate(), rawArticleEntity.getDescription());
        }
    }

    public List<String> saveRawArticles(List<RawArticleEntity> rawArticleEntities) {
        List<String> allKeys = rawArticleEntities.stream().map(RawArticleEntity::getKey).collect(Collectors.toList());
        String getSavedSqlKeys = "SELECT key from ARTICLES.RAW_ENTITIES L where L.key in (:keys)";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("keys", allKeys);
        List<String> existingKeysList = namedJdbcTemplate.queryForList(getSavedSqlKeys, parameters, String.class);
        Set<String> existingKeysSet = new HashSet<>(existingKeysList);

        List<RawArticleEntity> articlesToAdd = rawArticleEntities.stream()
                .filter((listing) -> !existingKeysSet.contains(listing.getKey()))
                .collect(Collectors.toList());

        String insertListingSql = "INSERT INTO ARTICLES.RAW_ENTITIES (key, url, title, publishedDate, description, author) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(insertListingSql, articlesToAdd, articlesToAdd.size(),
                (ps, listing) -> {
                    ps.setString(1, listing.getKey());
                    ps.setString(2, listing.getUrl());
                    ps.setString(3, listing.getTitle());
                    ps.setString(4, listing.getPublishedDate());
                    ps.setString(5, listing.getDescription());
                    ps.setString(6, listing.getAuthor());
                });

        return articlesToAdd.stream().map(RawArticleEntity::getKey).collect(Collectors.toList());
    }
}
