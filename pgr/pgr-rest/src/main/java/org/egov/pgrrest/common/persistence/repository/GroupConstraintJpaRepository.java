package org.egov.pgrrest.common.persistence.repository;

import org.egov.pgrrest.common.persistence.entity.GroupConstraint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupConstraintJpaRepository extends JpaRepository<GroupConstraint, Long> {

    @Query("select g from GroupConstraint g where g.serviceCode = :serviceCode and g.tenantId = :tenantId")
    List<GroupConstraint> find(@Param("serviceCode") String serviceCode,
                               @Param("tenantId") String tenantId);
}
