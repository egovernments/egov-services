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

	public Installment findByDescriptionAndModule(String instDescription, String moduleName) {
		return installmentRepository.findByDescriptionAndModule(instDescription, moduleName);
	}

	public List<Installment> findByFromDateAndToDateAndModule(Date fromDate, Date toDate, String moduleName) {
		return installmentRepository.findByFromDateAndToDateAndModule(fromDate, toDate, moduleName);
	}

	public List<Installment> findByFromDateAndToDateAndInstallmentTypeAndModule(Date fromDate, Date toDate,
			String installmentType, String moduleName) {
		return installmentRepository.findByFromDateAndToDateAndInstallmentTypeAndModule(fromDate, toDate,
				installmentType, moduleName);
	}

	public List<Installment> search(InstallmentSearchCriteria installmentSearchCriteria) {
		return installmentRepository.findByFromDateAndToDateAndInstallmentTypeAndModule(
				installmentSearchCriteria.getFromDate(), installmentSearchCriteria.getToDate(),
				installmentSearchCriteria.getInstallmentType(), installmentSearchCriteria.getModule());
	}
}
