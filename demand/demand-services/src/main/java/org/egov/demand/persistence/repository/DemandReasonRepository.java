package org.egov.demand.persistence.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.egov.demand.persistence.entity.EgDemandReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DemandReasonRepository
		extends JpaRepository<EgDemandReason, java.lang.Long>, JpaSpecificationExecutor<EgDemandReason> {
	@Query(" from EgDemandReason dr where dr.egDemandReasonMaster.code=:demandReasonCode and dr.egInstallmentMaster.description=:instDescription and dr.egInstallmentMaster.module=:moduleId")
	EgDemandReason findByCodeInstModule(@Param("demandReasonCode") String demandReasonCode,
			@Param("instDescription") String instDescription, @Param("moduleId") Long moduleId);

	@Query(" from EgDemandReason dr where dr.egInstallmentMaster.module=:moduleId")
	List<EgDemandReason> search(@Param("moduleId") Long moduleId);

	@Transactional
	EgDemandReason save(EgDemandReason demandReason);
}
