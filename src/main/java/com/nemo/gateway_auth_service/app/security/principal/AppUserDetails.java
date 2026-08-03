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

    private final Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    private final String password;

    @JsonIgnore
    private final Instant expiration;

    @JsonIgnore
    private final boolean enabled;

    @JsonIgnore
    private final boolean accountNonLocked;

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

        return this.accountNonLocked;
    }

    @Override
    public boolean isAccountNonLocked() {

        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return this.expiration == null || this.expiration.isAfter(Instant.now());
    }

    @Override
    public boolean isEnabled() {

        return this.enabled;
    }
}
