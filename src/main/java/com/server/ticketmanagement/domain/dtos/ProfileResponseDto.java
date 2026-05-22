package com.server.ticketmanagement.domain.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponseDto {
   private UUID userId;
   private String name;
   private String email;
   private Boolean isAccountVerified;
}
