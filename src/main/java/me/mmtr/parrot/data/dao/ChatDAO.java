package me.mmtr.parrot.data.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import me.mmtr.parrot.data.Chat;
import me.mmtr.parrot.data.dao.interfaces.IChatDAO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ChatDAO implements IChatDAO {

    @PersistenceContext
    private EntityManager entityManager;
    private final String GET_ALL_JPQL = "FROM me.mmtr.parrot.data.Chat";
    private final String GET_BY_ID_JPQL = "SELECT c FROM me.mmtr.parrot.data.Chat c WHERE c.id = :id";

    public ChatDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Chat> getById(Long id) {
        TypedQuery<Chat> query = entityManager.createQuery(GET_BY_ID_JPQL, Chat.class);
        query.setParameter("id", id);

        try {
            return Optional.of(query.getSingleResult());
        }catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Chat> getAll() {
        TypedQuery<Chat> query = entityManager.createQuery(GET_ALL_JPQL, Chat.class);
        return query.getResultList();
    }

    @Override
    public Chat saveOrUpdate(Chat chat) {
        if(getById(chat.getId()).isEmpty()) {
            entityManager.persist(chat);
        }else {
            entityManager.merge(chat);
        }
        return chat;
    }

    @Override
    public void delete(Long id) {
        getById(id).ifPresent(entityManager::remove);
    }
}
