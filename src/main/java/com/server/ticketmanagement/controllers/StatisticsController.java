package com.server.ticketmanagement.controllers;

import com.server.ticketmanagement.domain.dtos.OrganizerStatisticsResponseDto;
import com.server.ticketmanagement.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.server.ticketmanagement.util.JwtUtil.parseUserId;

@RestController
@RequestMapping(path = "/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/organizer")
    public ResponseEntity<OrganizerStatisticsResponseDto> getOrganizerStatistics(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        UUID organizerId = parseUserId(jwt);
        OrganizerStatisticsResponseDto stats = statisticsService.getOrganizerStatistics(organizerId, startDate, endDate);
        return ResponseEntity.ok(stats);
    }
}
