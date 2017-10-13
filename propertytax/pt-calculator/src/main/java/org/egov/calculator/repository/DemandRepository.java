package org.egov.calculator.repository;

import java.net.URI;

import org.egov.calculator.config.PropertiesManager;
import org.egov.calculator.exception.InvalidPenaltyDataException;
import org.egov.models.DemandResponse;
import org.egov.models.RequestInfoWrapper;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Description: This class will call demand service api's
 * @author WTC
 *
 */

@Slf4j
@Repository
public class DemandRepository {

    @Autowired
    PropertiesManager propertiesManager;
    
    @Autowired
    LogAwareRestTemplate restTemplate;


    /**
     * Description :This method will get all demands based on upic number and tenantId
     * @param upicNo
     * @param tenantId
     * @param requestInfo
     * @return demandResponse
     * @throws Exception
     */

    public DemandResponse getDemands(String upicNo, String tenantId, RequestInfoWrapper requestInfo) throws Exception {
        DemandResponse resonse = null;
        StringBuffer demandUrl = new StringBuffer();
        demandUrl.append(propertiesManager.getDemandUrl());
        demandUrl.append(propertiesManager.getDemandSearchPath());
        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<String, String>();
        requestMap.add("tenantId", tenantId);
        requestMap.add("consumerCode", upicNo);
        requestMap.add("businessService", propertiesManager.getBusinessService());
        URI uri = UriComponentsBuilder.fromHttpUrl(demandUrl.toString()).queryParams(requestMap).build()
                .encode().toUri();

        log.info("Get demand url is" + uri + " demand request is : " + requestInfo);
        try {
            String demandResponse = restTemplate.postForObject(uri, requestInfo, String.class);
            log.info("Get demand response is :" + demandResponse);
            if (demandResponse != null && demandResponse.contains("Demands")) {
                ObjectMapper objectMapper = new ObjectMapper();
                resonse = objectMapper.readValue(demandResponse, DemandResponse.class);
            }
            return resonse;
        } catch (HttpClientErrorException exception) {
            throw new InvalidPenaltyDataException(propertiesManager.getInvalidInput(), requestInfo.getRequestInfo(),
                    exception.getMessage());
        }

    }
}
