package scraper.repository;

import org.springframework.jdbc.core.RowMapper;
import scraper.entities.Article;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleRowMapper implements RowMapper<Article> {
    @Override
    public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
        Article article = new Article();

        article.setKey(rs.getInt("key"));
        article.setUrl(rs.getString("url"));
        article.setTitle(rs.getString("title"));
        article.setPublishedDate(rs.getString("publishedDate"));
        article.setDescription(rs.getString("description"));
        article.setAuthor(rs.getString("author"));

        return article;
    }
}
