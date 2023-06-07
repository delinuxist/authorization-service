package com.tradingengine.authorizationservice.controller;

import com.tradingengine.authorizationservice.dto.LoginRequestDto;
import com.tradingengine.authorizationservice.dto.LoginResponseDto;
import com.tradingengine.authorizationservice.dto.RegisterRequestDto;
import com.tradingengine.authorizationservice.dto.RegisterResponseDto;
import com.tradingengine.authorizationservice.entity.Role;
import com.tradingengine.authorizationservice.exception.UserNotFoundException;
import com.tradingengine.authorizationservice.repository.RoleRepository;
import com.tradingengine.authorizationservice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RoleRepository roleRepository;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Validated @RequestBody  RegisterRequestDto registerRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(registerRequestDto));
    }

    @PostMapping("/login")
    public  ResponseEntity<LoginResponseDto> login(@Validated @RequestBody  LoginRequestDto loginRequestDto) throws UserNotFoundException {
        return ResponseEntity.ok(authService.loginUser(loginRequestDto));
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) throws UserNotFoundException {
        String authHeader = request.getHeader("Authorization");
        authService.logout(authHeader);
    }

    @GetMapping("/role")
    public void create() {
        Role role = Role.builder().authority("USER").build();
        roleRepository.save(role);
    }
}
