package edu.java.scrapper.jdbc;

import edu.java.domain.jdbc.JdbcChatLinkRepository;
import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.entity.Chat;
import edu.java.entity.Link;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.net.URI;
import java.sql.ResultSet;
import java.time.OffsetDateTime;

@ConfigurationProperties
public class JdbcTestConfig {
    @Bean
    public RowMapper<Chat> chatRowMapper() {
        return (ResultSet resultSet, int rowNum) -> new Chat(
            resultSet.getLong("id"),
            resultSet.getObject("created_at", OffsetDateTime.class)
        );
    }

    @Bean
    public RowMapper<Link> linkRowMapper() {
        return (ResultSet resultSet, int rowNum) -> new Link(
            resultSet.getLong("id"),
            URI.create(resultSet.getString("url")),
            resultSet.getObject("last_modified_date", OffsetDateTime.class),
            resultSet.getObject("last_check_date", OffsetDateTime.class)
        );
    }

    @Bean
    public JdbcChatRepository jdbcChatRepository(JdbcTemplate jdbcTemplate, RowMapper<Chat> chatRowMapper) {
        return new JdbcChatRepository(jdbcTemplate, chatRowMapper);
    }

    @Bean
    public JdbcLinkRepository jdbcLinkRepository(JdbcTemplate jdbcTemplate, RowMapper<Link> linkRowMapper) {
        return new JdbcLinkRepository(jdbcTemplate, linkRowMapper);
    }

    @Bean
    public JdbcChatLinkRepository jdbcChatLinkRepository(
        JdbcTemplate jdbcTemplate,
        RowMapper<Chat> chatRowMapper,
        RowMapper<Link> linkRowMapper
    ) {
        return new JdbcChatLinkRepository(jdbcTemplate, chatRowMapper, linkRowMapper);
    }
}
