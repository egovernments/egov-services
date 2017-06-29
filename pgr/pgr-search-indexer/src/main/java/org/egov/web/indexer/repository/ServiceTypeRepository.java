package org.egov.web.indexer.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.web.indexer.contract.ServiceType;
import org.egov.web.indexer.contract.ServiceTypeResponse;
import org.egov.web.indexer.contract.RequestInfoBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceTypeRepository {

    private static final String SERVICE_TYPE_BY_CODE_URL = "pgr/services/v1/{serviceCode}/_search?tenantId={tenantId}";
    private final String complaintTypeServiceHostname;
    private RestTemplate restTemplate;

    public ServiceTypeRepository(RestTemplate restTemplate,
                                 @Value("${egov.services.pgrrest.host}") String complaintTypeServiceHostname) {
        this.restTemplate = restTemplate;
        this.complaintTypeServiceHostname = complaintTypeServiceHostname;
    }

    public ServiceType fetchServiceTypeByCode(String serviceCode, String tenantId) {
        String url = this.complaintTypeServiceHostname + SERVICE_TYPE_BY_CODE_URL;
        return getComplaintTypeServiceResponse(url, serviceCode, tenantId).getComplaintTypes().get(0);
    }

    private ServiceTypeResponse getComplaintTypeServiceResponse(final String url, String serviceCode, String tenantId) {
        final RequestInfoBody requestInfoBody = new RequestInfoBody(RequestInfo.builder().build());
        return restTemplate.postForObject(url, requestInfoBody, ServiceTypeResponse.class, serviceCode, tenantId);
    }

}