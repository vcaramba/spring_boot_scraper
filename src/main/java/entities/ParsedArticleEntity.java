package entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ParsedArticleEntity {
    private String key;
    private String url;
    private String title;
    private String publishedDate;
    private String postBody;
    private String mapLinkUrl;
    private String author;

    @JsonCreator
    public ParsedArticleEntity(
            @JsonProperty("key") String key,
            @JsonProperty("url") String url,
            @JsonProperty("title") String title,
            @JsonProperty("publishedDate") String publishedDate,
            @JsonProperty("postBody") String postBody,
            @JsonProperty("mapLinkUrl") String mapLinkUrl,
            @JsonProperty("author") String author) {
        this.key = key;
        this.url = url;
        this.title = title;
        this.publishedDate = publishedDate;
        this.postBody = postBody;
        this.mapLinkUrl = mapLinkUrl;
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

    public String getPostBody() {
        return postBody;
    }

    public String getMapLinkUrl() {
        return mapLinkUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public void setMapLinkUrl(String mapLinkUrl) {
        this.mapLinkUrl = mapLinkUrl;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
