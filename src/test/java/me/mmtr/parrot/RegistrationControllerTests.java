package me.mmtr.parrot;

import me.mmtr.parrot.controller.RegistrationController;
import me.mmtr.parrot.data.User;
import me.mmtr.parrot.data.dto.UserDTO;
import me.mmtr.parrot.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void shouldReturnRegistrationViewWithAnAttribute() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", new UserDTO()));
    }

    @Test
    public void shouldReturnAnErrorWhenUserAlreadyExists() throws Exception {
        Mockito.when(userService.findUserByUsername(Mockito.anyString()))
                .thenReturn(new User(1L,
                        "username",
                        "password",
                        Collections.emptyList(),
                        Collections.emptyList()));

        mockMvc.perform(post("/register")
                        .param("username", "username")
                        .param("password", "password")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasFieldErrors("user", "username"));
    }

    @Test
    public void shouldSuccessfullyRegisterUserAndRedirectToLoginPage() throws Exception {
        Mockito.when(userService.findUserByUsername(Mockito.anyString())).thenReturn(null);

        mockMvc.perform(post("/register")
                        .param("username", "username")
                        .param("password", "password")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @TestConfiguration
    static class TestSecurityConfiguration {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authorizeRequests ->
                            authorizeRequests.requestMatchers("/register").permitAll())
                    .csrf(AbstractHttpConfigurer::disable);

            return http.build();
        }
    }
}
