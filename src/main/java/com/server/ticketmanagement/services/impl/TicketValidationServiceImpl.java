package com.server.ticketmanagement.services.impl;

import com.server.ticketmanagement.domain.entities.*;
import com.server.ticketmanagement.exceptions.QrCodeNotFoundException;
import com.server.ticketmanagement.exceptions.TicketNotFoundException;
import com.server.ticketmanagement.exceptions.UserNotFoundException;
import com.server.ticketmanagement.repositories.QrCodeRepository;
import com.server.ticketmanagement.repositories.TicketRepository;
import com.server.ticketmanagement.repositories.TicketValidationRepository;
import com.server.ticketmanagement.repositories.UserRepository;
import com.server.ticketmanagement.services.TicketValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketValidationServiceImpl implements TicketValidationService {

    private final QrCodeRepository qrCodeRepository;
    private final TicketValidationRepository ticketValidationRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    private @NonNull TicketValidation validateTicket(Ticket ticket, TicketValidationMethod ticketValidationMethod, UUID staffId) {
        User staff = userRepository.findById(staffId).orElseThrow(UserNotFoundException::new);
        User purchaser = ticket.getPurchaser();
        Event event = ticket.getTicketType().getEvent();

        TicketValidationStatusEnum ticketValidationStatus = ticket.getValidations().stream()
                .filter(v -> TicketValidationStatusEnum.VALID.equals(v.getStatus()))
                .findFirst()
                .map(v -> TicketValidationStatusEnum.INVALID)
                .orElse(TicketValidationStatusEnum.VALID);

        if (TicketValidationStatusEnum.VALID.equals(ticketValidationStatus)) {
            boolean alreadyStaffing = staff.getStaffingEvents().stream()
                    .anyMatch(e -> e.getId().equals(event.getId()));
            if (!alreadyStaffing) {
                staff.getStaffingEvents().add(event);
            }

            if (purchaser != null) {
                boolean alreadyAttending = purchaser.getAttendingEvents().stream()
                        .anyMatch(e -> e.getId().equals(event.getId()));
                if (!alreadyAttending) {
                    purchaser.getAttendingEvents().add(event);
                }
            }
        }

        TicketValidation ticketValidation = new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setValidationMethod(ticketValidationMethod);
        ticketValidation.setStatus(ticketValidationStatus);

        return ticketValidationRepository.save(ticketValidation);
    }

    @Override
    public TicketValidation validateTicketByQrCode(UUID qrCodeId, UUID staffId) {
        QRCode qrCode = qrCodeRepository.findByIdAndStatus(qrCodeId, QRCodeStatus.ACTIVE)
                .orElseThrow(() -> new QrCodeNotFoundException(
                        String.format("QR code with ID %s was not found", qrCodeId)
                ));
        Ticket ticket = qrCode.getTicket();

        return validateTicket(ticket, TicketValidationMethod.QR_SCAN, staffId);

    }

    @Override
    public TicketValidation validateTicketManually(UUID ticketId, UUID staffId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);
        return validateTicket(ticket, TicketValidationMethod.MANUAL, staffId);
    }
}
