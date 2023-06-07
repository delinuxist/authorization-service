package com.tradingengine.authorizationservice.service.impl;

import com.tradingengine.authorizationservice.dto.*;
import com.tradingengine.authorizationservice.entity.User;
import com.tradingengine.authorizationservice.exception.UserNotFoundException;
import com.tradingengine.authorizationservice.producer.RabbitMqProducer;
import com.tradingengine.authorizationservice.repository.UserRepository;
import com.tradingengine.authorizationservice.service.AuthService;
import com.tradingengine.authorizationservice.service.UserService;
import com.tradingengine.authorizationservice.utils.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final RabbitMqProducer rabbitMqProducer;

    @Override
    public RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto) {
        return userService.createUser(registerRequestDto);
    }

    @Override
    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) throws UserNotFoundException {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password())
        );

        if (!auth.isAuthenticated()) {
            throw new UserNotFoundException();
        }
        RegisterResponseDto registeredUser = userService.getUserByUsername(loginRequestDto.username());

        User user = userService.getUser(registeredUser.userId());

        String token = jwtService.generateToken(user, user.getUserId());
        UserReportDto userReport = UserReportDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .role(user.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).get())
                .email(user.getEmail())
                .command("Logged In")
                .timestamp(LocalDateTime.now().toString())
                .build();
        rabbitMqProducer.sendMessage(userReport);
        return LoginResponseDto.builder()
                .user(registeredUser)
                .token(token)
                .build();
    }

    @Override
    public void logout(String authHeader) throws UserNotFoundException {
        String token = authHeader.split(" ")[1];
        var claims = jwtService.extractAllClaims(token);
        User user = userService.getUser(UUID.fromString(claims.getId()));

        UserReportDto userReport = UserReportDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).get())
                .command("Logged Out")
                .timestamp(LocalDateTime.now().toString())
                .build();
        rabbitMqProducer.sendMessage(userReport);

    }
}
