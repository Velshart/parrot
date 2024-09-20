package me.mmtr.parrot.data.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotEmpty(message = "Username cannot be empty.")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

}
