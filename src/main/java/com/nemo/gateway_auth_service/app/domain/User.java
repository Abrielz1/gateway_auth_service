package com.nemo.gateway_auth_service.app.domain;

import com.nemo.gateway_auth_service.app.domain.entity.enums.RoleType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.validator.constraints.UUID;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraph(
        name = "User.withRoles",
        attributeNodes = @NamedAttributeNode("roles")
)

@NamedEntityGraph(
        name = "User.withAllDetails",
        attributeNodes = {
                @NamedAttributeNode("roles"),
                @NamedAttributeNode("userPasswords"),
                @NamedAttributeNode("userEmails"),
                @NamedAttributeNode("userPhones"),
                @NamedAttributeNode("userLogins")
        }
)

@Entity
@Table(name = "users", schema = "security")
@Getter
@Setter
@Builder
@ToString()
@SQLRestriction("is_deleted = false")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UUID
    @Column(name = "user_uuid", unique = true, nullable = false, updatable = false)
    private java.util.UUID userUUID;

    @Column(name = "last_login_timestamp")
    private ZonedDateTime  lastLogin;

    @Column(name = "is_enabled")
    private Boolean enabled = false;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;

    @Version
    private Long version = 0L;

    @Column(name = "registration_timestamp")
    @ToString.Include
    private ZonedDateTime registrationDateTime;

    @ElementCollection(targetClass = RoleType.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "user_roles", schema = "security", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @ToString.Exclude
    private Set<RoleType> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<EmailData> userEmails = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<PasswordData> userPasswords = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<PhoneData> userPhones = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<LoginData> userLogins = new HashSet<>();

    public void addEmailData(EmailData emailData) {
        this.userEmails.add(emailData);
        emailData.setUser(this);
    }

    public void addPasswordData(PasswordData passwordData) {
        this.userPasswords.add(passwordData);
        passwordData.setUser(this);
    }

    public void addPhoneData(PhoneData phoneData) {
        this.userPhones.add(phoneData);
        phoneData.setUser(this);
    }

    public void addLoginData(LoginData loginData) {
        this.userLogins.add(loginData);
        loginData.setUser(this);
    }
}
