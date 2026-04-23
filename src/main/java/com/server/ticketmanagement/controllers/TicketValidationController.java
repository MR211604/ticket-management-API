package com.server.ticketmanagement.controllers;


import com.server.ticketmanagement.domain.dtos.TicketValidationRequestDto;
import com.server.ticketmanagement.domain.dtos.TicketValidationResponseDto;
import com.server.ticketmanagement.domain.entities.TicketValidation;
import com.server.ticketmanagement.domain.entities.TicketValidationMethod;
import com.server.ticketmanagement.mappers.TicketValidationMapper;
import com.server.ticketmanagement.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.server.ticketmanagement.util.JwtUtil.parseUserId;

@RestController
@RequestMapping(path = "/api/v1/ticket-validations")
@RequiredArgsConstructor
public class TicketValidationController {
    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;

    @PostMapping
    public ResponseEntity<TicketValidationResponseDto> validateTicket(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody TicketValidationRequestDto ticketValidationRequestDto
            ) {
        UUID staffId = parseUserId(jwt);
        TicketValidationMethod method = ticketValidationRequestDto.getMethod();
        TicketValidation ticketValidation;
        if (TicketValidationMethod.MANUAL.equals(method)) {
            ticketValidation = ticketValidationService.validateTicketManually(
                    ticketValidationRequestDto.getId(),
                    staffId
            );
        } else {
            ticketValidation = ticketValidationService.validateTicketByQrCode(
                    ticketValidationRequestDto.getId(),
                    staffId
            );
        }
        return ResponseEntity.ok(
                ticketValidationMapper.toTicketValidationResponseDto(ticketValidation)
        );
    }

}
