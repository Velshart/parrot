package me.mmtr.parrot.service;

import jakarta.transaction.Transactional;
import me.mmtr.parrot.data.Chat;
import me.mmtr.parrot.data.User;
import me.mmtr.parrot.data.dao.interfaces.IChatDAO;
import me.mmtr.parrot.service.interfaces.ChatService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {

    private final IChatDAO CHAT_DAO ;

    public ChatServiceImpl(IChatDAO chatDAO) {
        this.CHAT_DAO = chatDAO;
    }

    @Override
    @Transactional
    public void saveOrUpdate(Chat chat) {
        CHAT_DAO.saveOrUpdate(chat);
    }

    @Override
    @Transactional
    public Optional<Chat> getById(Long id) {
        return CHAT_DAO.getById(id);
    }

    @Override
    public Optional<Chat> getByParticipants(User first, User second) {
       return this.CHAT_DAO.getAll().stream().filter(chat ->
               chat.getFirstParticipant() == first && chat.getSecondParticipant() == second ||
                       chat.getFirstParticipant() == second && chat.getSecondParticipant() == first)
               .findFirst();
    }

    @Override
    @Transactional
    public List<Chat> getAll() {
        return CHAT_DAO.getAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CHAT_DAO.delete(id);
    }
}
