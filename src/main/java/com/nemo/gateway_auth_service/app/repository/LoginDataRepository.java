package com.nemo.gateway_auth_service.app.repository;

import com.nemo.gateway_auth_service.app.domain.entity.parent.LoginData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginDataRepository extends JpaRepository<LoginData, Long> {
}
