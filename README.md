# spring_boot_scraper
Spring Boot &amp; JDBC scraper that saves results to H2 in-memory database.
URL for scraping: https://www.scmp.com/rss/3/feed (South China Morning Post RSS feed)

Run application:

1. mvn install
2. mvn spring-boot:run

Access in-memory DB:

1. http://localhost:8080/h2
2. URL: jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE;MV_STORE=FALSE
3. user: sa, empty password

Endpoints:
1. **/scrape_scheduled**: scrape articles from RSS feed (scheduled task with delay 10 sec)
2. **/articles/last_10**: get last 10 articles (sorted by article id) from the in-memory database
3. **/articles**: get all scraped articles from the in-memory database
4. **/articles/id**: get article by id

