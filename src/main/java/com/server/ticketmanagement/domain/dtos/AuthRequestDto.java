package com.server.ticketmanagement.domain.dtos;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Nonnull
@Builder
public class AuthRequestDto {
    private String email;
    private String password;
}
