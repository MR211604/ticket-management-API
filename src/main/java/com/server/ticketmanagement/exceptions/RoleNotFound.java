package com.server.ticketmanagement.exceptions;

public class RoleNotFound extends EventTicketException {
    public RoleNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RoleNotFound(Throwable cause) {
        super(cause);
    }

    public RoleNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleNotFound(String message) {
        super(message);
    }

    public RoleNotFound() {
    }
}
