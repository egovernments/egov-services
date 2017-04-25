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
	@Query(" from EgDemandReason dr where dr.tenantId=:tenantId and dr.egDemandReasonMaster.code=:demandReasonCode and dr.egInstallmentMaster.description=:instDescription and dr.egInstallmentMaster.module=:moduleName")
	EgDemandReason findByCodeInstModule(@Param("demandReasonCode") String demandReasonCode,
			@Param("instDescription") String instDescription, @Param("moduleName") String moduleName, @Param("tenantId") String tenantId);

	@Query(" from EgDemandReason dr where dr.egInstallmentMaster.module=:moduleName and tenantId=:tenantId")
	List<EgDemandReason> search(@Param("moduleName") String moduleName, @Param("tenantId") String tenantId);

	@Transactional
	EgDemandReason save(EgDemandReason demandReason);
}
