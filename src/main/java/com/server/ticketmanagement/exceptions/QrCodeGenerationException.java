package com.server.ticketmanagement.exceptions;

public class QrCodeGenerationException extends EventTicketException {
    public QrCodeGenerationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public QrCodeGenerationException(Throwable cause) {
        super(cause);
    }

    public QrCodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public QrCodeGenerationException(String message) {
        super(message);
    }

    public QrCodeGenerationException() {
    }
}
