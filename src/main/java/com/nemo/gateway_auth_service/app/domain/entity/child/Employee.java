package com.nemo.gateway_auth_service.app.domain.entity.child;

import com.nemo.gateway_auth_service.app.domain.entity.parent.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Table(name = "employees", schema = "security")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString()
public class Employee extends User {

    @Column(name = "employee_internal_id", unique = true)
    private String employeeInternalId;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    @Column(name = "department")
    private String department;

    @Column(name = "position")
    private String position;

    @Column(name = "security_cleared", nullable = false)
    private boolean isSecurityCleared = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id")
    @ToString.Exclude
    private Employee mentor;

    @OneToMany(mappedBy = "mentor")
    @ToString.Exclude
    private Set<Employee> mentees = new HashSet<>();
}
