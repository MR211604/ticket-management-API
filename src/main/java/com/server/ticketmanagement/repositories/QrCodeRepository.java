package com.server.ticketmanagement.repositories;
import com.server.ticketmanagement.domain.entities.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface QrCodeRepository extends JpaRepository<QRCode, UUID> {



}
