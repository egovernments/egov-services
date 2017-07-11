package org.egov.pgrrest.read.persistence.repository;

import org.egov.pgrrest.read.persistence.entity.Draft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DraftJpaRepository extends JpaRepository<Draft, Long> {
    @Query("select d from Draft d where d.userId= :userId and d.serviceCode= :serviceCode and d.tenantId = :tenantId")
    List<Draft> findByUserIdAndServiceCodeAndTenantId(@Param("userId") Long userId, @Param("serviceCode") String serviceCode, @Param("tenantId") String tenantId);

    @Query("select d from Draft d where d.userId= :userId and d.tenantId = :tenantId")
    List<Draft> findByUserIdAndTenantId(@Param("userId") Long userId, @Param("tenantId") String tenantId);

    @Modifying
    @Transactional
    @Query("delete from Draft d where d.id in :ids")
    void deleteByIdList(@Param("ids") List<Long> draftIdList);

}
