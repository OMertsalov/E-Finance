package com.efinance.account.authorization;

import com.efinance.account.user.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
public class RegistrationForm {
    @NotBlank(message = "Provide name please")
    private String firstName;
    @NotBlank(message = "Provide last name please")
    private String lastName;
    @Size(min = 5, message = "username must be minimum 5")
    private String login;
    @Size(min=5, message = "password should be min 5")
    private String password;
    @Pattern(regexp = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$", message = "email is not valid")
    private String email;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(
                firstName, lastName, login, passwordEncoder.encode(password), email);
    }
}
