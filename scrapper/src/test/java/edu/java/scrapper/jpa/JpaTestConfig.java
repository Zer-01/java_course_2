package edu.java.scrapper.jpa;

import edu.java.domain.jpa.JpaChatLinkRepository;
import edu.java.domain.jpa.JpaChatRepository;
import edu.java.domain.jpa.JpaLinkRepository;
import jakarta.persistence.EntityManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties
public class JpaTestConfig {
    @Bean
    public JpaChatRepository jpaChatRepository(EntityManager entityManager) {
        return new JpaChatRepository(entityManager);
    }

    @Bean
    public JpaLinkRepository jpaLinkRepository(EntityManager entityManager) {
        return new JpaLinkRepository(entityManager);
    }

    @Bean
    public JpaChatLinkRepository jpaChatLinkRepository(EntityManager entityManager) {
        return new JpaChatLinkRepository(entityManager);
    }
}
