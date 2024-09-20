package me.mmtr.parrot.controller;

import me.mmtr.parrot.data.User;
import me.mmtr.parrot.data.dto.UserDTO;
import me.mmtr.parrot.service.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    private final UserService USER_SERVICE;

    public RegistrationController(UserService USER_SERVICE) {
        this.USER_SERVICE = USER_SERVICE;
    }

    @GetMapping("/register")
    public String registrationForm(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult, Model model) {
        User existingUser = USER_SERVICE.findUserByUsername(userDTO.getUsername());

        if (existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()) {
            bindingResult.rejectValue("username", "exists", "This username is already in use");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDTO);
            return "register";
        }

        USER_SERVICE.saveUser(userDTO);
        return "redirect:/login";
    }
}
