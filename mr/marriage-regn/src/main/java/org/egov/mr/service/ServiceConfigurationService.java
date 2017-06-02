package org.egov.mr.service;

import java.util.List;
import java.util.Map;

import org.egov.mr.repository.ServiceConfigurationRepository;
import org.egov.mr.web.contract.ServiceConfigurationSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceConfigurationService {

	@Autowired
	private ServiceConfigurationRepository serviceConfigurationRepository;

	public Map<String, List<String>> search(ServiceConfigurationSearchCriteria serviceConfigurationSearchCriteria) {
		return serviceConfigurationRepository.findForCriteria(serviceConfigurationSearchCriteria);
	}

}
