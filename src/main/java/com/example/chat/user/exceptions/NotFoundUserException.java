package com.example.chat.user.exceptions;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(Long id) {
        super("User ID=" + id + " does not exist.");
    }
}
