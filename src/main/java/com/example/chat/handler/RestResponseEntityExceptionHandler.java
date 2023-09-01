package com.example.chat.handler;

import com.example.chat.exceptions.AccessException;
import com.example.chat.exceptions.EmailUniqueException;
import com.example.chat.exceptions.NotFoundObjectException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(NotFoundObjectException.class)
    protected ResponseEntity<Object> handleConflict(NotFoundObjectException ex) throws JsonProcessingException {
        ApiError response = getResponse(HttpStatus.NOT_FOUND, "The required object was not found.", ex.getMessage());
        return getResponseEntity(HttpStatus.NOT_FOUND, response);
    }

    @ExceptionHandler(AccessException.class)
    protected ResponseEntity<Object> handleConflict(AccessException ex) throws JsonProcessingException {
        ApiError response = getResponse(HttpStatus.FORBIDDEN, "For the requested operation the conditions are not met.", ex.getMessage());
        return getResponseEntity(HttpStatus.FORBIDDEN, response);
    }

    @ExceptionHandler(EmailUniqueException.class)
    protected ResponseEntity<Object> handleConflict(EmailUniqueException ex) throws JsonProcessingException {
        ApiError response = getResponse(HttpStatus.CONFLICT, "Violation of the uniqueness of personal data.", ex.getMessage());
        return getResponseEntity(HttpStatus.CONFLICT, response);
    }

    private ApiError getResponse(HttpStatus notFound, String reason, String message) {
        return ApiError.builder()
                .status(notFound.name())
                .reason(reason)
                .message(message)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    private ResponseEntity<Object> getResponseEntity(HttpStatus status, ApiError response) throws JsonProcessingException {
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(response));
    }
}
