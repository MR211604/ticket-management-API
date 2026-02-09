package com.server.ticketmanagement.controllers;


import com.server.ticketmanagement.domain.dtos.GetPublishedEventDetailsResponseDto;
import com.server.ticketmanagement.domain.dtos.ListPublishedEventResponseDto;
import com.server.ticketmanagement.domain.entities.Event;
import com.server.ticketmanagement.mappers.EventMapper;
import com.server.ticketmanagement.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventsController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvents(
            Pageable pageable,
            @RequestParam(required = false) String q
    ) {

        Page<Event> events;

        if (q != null && !q.trim().isEmpty()) {
            events = eventService.searchPublishedEvents(q, pageable);
        } else {
            events = eventService.listPublishedEvents(pageable);
        }

        return ResponseEntity.ok(events.map(eventMapper::toListPublishedEventResponseDto));
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<GetPublishedEventDetailsResponseDto> getPublishedEventDetails(
            @PathVariable UUID eventId
    ) {
        return eventService.getPublishedEvent(eventId)
                .map(eventMapper::toGetPublishedEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
