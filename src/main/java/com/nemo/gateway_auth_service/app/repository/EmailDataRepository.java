package com.nemo.gateway_auth_service.app.repository;

import com.nemo.gateway_auth_service.app.domain.entity.parent.EmailData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
}
