package org.egov.wcms.workflow.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.workflow.config.ApplicationProperties;
import org.egov.wcms.workflow.tenant.contract.TenantRequest;
import org.egov.wcms.workflow.tenant.contract.TenantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TenantService {
    
    @Autowired
    private ApplicationProperties   applicationProperties;
    
    
    public String getLoggedInUserCityGrade(RequestInfo requestInfo)
    {
        String cityGrade="";
        StringBuilder url = new StringBuilder();
        url.append(applicationProperties.getTenantServiceBasePath());
        url.append(applicationProperties.getTenantServiceSearchPath());
        TenantResponse tenantResponse = new RestTemplate().postForObject(url.toString(), requestInfo, TenantResponse.class);
        if (tenantResponse != null && !tenantResponse.getTenant().isEmpty()) {
            cityGrade=tenantResponse.getTenant().get(0).getCity().getUlbGrade();
        }
        return cityGrade;
    }
 

}
