package com.tradingengine.authorizationservice.controller;

import com.tradingengine.authorizationservice.dto.ClientAndAdminDto;
import com.tradingengine.authorizationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/allClients")
    public ResponseEntity<List<ClientAndAdminDto>> getAllClients() {
        return ResponseEntity.ok(userService.getAllClients());
    }

    @GetMapping("/allAdmins")
    public ResponseEntity<List<ClientAndAdminDto>> getAllAdmins() {
        return ResponseEntity.ok(userService.getAllAdmins());
    }
}
