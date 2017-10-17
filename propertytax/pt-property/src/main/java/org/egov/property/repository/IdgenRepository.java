package org.egov.property.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.models.IdGenerationRequest;
import org.egov.models.IdRequest;
import org.egov.models.RequestInfo;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.ValidationUrlNotFoundException;
import org.egov.property.utility.UpicNoGeneration;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Anil
 *
 */
@Repository
public class IdgenRepository {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	LogAwareRestTemplate restTemplate;

	@Autowired
	UpicNoGeneration upicNoGeneration;

	private static final Logger logger = LoggerFactory.getLogger(UpicNoGeneration.class);

	/**
	 * Description: Generating sequence number
	 * 
	 * @param tenantId
	 * @param propertyRequest
	 * @param upicFormat
	 * @return UpicNumber
	 */
	public String getIdGeneration(String tenantId, RequestInfo requestInfo, String upicFormat, String idName) {

		StringBuffer idGenerationUrl = new StringBuffer();
		idGenerationUrl.append(propertiesManager.getIdHostName());
		idGenerationUrl.append(propertiesManager.getIdCreatepath());

		// generating acknowledgement number for all properties
		List<IdRequest> idRequests = new ArrayList<>();
		IdRequest idrequest = new IdRequest();
		idrequest.setFormat(upicFormat);
		idrequest.setIdName(idName);
		idrequest.setTenantId(tenantId);
		IdGenerationRequest idGeneration = new IdGenerationRequest();
		idRequests.add(idrequest);
		idGeneration.setIdRequests(idRequests);
		idGeneration.setRequestInfo(requestInfo);
		String response = null;
		try {
			logger.info("UpicNoGeneration calling id generation service url :" + idGenerationUrl.toString()
					+ " request is " + requestInfo);
			response = restTemplate.postForObject(idGenerationUrl.toString(), idGeneration, String.class);
		} catch (Exception ex) {
			throw new ValidationUrlNotFoundException(propertiesManager.getInvalidIdServiceUrl(),
					idGenerationUrl.toString(), requestInfo);
		}
		return response;
	}

}
