ALTER TABLE articles RENAME TO old_articles;

CREATE TABLE articles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    url TEXT NOT NULL,
    settings_id INTEGER NOT NULL,
    posted INTEGER NOT NULL,
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL,
    CONSTRAINT url_unique UNIQUE (url, settings_id)
);

INSERT INTO articles (url, settings_id, posted, created_at, updated_at)
SELECT url as url, 1 as settings_id, posted as poted, created_at as created_at, updated_at as updated_at
FROM old_articles;

DROP TABLE old_articles;
