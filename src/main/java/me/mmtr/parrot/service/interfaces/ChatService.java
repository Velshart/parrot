package me.mmtr.parrot.service.interfaces;

import me.mmtr.parrot.data.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    void saveOrUpdate(Chat chat);
    Optional<Chat> getById(Long id);
    List<Chat> getAll();
    void delete(Long id);
}
