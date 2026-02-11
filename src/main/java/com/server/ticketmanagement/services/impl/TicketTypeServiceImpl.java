package com.server.ticketmanagement.services.impl;

import com.server.ticketmanagement.domain.entities.Ticket;
import com.server.ticketmanagement.domain.entities.TicketStatusEnum;
import com.server.ticketmanagement.domain.entities.TicketType;
import com.server.ticketmanagement.domain.entities.User;
import com.server.ticketmanagement.exceptions.TicketSoldOutException;
import com.server.ticketmanagement.exceptions.TicketTypeNotFoundException;
import com.server.ticketmanagement.exceptions.UserNotFoundException;
import com.server.ticketmanagement.repositories.UserRepository;
import com.server.ticketmanagement.services.QrCodeService;
import com.server.ticketmanagement.services.TicketRepository;
import com.server.ticketmanagement.services.TicketTypeRepository;
import com.server.ticketmanagement.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeService qrCodeService;


    @Override
    @Transactional
    public Ticket purchaseTicket(UUID id, UUID ticketTypeId) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(
                        String.format("User with ID %s was not found", id)));

        TicketType ticketType = ticketTypeRepository.findById(ticketTypeId).orElseThrow(() ->
                new TicketTypeNotFoundException(
                        String.format("Ticket type with ID %s was not found", ticketTypeId)));

        int purchasedTickets = ticketRepository.countByTicketTypeId(ticketTypeId);

        Integer totalAvailable = ticketType.getTotalAvailable();

        if (purchasedTickets + 1 > totalAvailable) {
            throw new TicketSoldOutException();
        }

        Ticket ticket = new Ticket();

        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(user);

        Ticket savedTicket = ticketRepository.save(ticket);

        qrCodeService.generateQrCode(savedTicket);

        return ticketRepository.save(savedTicket);

    }
}
