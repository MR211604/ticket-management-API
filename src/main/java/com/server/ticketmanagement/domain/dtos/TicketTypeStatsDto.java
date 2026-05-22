package com.server.ticketmanagement.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketTypeStatsDto {
    private String name;
    private Double price;
    private Double totalSold;
}

