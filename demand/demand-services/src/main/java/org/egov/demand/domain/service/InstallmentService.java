package org.egov.demand.domain.service;

import java.util.Date;
import java.util.List;

import org.egov.demand.persistence.entity.Installment;
import org.egov.demand.persistence.repository.InstallmentRepository;
import org.egov.demand.web.contract.InstallmentSearchCriteria;
import org.egov.demand.web.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstallmentService {
	@Autowired
	private InstallmentRepository installmentRepository;
	@Autowired
	private ModuleRepository moduleRepository;

	public Installment findByDescriptionAndModule(String instDescription, String moduleName) {
		Long moduleId = moduleRepository.fetchModuleByName(moduleName).getId();
		return installmentRepository.findByDescriptionAndModule(instDescription, moduleId);
	}

	public List<Installment> findByFromDateAndToDateAndModule(Date fromDate, Date toDate, String moduleName) {
		Long moduleId = moduleRepository.fetchModuleByName(moduleName).getId();
		return installmentRepository.findByFromDateAndToDateAndModule(fromDate, toDate, moduleId);
	}

	public List<Installment> findByFromDateAndToDateAndInstallmentTypeAndModule(Date fromDate, Date toDate,
			String installmentType, String moduleName) {
		Long moduleId = moduleRepository.fetchModuleByName(moduleName).getId();
		return installmentRepository.findByFromDateAndToDateAndInstallmentTypeAndModule(fromDate, toDate,
				installmentType, moduleId);
	}

	public List<Installment> search(InstallmentSearchCriteria installmentSearchCriteria) {
		Long moduleId = moduleRepository.fetchModuleByName(installmentSearchCriteria.getModule()).getId();
		return installmentRepository.findByFromDateAndToDateAndInstallmentTypeAndModule(
				installmentSearchCriteria.getFromDate(), installmentSearchCriteria.getToDate(),
				installmentSearchCriteria.getInstallmentType(), moduleId);
	}
}
