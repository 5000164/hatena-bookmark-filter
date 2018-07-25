ALTER TABLE articles RENAME TO old_articles;

CREATE TABLE articles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    url TEXT NOT NULL,
    posted INTEGER NOT NULL,
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL,
    CONSTRAINT url_unique UNIQUE (url)
);

INSERT INTO articles (url, posted, created_at, updated_at)
SELECT
  DISTINCT
  url                   as url,
  1                     as poted,
  strftime('%s', 'now') as created_at,
  strftime('%s', 'now') as updated_at
FROM old_articles;

DROP TABLE old_articles;
