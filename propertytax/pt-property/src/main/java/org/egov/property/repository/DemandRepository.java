package org.egov.property.repository;

import java.net.URI;

import org.egov.models.DemandResponse;
import org.egov.models.RequestInfoWrapper;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.ValidationUrlNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Description: This class will call demand service api's
 * 
 * @author WTC
 *
 */

@Slf4j
@Repository
public class DemandRepository {

	@Autowired
	PropertiesManager propertiesManager;

	/**
	 * Description :This method will get all demands based on upic number and
	 * tenantId
	 * 
	 * @param upicNo
	 * @param tenantId
	 * @param requestInfo
	 * @return demandResponse
	 * @throws Exception
	 */

	public DemandResponse getDemands(String upicNo, String tenantId, RequestInfoWrapper requestInfo) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		DemandResponse resonse = null;
		StringBuffer demandUrl = new StringBuffer();
		demandUrl.append(propertiesManager.getBillingServiceHostname());
		demandUrl.append(propertiesManager.getBillingServiceSearchdemand());
		URI uri = UriComponentsBuilder.fromUriString(demandUrl.toString()).queryParam("tenantId", tenantId)
				.queryParam("consumerCode", upicNo).build(true).encode().toUri();

		log.info("Get demand url is " + uri + " demand request is : " + requestInfo);
		try {
			String demandResponse = restTemplate.postForObject(uri, requestInfo, String.class);
			log.info("Get demand response is :" + demandResponse);
			if (demandResponse != null && demandResponse.contains("Demands")) {
				ObjectMapper objectMapper = new ObjectMapper();
				resonse = objectMapper.readValue(demandResponse, DemandResponse.class);
			}
			return resonse;
		} catch (HttpClientErrorException exception) {
			throw new ValidationUrlNotFoundException(propertiesManager.getInvalidDemandValidation(),
					exception.getMessage(), requestInfo.getRequestInfo());
		}
	}
}
