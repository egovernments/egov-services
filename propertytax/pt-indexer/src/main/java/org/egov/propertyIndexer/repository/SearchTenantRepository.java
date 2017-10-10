package org.egov.propertyIndexer.repository;

import org.egov.models.RequestInfo;
import org.egov.models.SearchTenantResponse;
import org.egov.propertyIndexer.config.PropertiesManager;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Prasad
 * 
 *
 */
@Repository
@Slf4j
public class SearchTenantRepository {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private LogAwareRestTemplate restTemplate;

	/**
	 * This API will get the city details for the given tenantId
	 * 
	 * @param tenantId
	 * @return {@link SearchTenantResponse}
	 */
	public SearchTenantResponse getCityDetailsForTenant(String tenantId, RequestInfo requestInfo) throws Exception {

		StringBuilder tenantCodeUrl = new StringBuilder();
		tenantCodeUrl.append(propertiesManager.getTenantHostName());
		tenantCodeUrl.append(propertiesManager.getTenantBasePath());
		tenantCodeUrl.append(propertiesManager.getTenatSearchPath());
		String url = tenantCodeUrl.toString();
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("code", tenantId);
		log.info("Indexer Repository builderuri :" + builder.buildAndExpand().toUri()
				+ "\n Indexer Repository requestInfo:" + requestInfo);
		SearchTenantResponse searchTenantResponse = null;

		searchTenantResponse = restTemplate.postForObject(builder.buildAndExpand().toUri(), requestInfo,
				SearchTenantResponse.class);
		log.info("Indexer Repository SearchTenantResponse  ---->>  " + searchTenantResponse);

		return searchTenantResponse;
	}

}
