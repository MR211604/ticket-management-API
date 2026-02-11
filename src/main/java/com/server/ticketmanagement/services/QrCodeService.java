package com.server.ticketmanagement.services;


import com.server.ticketmanagement.domain.entities.QRCode;
import com.server.ticketmanagement.domain.entities.Ticket;

public interface QrCodeService {

    QRCode generateQrCode(Ticket ticket);
}
