package com.server.ticketmanagement.services;

import com.server.ticketmanagement.domain.dtos.OrganizerStatisticsResponseDto;
import java.time.LocalDateTime;
import java.util.UUID;

public interface StatisticsService {
    OrganizerStatisticsResponseDto getOrganizerStatistics(UUID organizerId, LocalDateTime startDate, LocalDateTime endDate);
}

