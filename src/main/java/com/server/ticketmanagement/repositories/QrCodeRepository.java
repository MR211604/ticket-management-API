package com.server.ticketmanagement.repositories;
import com.server.ticketmanagement.domain.entities.QRCode;
import com.server.ticketmanagement.domain.entities.QRCodeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface QrCodeRepository extends JpaRepository<QRCode, UUID> {

    Optional<QRCode> findByTicketIdAndTicketPurchaserId(UUID ticketId, UUID ticketPurchaserId);
    Optional<QRCode> findByIdAndStatus(UUID id, QRCodeStatus status);
}
