package edu.java.scrapper.jdbc;

import edu.java.domain.repositories.LinkRepository;
import edu.java.entity.Link;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JdbcLinkTest extends IntegrationTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    LinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    void emptyTest() {
        assertEquals(0, linkRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        URI link = URI.create("https://test1.com");
        Link addedLink = new Link(link);

        linkRepository.add(addedLink);
        URI resultLink = jdbcTemplate.queryForObject("SELECT url FROM link WHERE id = ?", URI.class, addedLink.getId());
        OffsetDateTime resultDate =
            jdbcTemplate.queryForObject(
                "SELECT last_modified_date FROM link WHERE id = ?",
                OffsetDateTime.class,
                addedLink.getId()
            );

        assertEquals(link, resultLink);
        assertThat(resultDate)
            .isCloseToUtcNow(within(1, ChronoUnit.MINUTES));
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        URI link = URI.create("https://test1.com");
        Link addedLink = new Link(link);

        linkRepository.add(addedLink);
        linkRepository.remove(addedLink.getId());

        assertEquals(linkRepository.findAll().size(), 0);
    }

    @Test
    @Transactional
    @Rollback
    void findByIdTest() {
        URI link = URI.create("https://test1.com");
        URI link2 = URI.create("https://test2.com");
        Link addedLink = new Link(link);
        Link addedLink2 = new Link(link2);
        linkRepository.add(addedLink);
        linkRepository.add(addedLink2);

        Optional<Link> result = linkRepository.findById(addedLink.getId());

        assertThat(result)
            .isNotEmpty();
        assertEquals(result.get().getUrl(), addedLink.getUrl());
        assertThat(result.get().getLastModifiedDate())
            .isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void findByIdNullTest() {
        URI link = URI.create("https://test1.com");
        Link addedLink = new Link(link);
        linkRepository.add(addedLink);

        Optional<Link> result = linkRepository.findById(addedLink.getId() + 10);

        assertThat(result)
            .isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findByUrlTest() {
        URI link = URI.create("https://test1.com");
        URI link2 = URI.create("https://test2.com");
        Link addedLink = new Link(link);
        Link addedLink2 = new Link(link2);
        linkRepository.add(addedLink);
        linkRepository.add(addedLink2);

        Optional<Link> result = linkRepository.findByUrl(addedLink.getUrl());

        assertThat(result)
            .isNotEmpty();
        assertEquals(result.get().getUrl(), addedLink.getUrl());
        assertThat(result.get().getLastModifiedDate())
            .isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void findOrCreateTestFind() {
        URI link = URI.create("https://test1.com");
        Link addedLink = new Link(link);
        linkRepository.add(addedLink);

        Link result = linkRepository.findOrCreate(link);

        assertEquals(result.getUrl(), addedLink.getUrl());
        assertThat(result.getLastModifiedDate())
            .isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void findOrCreateTestCreate() {
        URI link = URI.create("https://test1.com");

        Link addedLink = linkRepository.findOrCreate(link);
        Optional<Link> result = linkRepository.findByUrl(link);

        assertThat(result)
            .isNotEmpty();
        assertEquals(result.get().getUrl(), addedLink.getUrl());
        assertThat(result.get().getLastModifiedDate())
            .isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void updateTest() {
        URI link = URI.create("https://test1.com");
        OffsetDateTime newTime = OffsetDateTime.now().plusMonths(10);
        Link addedLink = new Link(link);
        linkRepository.add(addedLink);
        addedLink = linkRepository.findByUrl(addedLink.getUrl()).get();
        addedLink.setLastCheckDate(newTime);

        linkRepository.update(addedLink);
        Link result = linkRepository.findById(addedLink.getId()).get();

        assertEquals(newTime.toEpochSecond(), result.getLastCheckDate().toEpochSecond());
    }

    @Test
    @Transactional
    @Rollback
    void timeSelectTest() {
        URI link = URI.create("https://test1.com");
        URI link2 = URI.create("https://test2.com");
        Link addedLink = new Link(link);
        Link addedLink2 = new Link(link2);
        linkRepository.add(addedLink);
        linkRepository.add(addedLink2);
        OffsetDateTime time = OffsetDateTime.now().minusDays(1);
        addedLink2 = linkRepository.findByUrl(addedLink2.getUrl()).get();
        addedLink2.setLastCheckDate(time);
        linkRepository.update(addedLink2);

        List<Link> result = linkRepository.findCheckedEarlyThan(OffsetDateTime.now().minusHours(1));

        assertThat(result)
            .hasSize(1);
        assertEquals(result.get(0).getUrl(), link2);
    }
}
