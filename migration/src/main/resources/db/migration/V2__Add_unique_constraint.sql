ALTER TABLE articles RENAME TO old_articles;

CREATE TABLE articles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    url TEXT NOT NULL,
    CONSTRAINT url_unique UNIQUE (url)
);

INSERT INTO articles (url) SELECT DISTINCT url FROM old_articles;

DROP TABLE old_articles;
