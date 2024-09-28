package me.mmtr.parrot.service.interfaces;

import me.mmtr.parrot.data.Chat;
import me.mmtr.parrot.data.User;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    Chat saveOrUpdate(Chat chat);
    Optional<Chat> getById(Long id);
    Optional<Chat> getByParticipants(User first, User second);
    List<Chat> getAll();
    void delete(Long id);
}
