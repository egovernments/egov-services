package org.egov.workflow.persistence.repository;

import org.egov.workflow.persistence.entity.KeywordStatusMapping;
import org.egov.workflow.persistence.entity.KeywordStatusMappingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordStatusMappingJpaRepository extends JpaRepository<KeywordStatusMapping,KeywordStatusMappingKey>
     {

    @Query("select status from KeywordStatusMapping status where status.id.keyword = :keyword and status.id.tenantId = :tenantId")
    List<KeywordStatusMapping> findByKeywordAndTenantId(
        @Param("keyword") String keyword, @Param("tenantId") String tenantId);
}
