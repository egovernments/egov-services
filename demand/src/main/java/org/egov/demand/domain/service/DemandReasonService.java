package org.egov.demand.domain.service;

import org.egov.demand.persistence.entity.EgDemandReason;
import org.egov.demand.persistence.repository.DemandReasonRepository;
import org.egov.demand.web.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemandReasonService {
	@Autowired
	private DemandReasonRepository demandReasonRepository;
	@Autowired
	private ModuleRepository moduleRepository;

	public EgDemandReason findByCodeInstModule(String demandReasonCode, String instDescription, String moduleName) {
		Long moduleId = moduleRepository.fetchModuleByName(moduleName).getId();
		return demandReasonRepository.findByCodeInstModule(demandReasonCode, instDescription, moduleId);
	}
}
