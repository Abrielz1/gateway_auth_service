package com.nemo.gateway_auth_service.app.repository;

import com.nemo.gateway_auth_service.app.domain.entity.parent.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneDataRepository extends JpaRepository<PhoneData,Long> {
}
