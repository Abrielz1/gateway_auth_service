package com.nemo.gateway_auth_service.app.repository;

import com.nemo.gateway_auth_service.app.domain.entity.child.Client;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    @EntityGraph(value = "User.withAllDetails", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
          SELECT c
          FROM Client c
          JOIN c.userLogins ld
          WHERE LOWER(ld.login) = LOWER(:login)
          """)
    Optional<Client> findByLogin(String login);

    @EntityGraph(value = "User.withAllDetails", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
          SELECT c
          FROM Client c
          JOIN c.userPhones ph
          WHERE LOWER(ph.phone) = LOWER(:phone) 
          """)
    Optional<Client> findByPhone(String phone);

    @EntityGraph(value = "User.withAllDetails", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
          SELECT c
          FROM Client c
          JOIN c.userEmails e
          WHERE LOWER(e.email) = LOWER(:email)
          """)
    Optional<Client> findByEmail(String email);
}
