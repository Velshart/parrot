package me.mmtr.parrot;

import me.mmtr.parrot.controller.ChatController;
import me.mmtr.parrot.data.Chat;
import me.mmtr.parrot.data.Message;
import me.mmtr.parrot.data.User;
import me.mmtr.parrot.repository.UserRepository;
import me.mmtr.parrot.service.interfaces.ChatService;
import me.mmtr.parrot.service.interfaces.MessageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
public class ChatControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void shouldReturnChatViewWithAttributes() throws Exception {
        List<Message> messages = List.of(new Message(
                1L, new Chat(), new User(), "Content", LocalDate.now()));

        Mockito.when(messageService.getMessagesByChatId(1L)).thenReturn(messages);
        mockMvc.perform(get("/chat/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("chat"))
                .andExpect(model().attributeExists("chatId"))
                .andExpect(model().attributeExists("messages"))
                .andExpect(model().attribute("chatId", 1L))
                .andExpect(model().attribute("messages", messages));
    }

    @Test
    @WithMockUser(username = "principal")
    public void shouldGetChatByIdAndRedirectToChatView() throws Exception {
        User user = new User(1L, "username", "password", List.of(), List.of());
        Chat chat = new Chat(1L, user, user, List.of());

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        Mockito.when(chatService.getByParticipants(chat.getFirstParticipant(), chat.getSecondParticipant()))
                .thenReturn(Optional.of(chat));

        mockMvc.perform(get("/chat/get/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/chat/1"));
    }

    @TestConfiguration
    static class TestSecurityConfiguration {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authorizeRequests ->
                            authorizeRequests
                                    .requestMatchers("/chat/**").permitAll()
                    )
                    .csrf(AbstractHttpConfigurer::disable);

            return http.build();
        }
    }
}
