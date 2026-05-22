package com.server.ticketmanagement.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String email;
    private String token;
    private java.util.List<String> roles;
}
