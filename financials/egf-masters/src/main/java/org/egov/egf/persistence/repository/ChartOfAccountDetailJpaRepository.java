package org.egov.egf.persistence.repository;

import org.egov.egf.persistence.entity.ChartOfAccountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChartOfAccountDetailJpaRepository
		extends JpaRepository<ChartOfAccountDetail, java.lang.Long>, JpaSpecificationExecutor<ChartOfAccountDetail> {

}