package me.mmtr.parrot.service;

import me.mmtr.parrot.data.Role;
import me.mmtr.parrot.data.User;
import me.mmtr.parrot.data.dto.UserDTO;
import me.mmtr.parrot.repository.RoleRepository;
import me.mmtr.parrot.repository.UserRepository;
import me.mmtr.parrot.service.interfaces.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository USER_REPOSITORY;
    private final RoleRepository ROLE_REPOSITORY;
    private final PasswordEncoder PASSWORD_ENCODER;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.USER_REPOSITORY = userRepository;
        this.ROLE_REPOSITORY = roleRepository;
        this.PASSWORD_ENCODER = passwordEncoder;
    }

    @Override
    public void saveUser(UserDTO userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(PASSWORD_ENCODER.encode(userDto.getPassword()));

        Role role = checkIfRoleExistsOrCreate();
        user.setRoles(List.of(role));

        USER_REPOSITORY.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return USER_REPOSITORY.findByUsername(username);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        return USER_REPOSITORY.findAll().stream().map(this::mapToUserDto).toList();
    }

    private UserDTO mapToUserDto(User user) {
        UserDTO userDto = new UserDTO();
        String[] str = user.getUsername().split(" ");
        userDto.setUsername(str[0]);
        userDto.setId(user.getId());
        return userDto;
    }

    private Role checkIfRoleExistsOrCreate() {
        Role roleToCheck = ROLE_REPOSITORY.findByName("USER");
        if(roleToCheck == null) {
            Role role = new Role();
            role.setName("USER");
            return ROLE_REPOSITORY.save(role);
        }
        return roleToCheck;
    }
}
