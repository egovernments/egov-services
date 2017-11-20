package org.egov.lcms.notification.repository;

import java.net.URI;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.notification.config.PropertiesManager;
import org.egov.lcms.notification.model.AdvocateResponse;
import org.egov.lcms.notification.model.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AdvocateRepository {

	@Autowired
	PropertiesManager propertiesManager;

	public String getAdvocateName(String tenantId, String code, RequestInfo requestInfo) throws Exception {
		
		AdvocateResponse advocateResponse = new AdvocateResponse();
		
		final RestTemplate restTemplate = new RestTemplate();
		final StringBuffer advocateUrl = new StringBuffer();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		advocateUrl.append(propertiesManager.getHostName() + propertiesManager.getBasepath());
		advocateUrl.append(propertiesManager.getAdvocateSearchpath());		
		
		final MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<String, String>();
		requestMap.add("tenantId", tenantId);
		requestMap.add("code", code);

		final URI uri = UriComponentsBuilder.fromHttpUrl(advocateUrl.toString()).queryParams(requestMap).build().encode().toUri();
		log.info("Get Advocate url is " + uri + " Advocate search criteria object is : " + requestInfo);

		try {
			String response = restTemplate.postForObject(uri, requestInfoWrapper, String.class);
			
			if (response != null && response.contains("advocates")) {
				final ObjectMapper objectMapper = new ObjectMapper();
				advocateResponse = objectMapper.readValue(response, AdvocateResponse.class);
			}

		} catch (final HttpClientErrorException exception) {

			log.info("Error occured while fetching User Details!");
			exception.printStackTrace();
		}
		return advocateResponse.getAdvocates().get(0).getName();
	}
}
