package com.server.ticketmanagement.services;

import com.server.ticketmanagement.domain.CreateEventRequest;
import com.server.ticketmanagement.domain.UpdateEventRequest;
import com.server.ticketmanagement.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventService {

    Event createEvent(UUID organizerId,
                      CreateEventRequest event
    );

    Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable);

    Optional<Event> getEventForOrganizer(UUID organizerId, UUID id);

    Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest event);
}
