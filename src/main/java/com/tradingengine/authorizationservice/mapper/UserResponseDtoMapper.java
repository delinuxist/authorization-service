package com.tradingengine.authorizationservice.mapper;

import com.tradingengine.authorizationservice.dto.RegisterResponseDto;
import com.tradingengine.authorizationservice.entity.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserResponseDtoMapper implements Function<User, RegisterResponseDto> {
    @Override
    public RegisterResponseDto apply(User user) {
        return RegisterResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(user.getAuthorities())
                .build();
    }
}
