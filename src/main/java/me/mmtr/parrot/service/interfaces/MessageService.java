package me.mmtr.parrot.service.interfaces;

import me.mmtr.parrot.data.Message;

import java.util.List;

public interface MessageService {
    void saveMessage(Message message);
    List<Message> getMessagesByChatId(Long id);
}
