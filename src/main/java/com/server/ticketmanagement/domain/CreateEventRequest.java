package com.server.ticketmanagement.domain;

import com.server.ticketmanagement.domain.entities.EventStatusEnum;
import com.server.ticketmanagement.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequest {

    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    private String venue;
    private LocalDateTime salesStart;
    private LocalDateTime salesEnd;
    private EventStatusEnum status;

    private List<CreateTicketTypeRequest> ticketTypes = new ArrayList<>();

}
