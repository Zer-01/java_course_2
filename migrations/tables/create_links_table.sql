CREATE TABLE IF NOT EXISTS link
(
    id              BIGSERIAL NOT NULL PRIMARY KEY,
    url             VARCHAR NOT NULL UNIQUE,
    last_modified_date     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_check_date     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
