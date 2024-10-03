package me.mmtr.parrot.controller;

import me.mmtr.parrot.data.Chat;
import me.mmtr.parrot.data.Message;
import me.mmtr.parrot.data.User;
import me.mmtr.parrot.repository.UserRepository;
import me.mmtr.parrot.service.interfaces.ChatService;
import me.mmtr.parrot.service.interfaces.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping(path = "/chat")
public class ChatController {
    private final ChatService CHAT_SERVICE;
    private final MessageService MESSAGE_SERVICE;
    private final UserRepository USER_REPOSITORY;

    public ChatController(ChatService chatService, MessageService messageService, UserRepository userRepository) {
        this.CHAT_SERVICE = chatService;
        this.MESSAGE_SERVICE = messageService;
        this.USER_REPOSITORY = userRepository;
    }

    @GetMapping("/{id}")
    public String showChat(@PathVariable Long id, Model model) {
        model.addAttribute("chatId", id);

        List<Message> messages = MESSAGE_SERVICE.getMessagesByChatId(id);
        messages.sort(Comparator.comparing(Message::getTimestamp));

        model.addAttribute("messages", MESSAGE_SERVICE.getMessagesByChatId(id));
        return "chat";
    }

    @GetMapping("/get/{userId}")
    public String getChatOrCreate(@PathVariable Long userId, Principal principal) {
        User first = USER_REPOSITORY.findByUsername(principal.getName());
        User second = USER_REPOSITORY.findById(userId).orElse(null);

        Chat chat = CHAT_SERVICE.getByParticipants(first, second).orElse(null);

        if (chat == null) {
            chat = new Chat();
            chat.setFirstParticipant(first);
            chat.setSecondParticipant(second);

            CHAT_SERVICE.saveOrUpdate(chat);
        }
        return "redirect:/chat/" + chat.getId();
    }

    @PostMapping("/send/{id}")
    public String sendMessage(@PathVariable Long id,
                              @RequestParam String content, Principal principal) {

        Chat chat = CHAT_SERVICE.getById(id).orElseThrow();

        Message messageToSend = new Message();
        messageToSend.setChat(chat);
        messageToSend.setContent(content);

        messageToSend.setTimestamp(LocalDate.now());

        messageToSend.setSender(getPrincipalAsUser(principal, chat));

        MESSAGE_SERVICE.saveMessage(messageToSend);

        return "redirect:/chat/" + id;
    }

    private User getPrincipalAsUser(Principal principal, Chat chat) {
        List<User> chatParticipants = chat.getParticipants();

        return chatParticipants.stream().filter(user -> user.getUsername().equals(principal.getName())).findFirst().orElse(null);
    }
}