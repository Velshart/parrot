package me.mmtr.parrot.repository;

import me.mmtr.parrot.data.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatIdOrderByTimestampAsc(Long chatId);
}
