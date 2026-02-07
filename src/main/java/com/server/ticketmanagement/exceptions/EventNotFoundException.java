package com.server.ticketmanagement.exceptions;

public class EventNotFoundException extends EventTicketException {
    public EventNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EventNotFoundException(Throwable cause) {
        super(cause);
    }

    public EventNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException() {
    }
}
