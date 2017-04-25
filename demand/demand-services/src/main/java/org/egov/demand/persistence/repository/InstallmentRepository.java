package org.egov.demand.persistence.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.egov.demand.persistence.entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InstallmentRepository
		extends JpaRepository<Installment, java.lang.Long>, JpaSpecificationExecutor<Installment> {

	Installment findByDescriptionAndModuleAndTenantId(String description, String moduleName, String tenantId);

	List<Installment> findByFromDateAndToDateAndModule(Date fromDate, Date toDate, String moduleName);
	
	List<Installment> findByFromDateAndToDateAndInstallmentTypeAndModule(Date fromDate, Date toDate, String installmentType, String moduleName);

	@Transactional
	Installment save(Installment installment);

}
