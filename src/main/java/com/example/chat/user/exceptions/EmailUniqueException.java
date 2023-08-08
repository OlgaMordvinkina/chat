package com.example.chat.user.exceptions;

public class EmailUniqueException extends RuntimeException {
    public EmailUniqueException(String email) {
        super("Email address " + email + " already exists");
    }
}
