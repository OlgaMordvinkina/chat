package com.example.chat.exceptions;

public class NotFoundObjectException extends RuntimeException {
    public NotFoundObjectException(String message) {
        super(message);
    }
}
