package org.egov.persistence.repository;

import org.egov.persistence.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TokenJpaRepository extends JpaRepository<Token, String> {
    List<Token> findByNumberAndIdentityAndTenantId(String number, String identity, String tenant);
    @Modifying
    @Query("update Token t set t.validated = 'Y' where t.id = :id")
    int markTokenAsValidated(@Param("id") String id);
}

