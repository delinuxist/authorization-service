package com.tradingengine.authorizationservice.service;

import com.tradingengine.authorizationservice.dto.LoginRequestDto;
import com.tradingengine.authorizationservice.dto.LoginResponseDto;
import com.tradingengine.authorizationservice.dto.RegisterRequestDto;
import com.tradingengine.authorizationservice.dto.RegisterResponseDto;
import com.tradingengine.authorizationservice.exception.UserNotFoundException;

import java.util.UUID;

public interface AuthService {

    RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto);

    LoginResponseDto loginUser(LoginRequestDto loginRequestDto) throws UserNotFoundException;

    void logout(String authHeader) throws UserNotFoundException;

}
