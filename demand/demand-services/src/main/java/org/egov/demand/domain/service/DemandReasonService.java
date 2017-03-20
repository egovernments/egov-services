package org.egov.demand.domain.service;

import java.util.List;

import org.egov.demand.persistence.entity.EgDemandReason;
import org.egov.demand.persistence.repository.DemandReasonRepository;
import org.egov.demand.web.contract.DemandReasonCriteria;
import org.egov.demand.web.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemandReasonService {
	@Autowired
	private DemandReasonRepository demandReasonRepository;
	@Autowired
	private ModuleRepository moduleRepository;

	public EgDemandReason findByCodeInstModule(String demandReasonCode, String instDescription, String moduleName,
			String tenantId) {
		Long moduleId = moduleRepository.fetchModuleByName(moduleName).getId();
		return demandReasonRepository.findByCodeInstModule(demandReasonCode, instDescription, moduleId);
	}

	public List<EgDemandReason> search(DemandReasonCriteria demandReasonCriteria) {
		Long moduleId = moduleRepository.fetchModuleByName(demandReasonCriteria.getModuleName()).getId();
		return demandReasonRepository.search(moduleId);
	}
}
