package me.mmtr.parrot.controller;

import me.mmtr.parrot.service.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {
    private final UserService USER_SERVICE;

    public HomeController(UserService userService) {
        this.USER_SERVICE = userService;
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        model.addAttribute("users", USER_SERVICE.findAllUsers());
        model.addAttribute("currentUser", principal.getName());
        return "home";
    }
}
