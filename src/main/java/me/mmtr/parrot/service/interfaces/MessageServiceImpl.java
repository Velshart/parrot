package me.mmtr.parrot.service.interfaces;

import me.mmtr.parrot.data.Message;
import me.mmtr.parrot.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository MESSAGE_REPOSITORY;

    public MessageServiceImpl(MessageRepository MESSAGE_REPOSITORY) {
        this.MESSAGE_REPOSITORY = MESSAGE_REPOSITORY;
    }

    @Override
    public void saveMessage(Message message) {
        MESSAGE_REPOSITORY.save(message);
    }

    @Override
    public List<Message> getMessagesByChatId(Long id) {
        return MESSAGE_REPOSITORY.findByChatIdOrderByTimestampAsc(id);
    }
}
