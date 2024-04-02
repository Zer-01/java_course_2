package edu.java.configuration;

import edu.java.domain.jpa.JpaChatLinkRepository;
import edu.java.domain.jpa.JpaChatRepository;
import edu.java.domain.jpa.JpaLinkRepository;
import edu.java.domain.repositories.ChatLinkRepository;
import edu.java.domain.repositories.ChatRepository;
import edu.java.domain.repositories.LinkRepository;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public ChatRepository chatRepository(EntityManager entityManager) {
        return new JpaChatRepository(entityManager);
    }

    @Bean
    public LinkRepository linkRepository(EntityManager entityManager) {
        return new JpaLinkRepository(entityManager);
    }

    @Bean
    public ChatLinkRepository chatLinkRepository(EntityManager entityManager) {
        return new JpaChatLinkRepository(entityManager);
    }
}
