package org.egov.pg.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pg.config.AppProperties;
import org.egov.pg.models.Bill;
import org.egov.pg.models.BillResponse;
import org.egov.pg.web.models.RequestInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Repository
@Slf4j
public class BillingRepository {

    private AppProperties appProperties;
    private RestTemplate restTemplate;

    @Autowired
    BillingRepository(RestTemplate restTemplate, AppProperties appProperties) {
        this.restTemplate = restTemplate;
        this.appProperties = appProperties;
    }


    public List<Bill> fetchBill(RequestInfo requestInfo, String tenantId, String billId) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("tenantId", tenantId);
        queryParams.add("billId", billId);

        String uri = UriComponentsBuilder
                .fromHttpUrl(appProperties.getBillingServiceHost())
                .path(appProperties.getBillingServicePath())
                .queryParams(queryParams)
                .build()
                .toUriString();

        RequestInfoWrapper wrapper = new RequestInfoWrapper(requestInfo);

        try {
            BillResponse response = restTemplate.postForObject(uri, wrapper, BillResponse.class);
            return response.getBill();
        } catch (HttpClientErrorException e) {
            log.error("Unable to fetch bill for Bill ID: {} in tenant {}", billId, tenantId, e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Unable to fetch bill for Bill ID: {} in tenant {}", billId, tenantId, e);
            throw new CustomException("BILLING_SERVICE_SEARCH_ERROR", "Failed to fetch bill, unknown error occurred");
        }
    }


}
