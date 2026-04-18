package com.server.ticketmanagement.mappers;


import com.server.ticketmanagement.domain.dtos.ListTicketResponseDto;
import com.server.ticketmanagement.domain.dtos.ListTicketTicketTypeResponseDto;
import com.server.ticketmanagement.domain.entities.Ticket;
import com.server.ticketmanagement.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    ListTicketTicketTypeResponseDto tolistTicketTicketTypeResponseDto(TicketType ticketType);
    ListTicketResponseDto toListTicketResponseDto(Ticket ticket);
}
