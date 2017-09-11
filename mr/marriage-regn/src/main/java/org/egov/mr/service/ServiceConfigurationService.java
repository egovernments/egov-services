package org.egov.mr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.mr.model.enums.ServiceConfigurationKeys;
import org.egov.mr.repository.ServiceConfigurationRepository;
import org.egov.mr.web.contract.ServiceConfigurationSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceConfigurationService {

	@Autowired
	private ServiceConfigurationRepository serviceConfigurationRepository;

	public Map<String, List<String>> search(ServiceConfigurationSearchCriteria serviceConfigurationSearchCriteria) {
		return serviceConfigurationRepository.findForCriteria(serviceConfigurationSearchCriteria);
	}

	public String getServiceConfigValueByKeyAndTenantId(final ServiceConfigurationKeys serviceConfigurationKey,
			final String tenantId) {
	
		final ServiceConfigurationSearchCriteria serviceConfigurationSearchCriteria = ServiceConfigurationSearchCriteria
				.builder().tenantId(tenantId).name(serviceConfigurationKey.toString()).build();

		final Map<String, List<String>> serviceConfiguration = serviceConfigurationRepository
				.findForCriteria(serviceConfigurationSearchCriteria);
		if (serviceConfiguration.isEmpty())
			throw new RuntimeException("no service configuration found for the key--"
					+ serviceConfigurationKey.toString() + " tenant value--" + tenantId);
		return  serviceConfiguration.get(serviceConfigurationKey.toString()).get(0);
	}

}
