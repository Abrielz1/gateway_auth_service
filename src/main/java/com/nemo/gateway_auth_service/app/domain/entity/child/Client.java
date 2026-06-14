package com.nemo.gateway_auth_service.app.domain.entity.child;

import com.nemo.gateway_auth_service.app.domain.entity.parent.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "customers", schema = "security")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User {

    @Column(name = "registration_source")
    @ToString.Include
    private String registrationSource;

    @Column(name = "is_banned", nullable = false)
    @Builder.Default
    @ToString.Include
    private Boolean isBanned = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by_id")
    @ToString.Exclude
    private User invitedBy;
}