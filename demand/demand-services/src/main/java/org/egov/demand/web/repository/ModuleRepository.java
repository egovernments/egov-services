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
	
	@Autowired
	private RestTemplate restTemplate;

	public ModuleRepository(RestTemplate restTemplate,
			@Value("${egov.services.common-masters.host}") String commonServicesHost) {
		this.restTemplate = restTemplate;
		this.commonServicesHost = commonServicesHost;
	}

	public Module fetchModuleByName(String name) {
		String url = this.commonServicesHost + "egov-common-masters/modules/_search?module.name={name}";
		return getModuleServiceResponse(url, name).getModules().get(0);
	}

	private ModuleResponse getModuleServiceResponse(final String url, String name) {
		return restTemplate.getForObject(url, ModuleResponse.class, name);
	}
}
