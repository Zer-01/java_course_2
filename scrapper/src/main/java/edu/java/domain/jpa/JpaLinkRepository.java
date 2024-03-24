package edu.java.domain.jpa;

import edu.java.domain.repositories.LinkRepository;
import edu.java.entity.Link;
import jakarta.persistence.EntityManager;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkRepository implements LinkRepository {
    private final static String FIND_ALL_QUERY = "SELECT l FROM Link l";
    private final static String FIND_BY_URL = "SELECT l FROM Link l WHERE l.url = :url";
    private final static String FIND_CHECKED_EARLY = "SELECT l FROM Link l WHERE l.lastCheckDate < :lastCheckDate";
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public Optional<Link> findByUrl(URI url) {
        return entityManager.createQuery(FIND_BY_URL, Link.class)
            .setParameter("url", url)
            .getResultList().stream()
            .findAny();
    }

    @Transactional
    @Override
    public Link findOrCreate(URI url) {
        Optional<Link> optLink = findByUrl(url);
        if (optLink.isPresent()) {
            return optLink.get();
        } else {
            Link link = new Link(url);
            add(link);
            return link;
        }
    }

    @Transactional
    @Override
    public void update(Link link) {
        entityManager.merge(link);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Link> findCheckedEarlyThan(OffsetDateTime time) {
        return entityManager.createQuery(FIND_CHECKED_EARLY, Link.class)
            .setParameter("lastCheckDate", time)
            .getResultList();
    }

    @Transactional
    @Override
    public void add(Link entity) {
        entity.setLastCheckDate(OffsetDateTime.now());
        entity.setLastModifiedDate(OffsetDateTime.now());
        entityManager.persist(entity);
    }

    @Transactional
    @Override
    public void remove(long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Link> findAll() {
        return entityManager.createQuery(FIND_ALL_QUERY, Link.class).getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Link> findById(long id) {
        return Optional.ofNullable(entityManager.find(Link.class, id));
    }
}
