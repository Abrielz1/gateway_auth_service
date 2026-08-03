package com.nemo.gateway_auth_service.app.repository;

import com.nemo.gateway_auth_service.app.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID
        > {

    @EntityGraph(value = "User.withAllDetails", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
          SELECT c
          FROM User c
          JOIN c.userLogins ld
          WHERE LOWER(ld.login) = LOWER(:login)
          """)
    Optional<User> findByLogin(String login);

    @EntityGraph(value = "User.withAllDetails", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
          SELECT c
          FROM User c
          JOIN c.userPhones ph
          WHERE LOWER(ph.phone) = LOWER(:phone) 
          """)
    Optional<User> findByPhone(String phone);

    @EntityGraph(value = "User.withAllDetails", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
          SELECT c
          FROM User c
          JOIN c.userEmails e
          WHERE LOWER(e.email) = LOWER(:email)
          """)
    Optional<User> findByEmail(String email);
}
