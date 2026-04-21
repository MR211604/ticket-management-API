package com.server.ticketmanagement.services;


import com.server.ticketmanagement.domain.entities.QRCode;
import com.server.ticketmanagement.domain.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {

    QRCode generateQrCode(Ticket ticket);

    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}
