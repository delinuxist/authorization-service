package com.tradingengine.authorizationservice.mapper;

import com.tradingengine.authorizationservice.dto.ClientAndAdminDto;
import com.tradingengine.authorizationservice.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ClientAndAdminDtoMapper implements Function<User,ClientAndAdminDto> {

    @Override
    public ClientAndAdminDto apply(User user) {
        return ClientAndAdminDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .userId(user.getUserId())
                .createdAt(user.getCreatedAt())
                .role(user.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).get())
                .build();
    }
}
