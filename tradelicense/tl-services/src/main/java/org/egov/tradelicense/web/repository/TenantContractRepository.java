package org.egov.tradelicense.web.repository;

import org.egov.models.SearchTenantResponse;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tradelicense.common.config.PropertiesManager;
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

	public org.egov.models.City fetchTenantByCode(String tenantId, RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManger.getTenantServiceHostName() + propertiesManger.getTenantServiceBasePath();
		String searchUrl = propertiesManger.getTenantServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();

		if (tenantId != null && !tenantId.isEmpty()) {
			content.append("?code=" + tenantId);
		}

		url = url + content.toString();
		SearchTenantResponse tenantResponse = null;
		try {
			
			tenantResponse = restTemplate.postForObject(url, requestInfoWrapper.getRequestInfo(), SearchTenantResponse.class);

		} catch (Exception e) {
//			log.debug("Error connecting to Tenant service end point " + url);
		}

		if (tenantResponse != null && tenantResponse.getTenant() != null && tenantResponse.getTenant().size() > 0) {

			return tenantResponse.getTenant().get(0).getCity();

		} else {

			return null;
		}
	}
}