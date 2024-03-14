package edu.java.domain.jdbc;

import edu.java.domain.repositories.LinkRepository;
import edu.java.entity.Link;
import java.net.URI;
import java.security.InvalidParameterException;
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
public class JdbcLinkRepository implements LinkRepository {
    private final static String FIND_BY_URL_QUERY = "SELECT * FROM link WHERE url = ?";
    private final static String UPDATE_QUERY = """
        UPDATE link
        SET last_modified_date = ?, last_check_date = ?
        WHERE id = ?
        """;
    private final static String FIND_CHECKED_EARLY_QUERY = """
        SELECT * FROM link
        WHERE last_check_date < ?
        """;
    private final static String ADD_QUERY = """
        INSERT INTO link(url) VALUES (?)
        RETURNING id
        """;
    private final static String REMOVE_QUERY = "DELETE FROM link WHERE id = ?";
    private final static String FIND_ALL_QUERY = "SELECT * FROM link";
    private final static String FIND_BY_ID_QUERY = "SELECT * FROM link WHERE id = ?";
    private final static String FIND_OR_CREATE_QUERY = """
        WITH insert AS (
          INSERT INTO link (url)
          VALUES (?)
          ON CONFLICT (url) DO NOTHING
          RETURNING id, url, last_modified_date, last_check_date
        )
        SELECT id, url, last_modified_date, last_check_date FROM insert
        UNION ALL
        SELECT id, url, last_modified_date, last_check_date FROM link
            WHERE url = ? AND NOT EXISTS (SELECT 1 FROM insert);
        """;
    private final JdbcTemplate jdbcTemplate;

    public final static RowMapper<Link> LINK_MAPPER = (ResultSet resultSet, int rowNum) -> new Link(
        resultSet.getLong("id"),
        URI.create(resultSet.getString("url")),
        resultSet.getObject("last_modified_date", OffsetDateTime.class),
        resultSet.getObject("last_check_date", OffsetDateTime.class)
    );

    @Override
    public Optional<Link> findByUrl(URI url) {
        return jdbcTemplate.queryForStream(FIND_BY_URL_QUERY, LINK_MAPPER, url.toString()).findAny();
    }

    @Override
    public Link findOrCreate(URI url) {
        return jdbcTemplate.queryForObject(FIND_OR_CREATE_QUERY, LINK_MAPPER, url.toString(), url.toString());
    }

    @Override
    public void update(Link link) {
        if (link.getId() < 0 || link.getLastCheckDate() == null || link.getLastModifiedDate() == null) {
            throw new InvalidParameterException();
        }
        jdbcTemplate.update(UPDATE_QUERY, link.getLastModifiedDate(), link.getLastCheckDate(), link.getId());
    }

    @Override
    public List<Link> findCheckedEarlyThan(OffsetDateTime time) {
        return jdbcTemplate.query(FIND_CHECKED_EARLY_QUERY, LINK_MAPPER, time);
    }

    @Override
    public void add(Link entity) {
        entity.setId(jdbcTemplate.queryForObject(ADD_QUERY, Long.class, entity.getUrl().toString()));
    }

    @Override
    public void remove(long id) {
        jdbcTemplate.update(REMOVE_QUERY, id);
    }

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, LINK_MAPPER);
    }

    @Override
    public Optional<Link> findById(long id) {
        return jdbcTemplate.queryForStream(FIND_BY_ID_QUERY, LINK_MAPPER, id).findAny();
    }
}
