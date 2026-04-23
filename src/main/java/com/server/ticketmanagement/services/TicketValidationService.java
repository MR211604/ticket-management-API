package com.server.ticketmanagement.services;

import com.server.ticketmanagement.domain.entities.TicketValidation;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface TicketValidationService {

    TicketValidation validateTicketByQrCode(UUID qrCodeId, UUID staffId);
    TicketValidation validateTicketManually(UUID ticketId, UUID staffId);

}
