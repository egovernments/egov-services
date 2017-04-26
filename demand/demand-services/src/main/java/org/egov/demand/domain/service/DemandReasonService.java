package org.egov.demand.domain.service;

import java.util.List;

import org.egov.demand.persistence.entity.EgDemandReason;
import org.egov.demand.persistence.repository.DemandReasonRepository;
import org.egov.demand.web.contract.DemandReasonCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemandReasonService {
	@Autowired
	private DemandReasonRepository demandReasonRepository;

	public EgDemandReason findByCodeInstModule(String demandReasonCode, String instDescription, String moduleName, String tenantId) {
		return demandReasonRepository.findByCodeInstModule(demandReasonCode, instDescription, moduleName, tenantId);
	}

	public List<EgDemandReason> search(DemandReasonCriteria demandReasonCriteria) {
		return demandReasonRepository.search(demandReasonCriteria.getModuleName(), demandReasonCriteria.getTenantId());
	}
}
