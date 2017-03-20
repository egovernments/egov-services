package org.egov.demand.persistence.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.egov.demand.persistence.entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InstallmentRepository
		extends JpaRepository<Installment, java.lang.Long>, JpaSpecificationExecutor<Installment> {

	Installment findByDescriptionAndModule(String description, Long module);

	List<Installment> findByFromDateAndToDateAndModule(Date fromDate, Date toDate, Long module);
	
	List<Installment> findByFromDateAndToDateAndInstallmentTypeAndModule(Date fromDate, Date toDate, String installmentType, Long module);

	@Transactional
	Installment save(Installment installment);

}
