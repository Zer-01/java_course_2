package edu.java.scrapper.jpa;

import edu.java.domain.jpa.JpaChatLinkRepository;
import edu.java.entity.Chat;
import edu.java.entity.Link;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JpaChatLinkTest extends IntegrationTest {
    private static final String ADD_CHAT_QUERY = "INSERT INTO chat(id) VALUES(?)";
    private static final String ADD_LINK_QUERY = "INSERT INTO link(id, url) VALUES(?, ?)";
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    JpaChatLinkRepository chatLinkRepository;

    @Test
    @Transactional
    @Rollback
    void findLinksTestEmpty() {
        addTestData();

        List<Link> result = chatLinkRepository.findLinksOfChat(1);

        assertThat(result)
            .isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findLinksTest() {
        addTestData();
        chatLinkRepository.addLinkForChat(1, 1);
        chatLinkRepository.addLinkForChat(2, 1);
        chatLinkRepository.addLinkForChat(1, 3);

        List<Link> result = chatLinkRepository.findLinksOfChat(1);

        List<String> mapResult = result.stream()
            .map(link -> link.getUrl().toString())
            .toList();
        assertThat(mapResult)
            .containsExactlyInAnyOrderElementsOf(List.of("http://link1.com", "http://link3.com"));
    }

    @Test
    @Transactional
    @Rollback
    void findChatsTestEmpty() {
        addTestData();

        List<Link> result = chatLinkRepository.findLinksOfChat(1);

        assertThat(result)
            .isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findChatsTest() {
        addTestData();
        chatLinkRepository.addLinkForChat(1, 1);
        chatLinkRepository.addLinkForChat(2, 1);
        chatLinkRepository.addLinkForChat(1, 3);

        List<Chat> result = chatLinkRepository.findChatsOfLink(1);

        List<Long> mapResult = result.stream()
            .map(Chat::getId)
            .toList();
        assertThat(mapResult)
            .containsExactlyInAnyOrderElementsOf(List.of(1L, 2L));
    }

    @Test
    @Transactional
    @Rollback
    void removeLinkTest() {
        addTestData();
        chatLinkRepository.addLinkForChat(1, 1);
        chatLinkRepository.addLinkForChat(2, 1);
        chatLinkRepository.addLinkForChat(1, 3);

        chatLinkRepository.removeLinkForChat(1, 1);
        List<Link> result = chatLinkRepository.findLinksOfChat(1);

        List<String> mapResult = result.stream()
            .map(link -> link.getUrl().toString())
            .toList();
        assertThat(mapResult)
            .containsExactlyInAnyOrderElementsOf(List.of("http://link3.com"));
    }

    void addTestData() {
        jdbcTemplate.update(ADD_CHAT_QUERY, 1);
        jdbcTemplate.update(ADD_CHAT_QUERY, 2);
        jdbcTemplate.update(ADD_CHAT_QUERY, 3);

        jdbcTemplate.update(ADD_LINK_QUERY, 1, "http://link1.com");
        jdbcTemplate.update(ADD_LINK_QUERY, 2, "http://link2.com");
        jdbcTemplate.update(ADD_LINK_QUERY, 3, "http://link3.com");
    }
}
