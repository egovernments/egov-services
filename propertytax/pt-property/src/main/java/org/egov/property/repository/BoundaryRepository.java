package org.egov.property.repository;

import java.net.URI;

import org.egov.models.RequestInfoWrapper;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.InvalidPropertyBoundaryException;
import org.egov.property.exception.ValidationUrlNotFoundException;
import org.egov.property.model.BoundaryResponseInfo;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
public class BoundaryRepository {

	private static final Logger logger = LoggerFactory.getLogger(BoundaryRepository.class);

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private LogAwareRestTemplate restTemplate;

	public Boolean isBoundaryExists(String tenantId, RequestInfoWrapper requestInfoWrapper, String codes,
			String boundaryType, String hierarchyType) {

		StringBuffer BoundaryURI = new StringBuffer();
		BoundaryURI.append(propertiesManager.getLocationHostName()).append(propertiesManager.getLocationSearchpath())
				.append(propertiesManager.getBoundarySearch());

		URI uri = UriComponentsBuilder.fromUriString(BoundaryURI.toString()).queryParam("tenantId", tenantId)
				.queryParam("codes", codes).queryParam("boundaryType", boundaryType)
				.queryParam("hierarchyType", hierarchyType).build().encode().toUri();
		logger.info("BoundaryRepository BoundaryURI ---->> " + BoundaryURI.toString() + "\n request uri ---->> " + uri);
		BoundaryResponseInfo boundaryResponseInfo = null;

		try {
			boundaryResponseInfo = restTemplate.postForObject(uri, requestInfoWrapper, BoundaryResponseInfo.class);
			logger.info("BoundaryRepository BoundaryResponseInfo ---->> " + boundaryResponseInfo);
		} catch (HttpClientErrorException ex) {
			throw new ValidationUrlNotFoundException(propertiesManager.getInvalidBoundaryValidationUrl(),
					uri.toString(), requestInfoWrapper.getRequestInfo());
		}
		if (boundaryResponseInfo.getResponseInfo() != null && boundaryResponseInfo.getBoundary().size() != 0) {
			return true;
		} else {
			throw new InvalidPropertyBoundaryException(propertiesManager.getInvalidPropertyBoundary(),
					propertiesManager.getInvalidBoundaryMessage().replace("{boundaryId}", "" + codes),
					requestInfoWrapper.getRequestInfo());
		}
	}
}