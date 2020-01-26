package scraper.entities;

public class Article {
    private int key;
    private String url;
    private String title;
    private String publishedDate;
    private String description;
    private String author;

    public Article() {
    }

    public Article(int key, String url, String title, String publishedDate, String description, String author) {
        this.key = key;
        this.url = url;
        this.title = title;
        this.publishedDate = publishedDate;
        this.description = description;
        this.author = author;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
