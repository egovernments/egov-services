package org.egov.notification.repository;

import java.net.URI;
import java.util.List;

import org.egov.models.Property;
import org.egov.models.PropertyResponse;
import org.egov.models.RequestInfoWrapper;
import org.egov.notification.config.PropertiesManager;
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
public class PropertyRepository {
	@Autowired
	PropertiesManager propertiesManager;

	/**
	 * Description :This method will get property based on upic number and
	 * tenantId
	 *
	 * @param upicNo
	 * @param tenantId
	 * @param requestInfo
	 * @return propertyResponse
	 * @throws Exception
	 */
	public List<Property> getProperty(String tenantId, String upicNo, RequestInfoWrapper requestInfo) throws Exception{
		
		final RestTemplate restTemplate = new RestTemplate();
		PropertyResponse propertyResponse = null;
		final StringBuffer propertyUrl = new StringBuffer();
		propertyUrl.append(propertiesManager.getPropertyHostName()+propertiesManager.getPropertyBasePath());
		propertyUrl.append(propertiesManager.getSearchProperty());
		final MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<String, String>();
		requestMap.add("tenantId", tenantId);
		requestMap.add("upicNumber", upicNo);
		final URI uri = UriComponentsBuilder.fromHttpUrl(propertyUrl.toString()).queryParams(requestMap).build().encode().toUri();
		log.info("Get property url is " + uri + " property request is : " + requestInfo);
		try {
			String response = restTemplate.postForObject(uri, requestInfo, String.class);
			log.info("Get property response is :" + response);
			if (response != null && response.contains("properties")) {
				final ObjectMapper objectMapper = new ObjectMapper();
				propertyResponse = objectMapper.readValue(response, PropertyResponse.class);
			}

		} catch (final HttpClientErrorException exception) {
			exception.printStackTrace();
		}
		return propertyResponse.getProperties();
	}	
}
