package edu.java.domain.jdbc;

import edu.java.domain.repositories.LinkRepository;
import edu.java.entity.Link;
import java.net.URI;
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
    private final JdbcTemplate jdbcTemplate;
    private final static String FIND_BY_URL_QUERY = "SELECT * FROM link WHERE url = ?";
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
          RETURNING id, url, last_modified_date
        )
        SELECT id, url, last_modified_date FROM insert
        UNION ALL
        SELECT id, url, last_modified_date FROM link
            WHERE url = ? AND NOT EXISTS (SELECT 1 FROM insert);
        """;

    private final static RowMapper<Link> LINK_MAPPER = (ResultSet resultSet, int rowNum) -> new Link(
        resultSet.getLong("id"),
        URI.create(resultSet.getString("url")),
        resultSet.getObject("last_modified_date", OffsetDateTime.class)
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
