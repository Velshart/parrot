package me.mmtr.parrot.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chats")
public class Chat {
    @Id
    private Long id;

    @OneToMany(mappedBy = "chats", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

}