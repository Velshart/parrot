package me.mmtr.parrot.controller;

import me.mmtr.parrot.service.interfaces.ChatService;
import me.mmtr.parrot.service.interfaces.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {
    private final UserService USER_SERVICE;
    private final ChatService CHAT_SERVICE;

    public HomeController(UserService userService, ChatService chatService) {
        this.USER_SERVICE = userService;
        this.CHAT_SERVICE = chatService;
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        model.addAttribute("users", USER_SERVICE.findAllUsers());
        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("chats", CHAT_SERVICE.getAll());

        return "home";
    }
}
