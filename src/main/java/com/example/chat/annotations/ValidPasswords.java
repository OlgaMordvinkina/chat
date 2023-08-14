package com.example.chat.annotations;

import com.example.chat.dto.UserRegisterDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class ValidPasswords implements ConstraintValidator<PasswordConstraint, UserRegisterDto> {
    private final String passwordValidationMessage = "Passwords are not equal";

    @Override
    public boolean isValid(UserRegisterDto user, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if (Objects.equals(user.getPassword(), user.getRepeatPassword())) {
            return true;
        } else {
            context.buildConstraintViolationWithTemplate(passwordValidationMessage)
                    .addPropertyNode("passwords")
                    .addConstraintViolation();
        }

        return false;
    }
}
