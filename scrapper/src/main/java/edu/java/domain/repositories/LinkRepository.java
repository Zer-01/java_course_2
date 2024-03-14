package edu.java.domain.repositories;

import edu.java.entity.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository extends Repository<Link> {
    Optional<Link> findByUrl(URI url);

    Link findOrCreate(URI url);

    void update(Link link);

    List<Link> findCheckedEarlyThan(OffsetDateTime time);
}
