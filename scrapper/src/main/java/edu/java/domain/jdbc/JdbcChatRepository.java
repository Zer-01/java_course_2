package edu.java.domain.jdbc;

import edu.java.domain.repositories.ChatRepository;
import edu.java.entity.Chat;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JdbcChatRepository implements ChatRepository {
    private final static String ADD_QUERY = "INSERT INTO chat(id) VALUES (?)";
    private final static String REMOVE_QUERY = "DELETE FROM chat WHERE id = ?";
    private final static String FIND_ALL_QUERY = "SELECT * FROM chat";
    private final static String FIND_BY_ID_QUERY = "SELECT * FROM chat WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    public final static RowMapper<Chat> CHAT_MAPPER = (ResultSet resultSet, int rowNum) -> new Chat(
        resultSet.getLong("id"),
        resultSet.getObject("created_at", OffsetDateTime.class)
    );

    @Override
    public void add(Chat entity) {
        jdbcTemplate.update(ADD_QUERY, entity.getId());
    }

    @Override
    public void remove(long id) {
        jdbcTemplate.update(REMOVE_QUERY, id);
    }

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, CHAT_MAPPER);
    }

    @Override
    public Optional<Chat> findById(long id) {
        return jdbcTemplate.queryForStream(FIND_BY_ID_QUERY, CHAT_MAPPER, id).findAny();
    }
}
