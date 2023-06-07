package com.tradingengine.authorizationservice.repository;

import com.tradingengine.authorizationservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsernameAndEmail(String username,String email);
    Optional<User> findByUsername(String username);

}
