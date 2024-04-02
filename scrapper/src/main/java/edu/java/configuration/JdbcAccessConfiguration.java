package edu.java.configuration;

import edu.java.entity.Chat;
import edu.java.entity.Link;
import java.net.URI;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;

@ConfigurationProperties
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
}
