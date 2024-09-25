package me.mmtr.parrot.controller;

import me.mmtr.parrot.data.Chat;
import me.mmtr.parrot.service.interfaces.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/chat")
public class ChatController {
    private final ChatService CHAT_SERVICE;

    public ChatController(ChatService CHAT_SERVICE) {
        this.CHAT_SERVICE = CHAT_SERVICE;
    }

    @GetMapping("/add")
    public String addChat(Model model) {
        model.addAttribute("chat", new Chat());
        return "chat";
    }

    @PostMapping("/add")
    public String addChat(@ModelAttribute Chat chat) {
        CHAT_SERVICE.saveOrUpdate(chat);
        return "redirect:/home";
    }

    @PostMapping("/update/{id}")
    public String updateChat(@ModelAttribute Chat chat, @PathVariable Long id) {
        CHAT_SERVICE.saveOrUpdate(chat);
        return "redirect:/home";
    }

    @PostMapping("/delete/{id}")
    public String deleteChat(@ModelAttribute Chat chat, @PathVariable Long id) {
        CHAT_SERVICE.delete(id);
        return "redirect:/home";
    }
}
