package org.egov.eis.indexer.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.eis.indexer.config.PropertiesManager;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.tenant.web.contract.City;
import org.egov.tenant.web.contract.Tenant;
import org.egov.tenant.web.contract.TenantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Repository
public class TenantService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PropertiesManager propertiesManager;

	public Tenant getTenant(String tenant, RequestInfoWrapper requestInfoWrapper) {
        URI url = null;
        TenantResponse tenantResponse = null;
        try {
            url = new URI(propertiesManager.getTenantServiceHost()
                    + propertiesManager.getTenantServiceBasePath()
                    + propertiesManager.getTenantServiceSearchPath() + "?code=" + tenant);
            log.info(url.toString());
            tenantResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper), TenantResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return tenantResponse.getTenant().get(0);
    }

    private HttpEntity<RequestInfoWrapper> getRequestInfoAsHttpEntity(RequestInfoWrapper requestInfoWrapper) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestInfoWrapper> httpEntityRequest = new HttpEntity<RequestInfoWrapper>(requestInfoWrapper,
                headers);
        return httpEntityRequest;
    }
}