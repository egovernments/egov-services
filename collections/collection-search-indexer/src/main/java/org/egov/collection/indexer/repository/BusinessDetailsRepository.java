package org.egov.collection.indexer.repository;

import org.egov.collection.indexer.contract.BusinessCategory;
import org.egov.collection.indexer.contract.BusinessCategoryResponse;
import org.egov.collection.indexer.contract.BusinessDetailsResponse;
import org.egov.collection.indexer.contract.factory.RequestInfoWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class BusinessDetailsRepository {

    @Autowired
    public RestTemplate restTemplate;

    public String commonServiceHost;

    private String searchBusinessDetailsUrl;

    private String searchBusinessCategoryUrl;

    public BusinessDetailsRepository(RestTemplate restTemplate, @Value("${egov.common.service.host}") final String commonServiceHost,
                                     @Value("${egov.services.get_businessdetails_by_codes}") final String searchBusinessDetailsUrl,
                                     @Value("${egov.services.get_businesscategory_by_id}") final String searchBusinessCategoryUrl) {
        this.restTemplate = restTemplate;
        this.commonServiceHost =commonServiceHost;
        this.searchBusinessDetailsUrl = commonServiceHost + searchBusinessDetailsUrl;
        this.searchBusinessCategoryUrl = commonServiceHost + searchBusinessCategoryUrl;
    }

    public BusinessDetailsResponse getBusinessDetails(List<String> businessCodes,String tenantId,RequestInfo requestInfo) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        String businessDetailsCodes = String.join(",", businessCodes);
        return restTemplate.postForObject(searchBusinessDetailsUrl, requestInfoWrapper,
                    BusinessDetailsResponse.class,tenantId,businessDetailsCodes);
    }

    public List<BusinessCategory> getBusinessCategory(final Long id,String tenantId,final RequestInfo requestInfo) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        return restTemplate.postForObject(searchBusinessCategoryUrl, requestInfoWrapper,
                BusinessCategoryResponse.class,tenantId, id).getBusinessCategoryInfo();
    }
}
