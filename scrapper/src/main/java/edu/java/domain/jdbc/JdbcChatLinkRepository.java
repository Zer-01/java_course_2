package edu.java.domain.jdbc;

import edu.java.domain.repositories.ChatLinkRepository;
import edu.java.entity.Chat;
import edu.java.entity.Link;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository {
    private final static String FIND_LINKS_QUERY = """
        SELECT * FROM link l
        JOIN chat_link cl ON cl.link_id = l.id
        WHERE cl.chat_id = ?
        """;
    private final static String FIND_CHATS_QUERY = """
        SELECT * FROM chat c
        JOIN chat_link cl ON cl.chat_id = c.id
        WHERE cl.link_id = ?
        """;
    private final static String ADD_LINK = "INSERT INTO chat_link(chat_id, link_id) VALUES (?, ?)";
    private final static String REMOVE_LINK = "DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Chat> chatMapper;
    private final RowMapper<Link> linkMapper;

    @Override
    public List<Link> findLinksOfChat(long chatId) {
        return jdbcTemplate.query(FIND_LINKS_QUERY, linkMapper, chatId);
    }

    @Override
    public List<Chat> findChatsOfLink(long linkId) {
        return jdbcTemplate.query(FIND_CHATS_QUERY, chatMapper, linkId);
    }

    @Override
    public void addLinkForChat(Chat chat, Link link) {
        addLinkForChat(chat.getId(), link.getId());
    }

    @Override
    public void addLinkForChat(long chatId, long linkId) {
        jdbcTemplate.update(ADD_LINK, chatId, linkId);
    }

    @Override
    public void removeLinkForChat(Chat chat, Link link) {
        removeLinkForChat(chat.getId(), link.getId());
    }

    @Override
    public void removeLinkForChat(long chatId, long linkId) {
        jdbcTemplate.update(REMOVE_LINK, chatId, linkId);
    }
}
