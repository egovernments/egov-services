package org.egov.propertyWorkflow.repository;

import java.net.URI;

import org.egov.models.PropertyResponse;
import org.egov.models.RequestInfoWrapper;
import org.egov.propertyWorkflow.config.PropertiesManager;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class PropertyRepository {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	LogAwareRestTemplate restTemplate;

	/**
	 * This Method will check whether any property already exists with the same
	 * upicNumber (This case arises as of now only in modify property)
	 * 
	 * @param upicNumber
	 */
	public Boolean checkWhetherPropertyExistsWithUpic(String upicNumber, String tenantId,
			org.egov.models.RequestInfo requestInfo) throws Exception {

		Boolean isExists = Boolean.FALSE;

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		PropertyResponse response = null;
		StringBuilder searchPropertyUrl = new StringBuilder();
		searchPropertyUrl.append(propertiesManager.getPropertyHostName());
		searchPropertyUrl.append(propertiesManager.getPropertyBasepath());
		searchPropertyUrl.append(propertiesManager.getPropertySearch());
		String url = searchPropertyUrl.toString();

		MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<String, String>();
		requestMap.add("tenantId", tenantId);
		requestMap.add("upicNumber", upicNumber);
		URI uri = UriComponentsBuilder.fromHttpUrl(url).queryParams(requestMap).build().encode().toUri();
		String propertyResponse = restTemplate.postForObject(uri, requestInfoWrapper, String.class);

		if (propertyResponse != null && propertyResponse.contains("properties")) {
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.readValue(propertyResponse, PropertyResponse.class);
			if (response.getProperties().size() > 0) {
				isExists = Boolean.TRUE;
			}

		}

		return isExists;

	}

}
