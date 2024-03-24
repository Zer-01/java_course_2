package edu.java.domain.jpa;

import edu.java.domain.repositories.ChatRepository;
import edu.java.entity.Chat;
import jakarta.persistence.EntityManager;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaChatRepository implements ChatRepository {
    private final static String FIND_ALL_QUERY = "SELECT c FROM Chat c";
    private final EntityManager entityManager;

    @Transactional
    @Override
    public void add(Chat entity) {
        entity.setCreatedAt(OffsetDateTime.now());
        entityManager.persist(entity);
    }

    @Transactional
    @Override
    public void remove(long id) {
        findById(id).ifPresent(entityManager::remove);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Chat> findAll() {
        return entityManager.createQuery(FIND_ALL_QUERY, Chat.class).getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Chat> findById(long id) {
        return Optional.ofNullable(entityManager.find(Chat.class, id));
    }
}
