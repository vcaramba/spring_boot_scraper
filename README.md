# spring_boot_scraper
Spring Boot &amp; JDBC scraper that saves results to H2 in-memory database.
URL for scraping: http://quotes.toscrape.com/

1. mvn install
2. mvn spring-boot:run

Access in-memory DB:

1. http://localhost:8080/h2
2. URL: jdbc:h2:mem:testdb
3. user: sa, empty password

Endpoints:
1. /scrape: scrape articles from RSS feed (scheduled task with delay 10 sec)
2. /last_10: get last 10 articles (sorted by article id)

