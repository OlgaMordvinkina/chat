package org.mediagate.db.exceptions;

public class EmailUniqueException extends RuntimeException {
    public EmailUniqueException(String email) {
        super("Адрес электронной почты " + email + " уже существует в БД");
    }
}
