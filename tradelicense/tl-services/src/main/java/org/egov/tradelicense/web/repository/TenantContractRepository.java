package org.egov.tradelicense.web.repository;

import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.web.contract.City;
import org.egov.tradelicense.web.contract.TenantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TenantContractRepository {

	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManger;

	public TenantContractRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public City fetchTenantByCode(String tenantId, RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManger.getTenantServiceHostName() + propertiesManger.getTenantServiceBasePath();
		String searchUrl = propertiesManger.getTenantServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();

		if (tenantId != null) {

			content.append("code=" + tenantId);
		}

		url = url + content.toString();

		TenantResponse tenantResponse = restTemplate.postForObject(url, requestInfoWrapper, TenantResponse.class);

		if (tenantResponse != null && tenantResponse.getTenant() != null) {

			return tenantResponse.getTenant().get(0).getCity();

		} else {

			return null;
		}
	}
}