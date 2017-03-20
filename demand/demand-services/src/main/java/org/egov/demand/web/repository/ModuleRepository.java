package org.egov.demand.web.repository;

import org.egov.demand.web.contract.Module;
import org.egov.demand.web.contract.ModuleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class ModuleRepository {
	private final String commonServicesHost;
	private final String moduleByNameUrl;

	@Autowired
	private RestTemplate restTemplate;

	public ModuleRepository(RestTemplate restTemplate,
			@Value("${egov.services.common-masters.host}") String commonServicesHost,
			@Value("${egov.services.common-masters.module_by_name}") final String moduleByNameUrl) {
		this.restTemplate = restTemplate;
		this.commonServicesHost = commonServicesHost;
		this.moduleByNameUrl = moduleByNameUrl;
	}

	public Module fetchModuleByName(String name) {
		String url = this.commonServicesHost + moduleByNameUrl;
		return getModuleServiceResponse(url, name).getModules().get(0);
	}

	private ModuleResponse getModuleServiceResponse(final String url, String name) {
		return restTemplate.getForObject(url, ModuleResponse.class, name);
	}
}
