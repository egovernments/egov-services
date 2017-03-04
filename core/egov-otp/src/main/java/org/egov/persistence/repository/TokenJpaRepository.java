package org.egov.persistence.repository;

import org.egov.persistence.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenJpaRepository extends JpaRepository<Token, Long> {
    Token findByNumberAndIdentity(String number, String identity);
}

