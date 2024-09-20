package me.mmtr.parrot.service.interfaces;

import me.mmtr.parrot.data.User;
import me.mmtr.parrot.data.dto.UserDTO;

import java.util.List;

public interface UserService {
    void saveUser(UserDTO userDto);

    User findUserByUsername(String username);

    List<UserDTO> findAllUsers();
}
