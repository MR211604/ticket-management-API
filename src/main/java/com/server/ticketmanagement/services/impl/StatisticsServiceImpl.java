package com.server.ticketmanagement.services.impl;

import com.server.ticketmanagement.domain.dtos.OrganizerStatisticsResponseDto;
import com.server.ticketmanagement.repositories.EventRepository;
import com.server.ticketmanagement.repositories.TicketRepository;
import com.server.ticketmanagement.repositories.TicketValidationRepository;
import com.server.ticketmanagement.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final TicketValidationRepository ticketValidationRepository;

    @Override
    @Transactional(readOnly = true)
    public OrganizerStatisticsResponseDto getOrganizerStatistics(UUID organizerId, LocalDateTime startDate, LocalDateTime endDate) {
        long totalEventsCreated = eventRepository.countByOrganizerId(organizerId);

        double totalSalesAmount = ticketRepository.sumTicketsSoldByOrganizerAndDate(organizerId, startDate, endDate);

        long totalSoldTickets = ticketRepository.countTicketsSoldByOrganizerAndDate(organizerId, startDate, endDate);

        long totalTicketsValidated = ticketValidationRepository.countValidatedTicketsByOrganizer(organizerId);

        return OrganizerStatisticsResponseDto.builder()
                .totalEventsCreated(totalEventsCreated)
                .totalSalesAmount(totalSalesAmount)
                .totalSoldTickets(totalSoldTickets)
                .totalTicketsValidated(totalTicketsValidated)
                .build();
    }
}

