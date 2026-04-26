package com.server.ticketmanagement.domain.dtos;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTicketTypeRequestDto {

    private UUID id;

    private String name;

    @PositiveOrZero(message = "Price must be equal or greater than zero")
    private Double price;

    private String description;

    private Integer totalAvailable;
}
