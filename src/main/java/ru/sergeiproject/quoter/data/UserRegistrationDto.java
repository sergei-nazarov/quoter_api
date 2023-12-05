package ru.sergeiproject.quoter.data;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationDto {
    @NotNull
    @Size(min = 2, max = 100, message = "Username must contain at least 2 chars")
    @Pattern(regexp = "^(?=[a-zA-Z0-9._]{2,40}$)(?!.*[_.]{2})[^_.].*[^_.]$",
            message = "Username must contain only alphabetical characters, digits, underscore and dot")
            private String username;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email must be email")
    private String email;

    @NotNull
    @Size(min = 2, max = 100, message = "Password must contain at least 2 chars")
    private String password;
}
