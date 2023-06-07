package com.tradingengine.authorizationservice.service.impl;

import com.tradingengine.authorizationservice.dto.ClientAndAdminDto;
import com.tradingengine.authorizationservice.dto.RegisterRequestDto;
import com.tradingengine.authorizationservice.dto.RegisterResponseDto;
import com.tradingengine.authorizationservice.dto.UserReportDto;
import com.tradingengine.authorizationservice.entity.Role;
import com.tradingengine.authorizationservice.entity.User;
import com.tradingengine.authorizationservice.exception.UserNotFoundException;
import com.tradingengine.authorizationservice.mapper.ClientAndAdminDtoMapper;
import com.tradingengine.authorizationservice.mapper.UserResponseDtoMapper;
import com.tradingengine.authorizationservice.producer.RabbitMqProducer;
import com.tradingengine.authorizationservice.repository.RoleRepository;
import com.tradingengine.authorizationservice.repository.UserRepository;
import com.tradingengine.authorizationservice.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserResponseDtoMapper userResponseDtoMapper;

    private final ClientAndAdminDtoMapper clientAndAdminDtoMapper;

    private final RabbitMqProducer rabbitMqProducer;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException("Invalid Credentials")
        );
    }

    @Override
    public RegisterResponseDto createUser(RegisterRequestDto registerRequestDto) {

        Role userRole = roleRepository.findByAuthority("USER").orElse(Role.builder()
                        .authority("USER")
                        .build());
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

       User user = userRepository.save(
                        User.builder()
                                .username(registerRequestDto.username())
                                .createdAt(LocalDateTime.now())
                                .email(registerRequestDto.email())
                                .password(passwordEncoder.encode(registerRequestDto.password()))
                                .authorities(authorities)
                                .build()
        );

        UserReportDto userReportDto = UserReportDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .userId(user.getUserId())
                .timestamp(LocalDateTime.now().toString())
                .command("Registered")
                .role(user.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).get())
                .build();

        rabbitMqProducer.sendMessage(userReportDto);
        return  userResponseDtoMapper.apply(user);
    }

    @Override
    public RegisterResponseDto getUserByUsername(String username) throws UserNotFoundException {
        return userResponseDtoMapper.apply(
                userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new)
        );
    }

    @Override
    public User getUser(UUID userId) throws UserNotFoundException {
       return userRepository.findById(userId).orElseThrow(
               UserNotFoundException::new
       );
    }

    @Override
    public List<ClientAndAdminDto> getAllClients() {
        List<ClientAndAdminDto> allUsers = userRepository.findAll().stream().map(clientAndAdminDtoMapper).toList();
        return allUsers.stream().filter(r -> r.role().equals("USER")).toList();
    }

    @Override
    public List<ClientAndAdminDto> getAllAdmins() {
        List<ClientAndAdminDto> allUsers = userRepository.findAll().stream().map(clientAndAdminDtoMapper).toList();
        return allUsers.stream().filter(r -> r.role().equals("ADMIN")).toList();
    }
}
