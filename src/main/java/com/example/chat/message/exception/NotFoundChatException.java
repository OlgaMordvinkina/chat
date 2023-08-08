package com.example.chat.message.exception;

public class NotFoundChatException extends RuntimeException {
    public NotFoundChatException(Long id) {
        super("Chat ID=" + id + " does not exist.");
    }
}
