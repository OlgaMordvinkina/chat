package org.mediagate.core.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.mediagate.core.exceptions.AccessException;
import org.mediagate.db.exceptions.EmailUniqueException;
import org.mediagate.db.exceptions.NotFoundObjectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.core.AuthenticationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(NotFoundObjectException.class)
    protected ResponseEntity<Object> handleConflict(NotFoundObjectException ex) throws JsonProcessingException {
        ApiError response = getResponse(HttpStatus.NOT_FOUND, "Запрашиваемый объект не найден.", ex.getMessage());
        return getResponseEntity(HttpStatus.NOT_FOUND, response);
    }

    @ExceptionHandler(AccessException.class)
    protected ResponseEntity<Object> handleConflict(AccessException ex) throws JsonProcessingException {
        ApiError response = getResponse(HttpStatus.FORBIDDEN, "Для запрашиваемой операции не выполняются условия.", ex.getMessage());
        return getResponseEntity(HttpStatus.FORBIDDEN, response);
    }

    @ExceptionHandler(EmailUniqueException.class)
    protected ResponseEntity<Object> handleConflict(EmailUniqueException ex) throws JsonProcessingException {
        ApiError response = getResponse(HttpStatus.CONFLICT, "Нарушение уникальности персональных данных.", ex.getMessage());
        return getResponseEntity(HttpStatus.CONFLICT, response);
    }

    /** Не работает */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleUnauthorized(AuthenticationException ex) throws JsonProcessingException {
        ApiError response = getResponse(HttpStatus.UNAUTHORIZED, "Неавторизованный запрос.", ex.getMessage());
        return getResponseEntity(HttpStatus.UNAUTHORIZED, response);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleConflict(RuntimeException ex) throws JsonProcessingException {
        ApiError response = getResponse(HttpStatus.UNAUTHORIZED, "Неизвестная ошибка.", ex.getMessage());
        return getResponseEntity(HttpStatus.UNAUTHORIZED, response);
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
