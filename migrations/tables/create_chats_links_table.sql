CREATE TABLE IF NOT EXISTS chats_links
(
    chat_id BIGINT NOT NULL REFERENCES chats (id) ON DELETE CASCADE,
    link_id BIGINT NOT NULL REFERENCES links (id) ON DELETE CASCADE,
    PRIMARY KEY(chat_id, link_id)
);
