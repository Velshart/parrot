package me.mmtr.parrot;

import me.mmtr.parrot.controller.HomeController;
import me.mmtr.parrot.service.interfaces.ChatService;
import me.mmtr.parrot.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
public class HomeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ChatService chatService;

    @Test
    @WithMockUser(username = "principal")
    public void shouldReturnHomeViewWithAttributes() throws Exception {

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("chats"))
                .andExpect(model().attributeExists("currentUser"))
                .andExpect(model().attribute("users", userService.findAllUsers()))
                .andExpect(model().attribute("chats", chatService.getAll()))
                .andExpect(model().attribute("currentUser", "principal"));
    }

    @TestConfiguration
    static class TestSecurityConfiguration {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests.requestMatchers("/home").permitAll());

            return http.build();
        }
    }
}
