package org.egov.property.repository;

import org.egov.models.RequestInfo;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.InvalidCodeException;
import org.egov.property.exception.ValidationUrlNotFoundException;
import org.egov.property.model.SearchTenantRequest;
import org.egov.property.utility.UpicNoGeneration;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 
 * @author Anil
 *
 */
@Repository
public class TenantRepository {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	LogAwareRestTemplate restTemplate;

	@Autowired
	UpicNoGeneration upicNoGeneration;

	private static final Logger logger = LoggerFactory.getLogger(UpicNoGeneration.class);

	@SuppressWarnings("null")
	public String getTenantRepository(String tenantId, RequestInfo requestInfo) {

		StringBuilder tenantCodeUrl = new StringBuilder();
		tenantCodeUrl.append(propertiesManager.getTenantHostName());
		tenantCodeUrl.append(propertiesManager.getTenantBasepath());
		tenantCodeUrl.append(propertiesManager.getTenantSearchpath());
		String url = tenantCodeUrl.toString();
		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
				// Add query parameter
				.queryParam("code", tenantId);
		String response = null;
		SearchTenantRequest request = new SearchTenantRequest(requestInfo);
		try {
			logger.info("calling tennat service url :" + tenantCodeUrl.toString() + " request is " + requestInfo);
			response = restTemplate.postForObject(builder.buildAndExpand().toUri(), request, String.class);
			logger.info("after calling tennat service response :" + response);
			if (response == null && response.isEmpty()) {
				throw new InvalidCodeException(propertiesManager.getInvalidTenent(), requestInfo);
			}
		} catch (final HttpClientErrorException exception) {
			throw new ValidationUrlNotFoundException(propertiesManager.getInvalidTenent(), exception.getMessage(),
					requestInfo);
		}
		return response;
	}

}
