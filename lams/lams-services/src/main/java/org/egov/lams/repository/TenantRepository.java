package org.egov.lams.repository;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.City;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.Tenant;
import org.egov.lams.web.contract.TenantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

@Repository
public class TenantRepository {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	public City fetchTenantByCode(String tenant) {
        String url = propertiesManager.getTenantServiceHostName() + "tenant/v1/tenant/_search?code=" + tenant;

        /*final RequestInfoBody requestInfoBody = new RequestInfoBody(RequestInfo.builder().build());

        final HttpEntity<RequestInfoBody> request = new HttpEntity<>(requestInfoBody);*/

        TenantResponse tr = restTemplate.postForObject(url, new RequestInfo(), TenantResponse.class);
        if (!CollectionUtils.isEmpty(tr.getTenant()))
            return tr.getTenant().get(0).getCity();
        else
            return null;
    }
}
