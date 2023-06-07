package com.tradingengine.authorizationservice.dto;

import lombok.Builder;

import java.util.Collection;

import java.util.UUID;

@Builder
public record RegisterResponseDto(
        UUID userId,
        String username,
        String email,
        Collection<?> authorities
) {
}
