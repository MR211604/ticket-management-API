package com.server.ticketmanagement.services.impl;

import com.server.ticketmanagement.domain.CreateEventRequest;
import com.server.ticketmanagement.domain.UpdateEventRequest;
import com.server.ticketmanagement.domain.UpdateTicketTypeRequest;
import com.server.ticketmanagement.domain.entities.Event;
import com.server.ticketmanagement.domain.entities.EventStatusEnum;
import com.server.ticketmanagement.domain.entities.TicketType;
import com.server.ticketmanagement.domain.entities.User;
import com.server.ticketmanagement.exceptions.EventNotFoundException;
import com.server.ticketmanagement.exceptions.EventUpdateException;
import com.server.ticketmanagement.exceptions.TicketTypeNotFoundException;
import com.server.ticketmanagement.exceptions.UserNotFoundException;
import com.server.ticketmanagement.repositories.EventRepository;
import com.server.ticketmanagement.repositories.UserRepository;
import com.server.ticketmanagement.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Event createEvent(UUID organizerId, CreateEventRequest event) {

        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id '%s' not found", organizerId)
                ));
        Event eventToCreate = new Event();

        List<TicketType> ticketTypesToCreate = event.getTicketTypes().stream().map(
                ticketType ->
                {
                    TicketType ticketTypeToCreate = new TicketType();
                    ticketTypeToCreate.setName(ticketType.getName());
                    ticketTypeToCreate.setPrice(ticketType.getPrice());
                    ticketTypeToCreate.setDescription(ticketType.getDescription());
                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                    ticketTypeToCreate.setEvent(eventToCreate);
                    return ticketTypeToCreate;
                }).toList();

        eventToCreate.setName(event.getName());
        eventToCreate.setStart(event.getStart());
        eventToCreate.setEnd(event.getEnd());
        eventToCreate.setVenue(event.getVenue());
        eventToCreate.setSalesStart(event.getSalesStart());
        eventToCreate.setSalesEnd(event.getSalesEnd());
        eventToCreate.setStatus(event.getStatus());
        eventToCreate.setImage(event.getImage());
        eventToCreate.setOrganizer(organizer);
        eventToCreate.setTicketTypes(ticketTypesToCreate);

        return eventRepository.save(eventToCreate);
    }

    @Override
    public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
        return eventRepository.findByOrganizerId(organizerId, pageable);
    }

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
        return eventRepository.findByIdAndOrganizerId(id, organizerId);
    }

    @Override
    @Transactional
    public Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest event) {
        if (event.getId() == null) {
            throw new EventUpdateException("Event id cannot be null");
        }

        if (!id.equals(event.getId())) {
            throw new EventUpdateException("Cannot update the ID of an event");
        }

        Event existingEvent = eventRepository.findByIdAndOrganizerId(id, organizerId).orElseThrow(() ->
                new EventNotFoundException(String.format("Event with ID '%s' does not exist", id)));

        if (event.getName() != null) existingEvent.setName(event.getName());
        if (event.getStart() != null) existingEvent.setStart(event.getStart());
        if (event.getEnd() != null) existingEvent.setEnd(event.getEnd());
        if (event.getVenue() != null) existingEvent.setVenue(event.getVenue());
        if (event.getSalesStart() != null) existingEvent.setSalesStart(event.getSalesStart());
        if (event.getSalesEnd() != null) existingEvent.setSalesEnd(event.getSalesEnd());
        if (event.getStatus() != null) existingEvent.setStatus(event.getStatus());
        if (event.getImage() != null) existingEvent.setImage(event.getImage());

        if (event.getTicketTypes() != null && !event.getTicketTypes().isEmpty()) {
            Set<UUID> requestTicketTypeIds = event.getTicketTypes().stream().map(UpdateTicketTypeRequest::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            existingEvent.getTicketTypes().removeIf(existingTicketType
                    -> !requestTicketTypeIds.contains(existingTicketType.getId()));

            Map<UUID, TicketType> existingTicketTypesIndex = existingEvent.getTicketTypes().stream()
                    .collect(Collectors.toMap(TicketType::getId, Function.identity()));

            for (UpdateTicketTypeRequest ticketType : event.getTicketTypes()) {
                if (null == ticketType.getId()) {

                    TicketType ticketTypeToCreate = getTicketType(ticketType, existingEvent);
                    existingEvent.getTicketTypes().add(ticketTypeToCreate);

                } else if (existingTicketTypesIndex.containsKey(ticketType.getId())) {

                    TicketType existingTicketType = existingTicketTypesIndex.get(ticketType.getId());
                    if (ticketType.getName() != null) existingTicketType.setName(ticketType.getName());
                    if (ticketType.getPrice() != null) existingTicketType.setPrice(ticketType.getPrice());
                    if (ticketType.getDescription() != null) existingTicketType.setDescription(ticketType.getDescription());
                    if (ticketType.getTotalAvailable() != null) existingTicketType.setTotalAvailable(ticketType.getTotalAvailable());

                } else {
                    throw new TicketTypeNotFoundException(String.format(
                            "Ticket type with ID '%s' does not exist",
                            ticketType.getId()
                    ));
                }
            }
        }

        return eventRepository.save(existingEvent);

    }

    private static @NonNull TicketType getTicketType(UpdateTicketTypeRequest ticketType, Event existingEvent) {
        TicketType ticketTypeToCreate = new TicketType();
        if (ticketType.getName() != null) ticketTypeToCreate.setName(ticketType.getName());
        if (ticketType.getPrice() != null) ticketTypeToCreate.setPrice(ticketType.getPrice());
        if (ticketType.getDescription() != null) ticketTypeToCreate.setDescription(ticketType.getDescription());
        if (ticketType.getTotalAvailable() != null) ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
        ticketTypeToCreate.setEvent(existingEvent);
        return ticketTypeToCreate;
    }

    @Override
    @Transactional
    public void deleteEventForOrganizer(UUID organizerId, UUID id) {
        getEventForOrganizer(organizerId, id).ifPresent(eventRepository::delete);
    }

    @Override
    public Page<Event> listPublishedEvents(Pageable pageable) {
        return eventRepository.findByStatus(EventStatusEnum.PUBLISHED, pageable);
    }

    @Override
    public Page<Event> searchPublishedEvents(String query, Pageable pageable) {
        return eventRepository.searchEvents(query, pageable);
    }

    @Override
    public Optional<Event> getPublishedEvent(UUID id) {
        return eventRepository.findByIdAndStatus(id, EventStatusEnum.PUBLISHED);
    }
}
