package me.mmtr.parrot.data.dao.interfaces;

import me.mmtr.parrot.data.Chat;

import java.util.List;
import java.util.Optional;

public interface IChatDAO {
    Optional<Chat> getById(Long id);
    List<Chat> getAll();
    void saveOrUpdate(Chat chat);
    void delete(Long id);
}
