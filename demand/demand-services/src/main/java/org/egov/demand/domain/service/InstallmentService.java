package org.egov.demand.domain.service;

import java.util.Date;
import java.util.List;

import org.egov.demand.persistence.entity.Installment;
import org.egov.demand.persistence.repository.InstallmentRepository;
import org.egov.demand.web.contract.InstallmentSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstallmentService {
	@Autowired
	private InstallmentRepository installmentRepository;

	public Installment findByDescriptionAndModuleAndTenantId(String instDescription, String moduleName,
			String tenantId) {
		return installmentRepository.findByDescriptionAndModuleAndTenantId(instDescription, moduleName, tenantId);
	}

	public List<Installment> findByFromDateAndToDateAndModuleAndTenantId(Date fromDate, Date toDate, String moduleName,
			String tenantId) {
		return installmentRepository.findByFromDateAndToDateAndModuleAndTenantId(fromDate, toDate, moduleName,
				tenantId);
	}

	public List<Installment> findByFromDateAndToDateAndInstallmentTypeAndModule(Date fromDate, Date toDate,
			String installmentType, String moduleName, String tenantId) {
		return installmentRepository.findByFromDateAndToDateAndInstallmentTypeAndModuleAndTenantId(fromDate, toDate,
				installmentType, moduleName, tenantId);
	}

	public List<Installment> search(InstallmentSearchCriteria installmentSearchCriteria) {
		return installmentRepository.findByFromDateAndToDateAndInstallmentTypeAndModuleAndTenantId(
				installmentSearchCriteria.getFromDate(), installmentSearchCriteria.getToDate(),
				installmentSearchCriteria.getInstallmentType(), installmentSearchCriteria.getModule(),
				installmentSearchCriteria.getTenantId());
	}
	
	public List<Installment> findCurrentInstallmentByCurrentDateAndInstallmentTypeAndModuleAndTenantId(InstallmentSearchCriteria installmentSearchCriteria) {
		return installmentRepository.findCurrentInstallmentByCurrentDateAndInstallmentTypeAndModuleAndTenantId(
				installmentSearchCriteria.getCurrentDate(),
				installmentSearchCriteria.getInstallmentType(), installmentSearchCriteria.getModule(),
				installmentSearchCriteria.getTenantId());
	}
}
