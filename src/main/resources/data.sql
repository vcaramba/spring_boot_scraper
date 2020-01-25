DROP TABLE IF EXISTS ARTICLES;

CREATE TABLE ARTICLES (
  key  INTEGER(25)   NOT NULL PRIMARY KEY,
  url  VARCHAR(256)  NOT NULL,
  title VARCHAR(256) NOT NULL,
  publishedDate VARCHAR(50) NOT NULL,
  description VARCHAR(700),
  author VARCHAR(50) NOT NULL
);