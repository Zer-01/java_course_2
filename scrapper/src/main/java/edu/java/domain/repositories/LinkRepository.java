package edu.java.domain.repositories;

import edu.java.entity.Link;
import java.net.URI;
import java.util.Optional;

public interface LinkRepository extends Repository<Link> {
    Optional<Link> findByUrl(URI url);

    Link findOrCreate(URI url);
}
