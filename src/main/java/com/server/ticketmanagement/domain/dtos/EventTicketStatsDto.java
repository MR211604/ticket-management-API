package com.server.ticketmanagement.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventTicketStatsDto {
    private String eventName;
    private List<TicketTypeStatsDto> tickets;
}

