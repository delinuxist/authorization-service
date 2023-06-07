package com.tradingengine.authorizationservice.service;

import com.tradingengine.authorizationservice.dto.ClientAndAdminDto;
import com.tradingengine.authorizationservice.dto.RegisterRequestDto;
import com.tradingengine.authorizationservice.dto.RegisterResponseDto;
import com.tradingengine.authorizationservice.entity.User;
import com.tradingengine.authorizationservice.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public interface UserService {

    RegisterResponseDto createUser(RegisterRequestDto registerRequestDto);

    RegisterResponseDto getUserByUsername(String username) throws UserNotFoundException;

    User getUser(UUID userId) throws UserNotFoundException;

    List<ClientAndAdminDto> getAllClients();

    List<ClientAndAdminDto> getAllAdmins();
}
