CREATE TABLE IF NOT EXISTS chats
(
    id              BIGINT NOT NULL PRIMARY KEY,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
