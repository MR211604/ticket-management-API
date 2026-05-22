package com.server.ticketmanagement.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerStatisticsResponseDto {
    private double totalSalesAmount;
    private long totalSoldTickets;
    private long totalTicketsValidated;
    private long totalEventsCreated;
    private List<EventTicketStatsDto> ticketsSoldByOrganizer;

}

