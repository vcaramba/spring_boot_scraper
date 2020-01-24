package entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RawArticleEntity {
    private final String key;
    private final String url;
    private final String title;
    private final String publishedDate;
    private final String description;
    private final String author;

    @JsonCreator
    public RawArticleEntity(
            @JsonProperty("key") String key,
            @JsonProperty("url") String url,
            @JsonProperty("title") String title,
            @JsonProperty("publishedDate") String publishedDate,
            @JsonProperty("description") String description,
            @JsonProperty("author") String author) {
        this.key = key;
        this.url = url;
        this.title = title;
        this.publishedDate = publishedDate;
        this.description = description;
        this.author = author;
    }

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }
}