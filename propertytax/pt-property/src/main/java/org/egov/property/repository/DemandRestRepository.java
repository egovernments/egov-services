package org.egov.property.repository;

import org.egov.models.DemandRequest;
import org.egov.models.DemandResponse;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.DemandUpdateException;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DemandRestRepository {

	private final LogAwareRestTemplate restTemplate;

	private final String url;

	private PropertiesManager propertiesManager;

	public DemandRestRepository(LogAwareRestTemplate restTemplate, PropertiesManager propertiesManager) {
		this.restTemplate = restTemplate;
		this.propertiesManager = propertiesManager;
		this.url = propertiesManager.getBillingServiceHostname() + propertiesManager.getBillingServiceUpdatedemand();
	}

	public DemandResponse updateDemand(DemandRequest demandRequest) {
		DemandResponse demandResponse = DemandResponse.builder().build();
		try {
			demandResponse = restTemplate.postForObject(url, demandRequest, DemandResponse.class);
		} catch (HttpStatusCodeException ex) {
			if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST))
				throw new DemandUpdateException(ex);
		} catch (Exception e) {
			log.error("Following Exception Occurred While Calling User Service : " + e.getMessage());
			throw new RuntimeException(e);
		}
		return demandResponse;
	}
}
