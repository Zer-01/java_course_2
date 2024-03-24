package edu.java.scrapper.jdbc;

import edu.java.domain.jdbc.JdbcChatLinkRepository;
import edu.java.domain.jdbc.JdbcChatRepository;
import edu.java.domain.jdbc.JdbcLinkRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@ConfigurationProperties
public class JdbcTestConfig {
    @Bean
    public JdbcChatRepository jdbcChatRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    public JdbcLinkRepository jdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcLinkRepository(jdbcTemplate);
    }

    @Bean
    public JdbcChatLinkRepository jdbcChatLinkRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcChatLinkRepository(jdbcTemplate);
    }
}
