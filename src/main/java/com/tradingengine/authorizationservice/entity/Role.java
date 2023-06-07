package com.tradingengine.authorizationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role implements GrantedAuthority {
    @Id()
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;
    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
