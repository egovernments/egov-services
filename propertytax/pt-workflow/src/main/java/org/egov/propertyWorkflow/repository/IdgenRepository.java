package org.egov.propertyWorkflow.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.models.ErrorRes;
import org.egov.models.IdGenerationRequest;
import org.egov.models.IdGenerationResponse;
import org.egov.models.IdRequest;
import org.egov.models.PropertyRequest;
import org.egov.propertyWorkflow.config.PropertiesManager;
import org.egov.propertyWorkflow.consumer.WorkFlowUtil;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

	private static final Logger logger = LoggerFactory.getLogger(WorkFlowUtil.class);

	/**
	 * Description: Generating sequence number
	 * 
	 * @param tenantId
	 * @param propertyRequest
	 * @param upicFormat
	 * @return UpicNumber
	 */
	public String getUpicNumberRepository(String tenantId, PropertyRequest propertyRequest, String upicFormat) throws Exception{

		StringBuffer idGenerationUrl = new StringBuffer();
		idGenerationUrl.append(propertiesManager.getIdHostName());
		idGenerationUrl.append(propertiesManager.getIdBasepath());
		idGenerationUrl.append(propertiesManager.getIdCreatepath());

		// generating acknowledgement number for all properties
		String UpicNumber = null;
		List<IdRequest> idRequests = new ArrayList<>();
		IdRequest idrequest = new IdRequest();
		idrequest.setFormat(upicFormat);
		idrequest.setIdName(propertiesManager.getIdName());
		idrequest.setTenantId(tenantId);
		IdGenerationRequest idGeneration = new IdGenerationRequest();
		idRequests.add(idrequest);
		idGeneration.setIdRequests(idRequests);
		idGeneration.setRequestInfo(propertyRequest.getRequestInfo());
		String response = null;
		logger.info("IdgenRepository idGenerationUrl :" + idGenerationUrl.toString()
				+ "\n IdgenRepository idGenerationRequest :" + idGeneration);
		try {
			response = restTemplate.postForObject(idGenerationUrl.toString(), idGeneration, String.class);
			logger.info("IdgenRepository getupicnumber response  ---->>  " + response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		ErrorRes errorResponse = gson.fromJson(response, ErrorRes.class);
		IdGenerationResponse idResponse = gson.fromJson(response, IdGenerationResponse.class);

		if (errorResponse.getErrors() != null && errorResponse.getErrors().size() > 0) {
			// TODO throw error exception
			// Error error = errorResponse.getErrors().get(0);
		} else if (idResponse.getResponseInfo() != null) {
			if (idResponse.getResponseInfo().getStatus().toString().equalsIgnoreCase(propertiesManager.getSuccess())) {
				if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
					UpicNumber = idResponse.getIdResponses().get(0).getId();
			}
		}

		return UpicNumber;
	}

}
