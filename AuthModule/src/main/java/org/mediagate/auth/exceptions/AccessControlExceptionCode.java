package org.mediagate.auth.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccessControlExceptionCode {
    ACCESS_DENIED(571, "Недостаточно прав для действия."),
    OBJECT_NOT_FOUND(572, "Объект не найден."),
    USER_NOT_AUTHENTICATED(573, "Пользователь не аутентифицирован."),
    ANNOTATION_INVALID_USAGE(574, "Неправильное использование аннотаций модуля."),
    UNSUPPORTED_OBJECT_TYPE(575, "Неподдерживаемый тип объекта");
    private final int code;
    private final String message;
}