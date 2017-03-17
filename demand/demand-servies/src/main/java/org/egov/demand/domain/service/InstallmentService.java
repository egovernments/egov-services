package org.egov.demand.domain.service;

import org.egov.demand.persistence.entity.Installment;
import org.egov.demand.persistence.repository.InstallmentRepository;
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
}
