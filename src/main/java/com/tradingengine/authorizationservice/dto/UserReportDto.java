package com.tradingengine.authorizationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReportDto {
    private UUID userId;
    private String username;
    private String email;
    private String role;
    private String command;
    private String timestamp;
}