package com.server.ticketmanagement.services;

import com.server.ticketmanagement.domain.entities.Ticket;

import java.util.UUID;

public interface TicketTypeService {

    Ticket purchaseTicket(UUID id, UUID ticketTypeId);

}
