package org.egov.lcms.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 
 * @author Prasad
 *
 */
@Repository
public class TenantRepository {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	LogAwareRestTemplate restTemplate;

	private static final Logger logger = LoggerFactory.getLogger(TenantRepository.class);

	public String getTenantRepository(String tenantId, RequestInfo requestInfo) {

		StringBuilder tenantCodeUrl = new StringBuilder();
		tenantCodeUrl.append(propertiesManager.getTenantHostName());
		tenantCodeUrl.append(propertiesManager.getTenantBasePath());
		tenantCodeUrl.append(propertiesManager.getTenantSearchPath());
		String url = tenantCodeUrl.toString();
		
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		
		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
				// Add query parameter
				.queryParam("code", tenantId);
		String response = null;
		try {
			logger.info("calling tennat service url :" + tenantCodeUrl.toString() + " request is " + requestInfoWrapper);
			response = restTemplate.postForObject(builder.buildAndExpand().toUri(), requestInfoWrapper, String.class);
			logger.info("after calling tennat service response :" + response);
			if (response == null) {
				throw new CustomException(propertiesManager.getTenantCode(),
						propertiesManager.getTenantServiceErrorMsg());
			}
		} catch (final HttpClientErrorException exception) {
			logger.info("Exception occured in while getting the tennat service response :" + response);
			throw new CustomException(propertiesManager.getTenantCode(), propertiesManager.getTenantServiceErrorMsg());
		}
		return response;
	}

}
