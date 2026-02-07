package com.server.ticketmanagement.exceptions;

public class TicketTypeNotFoundException extends EventTicketException {
    public TicketTypeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TicketTypeNotFoundException(Throwable cause) {
        super(cause);
    }

    public TicketTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketTypeNotFoundException(String message) {
        super(message);
    }

    public TicketTypeNotFoundException() {
    }
}
