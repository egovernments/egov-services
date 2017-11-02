package org.egov.lcms.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.IdGenerationRequest;
import org.egov.lcms.models.IdGenerationResponse;
import org.egov.lcms.models.IdRequest;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Prasad
 *
 */
@Repository
public class IdGenerationRepository {
	
	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	LogAwareRestTemplate restTemplate;


	private static final Logger logger = LoggerFactory.getLogger(IdGenerationRepository.class);

	/**
	 * Description: Generating sequence number
	 * 
	 * @param tenantId
	 * @param propertyRequest
	 * @param upicFormat
	 * @return UpicNumber
	 */
	public IdGenerationResponse getIdGeneration(String tenantId, RequestInfo requestInfo, String upicFormat, String idName) throws Exception {

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
		IdGenerationResponse response = null;
		try {
			logger.info("UpicNoGeneration calling id generation service url :" + idGenerationUrl.toString()
					+ " request is " + requestInfo);
			 response = restTemplate.postForObject(idGenerationUrl.toString(), idGeneration, IdGenerationResponse.class);
		} catch (Exception ex) {
			logger.info("Exception in generating the code :" + ex.getMessage());
			throw new CustomException(propertiesManager.getInvalidIdGenerationCode(),
					propertiesManager.getIdGenerationExceptionMessage());
		}
		return response;
	}


}
