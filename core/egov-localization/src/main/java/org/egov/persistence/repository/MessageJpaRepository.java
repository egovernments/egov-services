package org.egov.persistence.repository;

import org.egov.persistence.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageJpaRepository extends JpaRepository<Message, Long> {
    List<Message> findByTenantIdAndLocale(String tenantId, String locale);

}
