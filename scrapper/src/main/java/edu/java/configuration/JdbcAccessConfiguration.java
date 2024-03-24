package edu.java.configuration;

import edu.java.domain.jdbc.JdbcChatLinkRepository;
import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import edu.java.domain.repositories.ChatLinkRepository;
import edu.java.domain.repositories.ChatRepository;
import edu.java.domain.repositories.LinkRepository;
import edu.java.entity.Chat;
import edu.java.entity.Link;
import java.net.URI;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
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
    public ChatRepository chatRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcChatRepository(jdbcTemplate, chatRowMapper());
    }

    @Bean
    public LinkRepository linkRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcLinkRepository(jdbcTemplate, linkRowMapper());
    }

    @Bean
    public ChatLinkRepository chatLinkRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcChatLinkRepository(jdbcTemplate, chatRowMapper(), linkRowMapper());
    }

}
