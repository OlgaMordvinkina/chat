package com.example.chat.dto;

import com.example.chat.annotations.PasswordConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@PasswordConstraint
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDto {
    public static final String messagePattern = "The field can contain only latin letters and numbers";
    public static final String patternPassword = "[a-zA-Z0-9]*";

    @Email
    @NotBlank
    @Size(min = 6, max = 256)
    private String email;
    @NotBlank
    @Pattern(regexp = patternPassword, message = messagePattern)
    @Size(min = 8, max = 256)
    private String password;
    @NotBlank
    @Pattern(regexp = patternPassword, message = messagePattern)
    @Size(min = 8, max = 256)
    private String repeatPassword;
    @NotBlank
    @Size(min = 2, max = 30)
    private String name;
    @NotBlank
    @Size(min = 2, max = 30)
    private String surname;
}
