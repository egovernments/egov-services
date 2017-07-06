package org.egov.demand.persistence.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.egov.demand.persistence.entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InstallmentRepository
		extends JpaRepository<Installment, java.lang.Long>, JpaSpecificationExecutor<Installment> {

	Installment findByDescriptionAndModuleAndTenantId(String description, String moduleName, String tenantId);

	List<Installment> findByFromDateAndToDateAndModuleAndTenantId(Date fromDate, Date toDate, String moduleName, String tenantId);
	
	List<Installment> findByFromDateAndToDateAndInstallmentTypeAndModuleAndTenantId(Date fromDate, Date toDate, String installmentType, String moduleName, String tenantId);

	@Query("SELECT inst FROM Installment inst where inst.fromDate<= :currentDate and inst.toDate >= :currentDate and installmentType = :installmentType and module = :module and tenantId = :tenantId")
	List<Installment> findCurrentInstallmentByCurrentDateAndInstallmentTypeAndModuleAndTenantId(
			@Param("currentDate") Date currentDate, @Param("installmentType") String installmentType,
			@Param("module") String module, @Param("tenantId") String tenantId);
	
	@Transactional
	Installment save(Installment installment);

}
