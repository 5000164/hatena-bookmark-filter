CREATE TABLE articles (
  id          IDENTITY PRIMARY KEY,
  url         VARCHAR   NOT NULL,
  settings_id TINYINT   NOT NULL,
  posted      BOOLEAN   NOT NULL,
  created_at  TIMESTAMP NOT NULL,
  updated_at  TIMESTAMP NOT NULL,
  UNIQUE (url, settings_id)
);
