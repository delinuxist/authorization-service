package com.tradingengine.authorizationservice.dto;

import lombok.Builder;

@Builder
public record LoginResponseDto(
        RegisterResponseDto user,
        String token
) {
}
