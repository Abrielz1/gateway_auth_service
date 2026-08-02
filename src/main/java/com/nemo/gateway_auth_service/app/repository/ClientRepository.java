package com.nemo.gateway_auth_service.app.repository;

import com.nemo.gateway_auth_service.app.domain.entity.child.Admin;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Admin, UUID> {

    @EntityGraph(value = "User.withAllDetails", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
          SELECT c
          FROM Admin c
          JOIN c.userLogins ld
          WHERE LOWER(ld.login) = LOWER(:login)
          """)
    Optional<Admin> findByLogin(String login);

    @EntityGraph(value = "User.withAllDetails", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
          SELECT c
          FROM Admin c
          JOIN c.userPhones ph
          WHERE LOWER(ph.phone) = LOWER(:phone) 
          """)
    Optional<Admin> findByPhone(String phone);

    @EntityGraph(value = "User.withAllDetails", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
          SELECT c
          FROM Admin c
          JOIN c.userEmails e
          WHERE LOWER(e.email) = LOWER(:email)
          """)
    Optional<Admin> findByEmail(String email);
}
