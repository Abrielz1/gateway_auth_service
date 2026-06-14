package com.nemo.gateway_auth_service.app.security.principal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class AppUserDetails implements UserDetails {

    private final UUID id;

    private final String email;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    private final Instant expiration;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {

        return email;
    }

    @Override
    public boolean isAccountNonExpired() {

        // todo докинуть проверку флага
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        // todo докинуть проверку флага
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        // todo докинуть проверку флага
        return true;
    }

    @Override
    public boolean isEnabled() {

        // todo докинуть проверку флага
        return true;
    }
}
