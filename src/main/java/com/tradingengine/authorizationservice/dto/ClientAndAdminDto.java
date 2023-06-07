package com.tradingengine.authorizationservice.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ClientAndAdminDto(
        UUID userId,
       String username,
     String email,
    String role,
    LocalDateTime createdAt
) {
}
