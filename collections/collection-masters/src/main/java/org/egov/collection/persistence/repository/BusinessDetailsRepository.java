package org.egov.collection.persistence.repository;

import org.egov.collection.web.contract.BusinessDetailsRequestInfo;
import org.egov.collection.web.contract.BusinessDetailsResponse;
import org.egov.collection.web.contract.factory.RequestInfoWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class BusinessDetailsRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(BusinessDetailsRepository.class);
	
    @Autowired
    public RestTemplate restTemplate;

    public String commonServiceHost;

    private String url;

    public BusinessDetailsRepository(RestTemplate restTemplate, @Value("${egov.common.service.host}") final String commonServiceHost,
                                     @Value("${egov.services.get_businessdetails_by_codes}") final String url) {
        this.restTemplate = restTemplate;
        this.commonServiceHost =commonServiceHost;
        this.url = commonServiceHost + url;
    }

    public List<BusinessDetailsRequestInfo> getBusinessDetails(List<Long> id,String tenantId,RequestInfo requestInfo) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        LOGGER.info("URI: "+url);
        LOGGER.info("tenantid: "+tenantId);
        LOGGER.info("business details id: "+id);
        return restTemplate.postForObject(url, requestInfoWrapper,
                    BusinessDetailsResponse.class,tenantId,id).getBusinessDetails();
    }
}
