package org.egov.pg.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pg.config.AppProperties;
import org.egov.pg.models.BusinessDetailsRequestInfo;
import org.egov.pg.models.BusinessDetailsResponse;
import org.egov.pg.web.models.RequestInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
@Slf4j
public class BusinessDetailsRepository {

    private RestTemplate restTemplate;
//    private AppProperties appProperties;

    private String url;

    @Autowired
    public BusinessDetailsRepository(RestTemplate restTemplate, AppProperties appProperties) {
        this.restTemplate = restTemplate;
        this.url = appProperties.getBusinessDetailsHost() + appProperties.getBusinessDetailsPath();
    }

    public List<BusinessDetailsRequestInfo> getBusinessDetails(List<String> businessCodes, String tenantId,
                                                                RequestInfo
            requestInfo) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper(requestInfo);
        String businessDetailsCodes = String.join(",", businessCodes);
        try {
             BusinessDetailsResponse businessDetailsResponse = restTemplate.postForObject(url, requestInfoWrapper,
                    BusinessDetailsResponse.class, tenantId, businessDetailsCodes);
            return businessDetailsResponse.getBusinessDetails();
        } catch (HttpClientErrorException e) {
            log.error("Unable to fetch business detail for {} and tenant id {} from egov-common-masters, " ,
                    businessCodes, tenantId, e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Unable to fetch business detail for {} and tenant id, " , businessCodes, tenantId, e);
            throw new CustomException("BUSINESS_DETAIL_SERVICE_ERROR", "Unable to fetch business detail from " +
                    "egov-common-masters, unknown error occurred");
        }
    }
}
