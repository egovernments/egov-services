package org.egov.demand.persistence.repository;

import javax.transaction.Transactional;

import org.egov.demand.persistence.entity.EgDemand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandRepository
		extends JpaRepository<EgDemand, java.lang.Long>, JpaSpecificationExecutor<EgDemand> {
	@Transactional
	EgDemand save(EgDemand demand);
}
