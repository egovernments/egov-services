package org.egov.commons.service;

import java.util.List;

import org.egov.commons.model.Module;
import org.egov.commons.repository.ModuleRepository;
import org.egov.commons.web.contract.ModuleGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ModuleService {
	@Autowired
	private ModuleRepository moduleRepository;

	public List<Module> getModules(ModuleGetRequest moduleGetRequest) {
		return moduleRepository.findForCriteria(moduleGetRequest);
	}
}
