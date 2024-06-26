package edu.java.scrapper.jpa;

import edu.java.domain.jpa.JpaChatRepository;
import edu.java.entity.Chat;
import edu.java.scrapper.IntegrationTest;
import jakarta.persistence.EntityManager;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties(JpaTestConfig.class)
public class JpaChatTest extends IntegrationTest {
    @Autowired
    private JpaChatRepository chatRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    @Rollback
    void emptyTest() {
        assertEquals(0, chatRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Long chatId = 10L;

        chatRepository.add(new Chat(chatId, null));
        entityManager.flush();
        Long resultId = jdbcTemplate.queryForObject("SELECT id FROM chat", Long.class);
        OffsetDateTime resultDate = jdbcTemplate.queryForObject("SELECT created_at FROM chat", OffsetDateTime.class);

        assertEquals(resultId, chatId);
        assertThat(resultDate)
            .isCloseToUtcNow(within(1, ChronoUnit.MINUTES));
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        long chatId = 10L;

        chatRepository.add(new Chat(chatId, null));
        chatRepository.remove(chatId);

        assertEquals(chatRepository.findAll().size(), 0);
    }

    @Test
    @Transactional
    @Rollback
    void findByIdTest() {
        long expId = 4;
        addChats(1L, 10L);

        Optional<Chat> result = chatRepository.findById(expId);

        assertThat(result)
            .isNotEmpty();
        assertEquals(result.get().getId(), expId);
        assertThat(result.get().getCreatedAt())
            .isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void findByIdNullTest() {
        long expId = 1;
        addChats(2L, 11L);

        Optional<Chat> result = chatRepository.findById(expId);

        assertThat(result)
            .isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        addChats(1L, 15L);
        List<Chat> expResult = LongStream.range(1, 16)
            .mapToObj(id -> new Chat(id, OffsetDateTime.now()))
            .toList();

        List<Chat> result = chatRepository.findAll();

        for (Chat chat : result) {
            System.out.println(chat.getId());
        }

        assertThat(result)
            .hasSize(expResult.size());
        assertThat(result)
            .allMatch(chat -> expResult.stream().anyMatch(
                expChat -> chat.getId().equals(expChat.getId())
            ));
    }

    void addChats(Long from, Long to) {
        for (Long i = from; i <= to; i++) {
            chatRepository.add(new Chat(i, null));
        }
    }
}
