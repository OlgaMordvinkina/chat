package org.mediagate.auth.exceptions;

import lombok.Getter;

@Getter
public class AccessControlException extends RuntimeException {
    final AccessControlExceptionCode code;

    public AccessControlException(AccessControlExceptionCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public AccessControlException(AccessControlExceptionCode code, String message) {
        super(code.getMessage() + " " + message);
        this.code = code;
    }

    public AccessControlException(AccessControlExceptionCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public AccessControlException(AccessControlExceptionCode code, String message, Throwable cause) {
        super(code.getMessage() + " " + message, cause);
        this.code = code;
    }

}
