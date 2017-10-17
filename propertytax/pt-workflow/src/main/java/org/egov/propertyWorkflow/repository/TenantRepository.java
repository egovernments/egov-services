package org.egov.propertyWorkflow.repository;

import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.propertyWorkflow.config.PropertiesManager;
import org.egov.propertyWorkflow.consumer.WorkFlowUtil;
import org.egov.propertyWorkflow.consumer.WorkflowConsumer;
import org.egov.propertyWorkflow.models.City;
import org.egov.propertyWorkflow.models.SearchTenantResponse;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
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
	WorkflowConsumer workflowConsumer;

	private static final Logger logger = LoggerFactory.getLogger(WorkFlowUtil.class);

	/**
	 * This method will generate UPIC NO from the Property
	 * 
	 * @param property
	 * @param propertyRequest
	 * @return upicNumber
	 */
	public String generateUpicNoRepository(Property property, PropertyRequest propertyRequest) throws Exception{

		String upicNumber = null;
		StringBuilder tenantCodeUrl = new StringBuilder();
		tenantCodeUrl.append(propertiesManager.getTenantHostName());
		tenantCodeUrl.append(propertiesManager.getTenantBasepath());
		tenantCodeUrl.append(propertiesManager.getTenantSearchpath());
		String url = tenantCodeUrl.toString();
		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
				// Add query parameter
				.queryParam("code", property.getTenantId());
		logger.info("getUpicNumberRepository builderuri :" + builder.buildAndExpand().toUri()
				+ "\n getUpicNumberRepository PropertyRequest :" + propertyRequest.getRequestInfo());
		SearchTenantResponse searchTenantResponse = restTemplate.postForObject(builder.buildAndExpand().toUri(),
				propertyRequest.getRequestInfo(), SearchTenantResponse.class);
		logger.info("getUpicNumberRepository SearchTenantResponse  ---->>  " + searchTenantResponse);
		if (searchTenantResponse.getTenant().size() > 0) {
			City city = searchTenantResponse.getTenant().get(0).getCity();
			String cityCode = city.getCode();
			String upicFormat = propertiesManager.getUpicNumberFormat();
			upicNumber = workflowConsumer.getUpicNumber(property.getTenantId(), propertyRequest, upicFormat);
			upicNumber = String.format("%08d", Integer.parseInt(upicNumber));
			if (cityCode != null) {
				upicNumber = cityCode + upicNumber;
			}
		}
		return upicNumber;
	}

}
