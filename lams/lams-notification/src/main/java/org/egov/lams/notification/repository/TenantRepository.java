package org.egov.lams.notification.repository;


import org.egov.lams.notification.config.PropertiesManager;
import org.egov.lams.notification.model.City;
import org.egov.lams.notification.web.contract.RequestInfo;
import org.egov.lams.notification.web.contract.Tenant;
import org.egov.lams.notification.web.contract.TenantResponse;
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
	
	public Tenant fetchTenantByCode(String tenant) {
        String url = propertiesManager.getTenantServiceHostName() + "tenant/v1/tenant/_search?code=" + tenant;

        TenantResponse tr = restTemplate.postForObject(url, new RequestInfo(), TenantResponse.class);
        if (!CollectionUtils.isEmpty(tr.getTenant()))
            return tr.getTenant().get(0);
        else
            return null;
    }
}
