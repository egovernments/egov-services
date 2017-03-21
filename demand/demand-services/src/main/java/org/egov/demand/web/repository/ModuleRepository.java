package org.egov.demand.web.repository;

import java.util.Date;

import org.egov.demand.web.contract.Module;
import org.egov.demand.web.contract.ModuleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class ModuleRepository {
	private final String commonServicesHost;
	private final String searchPath;

	@Autowired
	private RestTemplate restTemplate;

	public ModuleRepository(RestTemplate restTemplate,
			@Value("${egov.services.common-masters.hostname}") String commonServicesHost,
			@Value("${egov.services.common-masters.searchpath}") final String searchPath) {
		this.restTemplate = restTemplate;
		this.commonServicesHost = commonServicesHost;
		this.searchPath = searchPath;
	}

	public Module fetchModuleByName(String name) {
		String url = this.commonServicesHost + searchPath+"?tenantId=ap.public&name="+name;
		org.egov.demand.web.contract.RequestInfo requestInfo = new org.egov.demand.web.contract.RequestInfo();
		requestInfo.setApiId("org.egov.commons.module.search");
		requestInfo.setVer("1.0");
		requestInfo.setTs(new Date().toString());
		ModuleResponse moduleResponse = restTemplate.postForObject(url, requestInfo, ModuleResponse.class);
		return moduleResponse.getModules().get(0);
	}
}
