package org.egov.notification.repository;

import java.net.URI;
import java.util.Set;

import org.egov.models.DemandResponse;
import org.egov.models.RequestInfoWrapper;
import org.egov.notification.config.PropertiesManager;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DemandRepository {
	@Autowired
	PropertiesManager propertiesManager;
	
	@Autowired
	LogAwareRestTemplate restTemplate;

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

	public DemandResponse getDemands(final Set<String> id, final String tenantId, final RequestInfoWrapper requestInfo)
			throws Exception {
		DemandResponse resonse = null;
		final StringBuffer demandUrl = new StringBuffer();
		demandUrl.append(propertiesManager.getBillingServiceHostname());
		demandUrl.append(propertiesManager.getBillingServiceSearchdemand());
		final MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<String, String>();
		requestMap.add("tenantId", tenantId);
		 final String demandId = id.toString().substring(1, id.toString().length() - 1);
	        requestMap.add("demandId", demandId);
		requestMap.add("businessService", propertiesManager.getBusinessService());

		final URI uri = UriComponentsBuilder.fromHttpUrl(demandUrl.toString()).queryParams(requestMap).build().encode().toUri();
				
		log.info("Get demand url is " + uri + " demand request is : " + requestInfo);
		try {
			final String demandResponse = restTemplate.postForObject(uri, requestInfo, String.class);
			log.info("Get demand response is :" + demandResponse);
			if (demandResponse != null && demandResponse.contains("Demands")) {
				final ObjectMapper objectMapper = new ObjectMapper();
				resonse = objectMapper.readValue(demandResponse, DemandResponse.class);
			}

		} catch (final HttpClientErrorException exception) {
			exception.printStackTrace();
		}
		return resonse;
	}
}
