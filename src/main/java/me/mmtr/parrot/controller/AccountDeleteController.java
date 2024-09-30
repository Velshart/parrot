package me.mmtr.parrot.controller;

import me.mmtr.parrot.repository.UserRepository;
import me.mmtr.parrot.service.interfaces.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/account")
public class AccountDeleteController {

    private final UserRepository USER_REPOSITORY;
    private final ChatService CHAT_SERVICE;

    public AccountDeleteController(UserRepository userRepository, ChatService chatService) {
        this.USER_REPOSITORY = userRepository;
        this.CHAT_SERVICE = chatService;
    }

    @PostMapping("/delete")
    public String deleteAccount(@RequestParam long id) {
        USER_REPOSITORY.delete(USER_REPOSITORY.findById(id).orElseThrow());
        CHAT_SERVICE.getAll().forEach(chat -> {
            if(chat.getFirstParticipant().getId() == id || chat.getSecondParticipant().getId() == id) {
                CHAT_SERVICE.delete(chat.getId());
            }
        });
        return "redirect:/login";
    }

    @GetMapping("/delete-confirm")
    public String accountDeletionConfirmation( Principal principal, Model model) {
        model.addAttribute("userId", USER_REPOSITORY.findByUsername(principal.getName()).getId());
        return "delete-account-confirmation";
    }
}
