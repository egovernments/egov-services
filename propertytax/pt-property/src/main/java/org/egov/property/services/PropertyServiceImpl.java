package org.egov.property.services;

import java.util.ArrayList;
import java.util.List;

import org.egov.models.Error;
import org.egov.models.ErrorRes;
import org.egov.models.IdGenerationRequest;
import org.egov.models.IdGenerationResponse;
import org.egov.models.IdRequest;
import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.PropertyResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.property.exception.IdGenerationException;
import org.egov.property.exception.ValidationUrlNotFoundException;
import org.egov.property.propertyConsumer.Producer;
import org.egov.property.util.PropertyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class PropertyServiceImpl implements PropertyService {

	@Autowired
	private Environment env;

	@Autowired
	PropertyValidator propertyValidator;

	@Autowired
	Producer producer;

	@Autowired
	Environment environment;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	PropertySearchService propertySearchService;

	@Override
	public PropertyResponse createProperty(PropertyRequest propertyRequest) {
		// TODO Auto-generated method stub
		for (Property property : propertyRequest.getProperties()) {
			propertyValidator.validatePropertyBoundary(property, propertyRequest.getRequestInfo());
			property = generateAcknowledegeNumber(property, propertyRequest.getRequestInfo());
			sendToKafka(environment.getProperty("user.create"), propertyRequest);
		}
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(propertyRequest.getRequestInfo(), true);
		PropertyResponse propertyResponse = new PropertyResponse();
		propertyResponse.setResponseInfo(responseInfo);
		propertyResponse.setProperties(propertyRequest.getProperties());
		return propertyResponse;
	}

	@Override
	public PropertyResponse updateProperty(PropertyRequest propertyRequest) {
		for (Property property : propertyRequest.getProperties()) {
			propertyValidator.validatePropertyBoundary(property, propertyRequest.getRequestInfo());
			propertyValidator.validateWorkflowDeatails(property, propertyRequest.getRequestInfo());
			sendToKafka(environment.getProperty("user.update"), propertyRequest);
		}
		return null;
	}

	/**
	 * Description: sending property request to kafka
	 * 
	 * @param topic
	 * @param propertyRequestInfo
	 */
	private void sendToKafka(String topic, PropertyRequest propertyRequest) {
		producer.send(topic, propertyRequest);

	}

	/**
	 * Description: Generating acknowledge number for property
	 * 
	 * @param property
	 * @param requestInfo
	 * @return
	 */
	public Property generateAcknowledegeNumber(Property property, RequestInfo requestInfo) {

		StringBuffer idGenerationUrl = new StringBuffer();
		idGenerationUrl.append(environment.getProperty("egov.services.id_service.hostname"));
		idGenerationUrl.append(environment.getProperty("egov.services.id_service.basepath"));
		idGenerationUrl.append(environment.getProperty("egov.services.id_service.createpath"));

		// generating acknowledgement number for all properties

		List<IdRequest> idRequests = new ArrayList<>();
		IdRequest idrequest = new IdRequest();
		idrequest.setFormat(environment.getProperty("id.format"));
		idrequest.setIdName(environment.getProperty("id.idName"));
		idrequest.setTenantId(property.getTenantId());
		IdGenerationRequest idGeneration = new IdGenerationRequest();
		idRequests.add(idrequest);
		idGeneration.setIdRequests(idRequests);
		idGeneration.setRequestInfo(requestInfo);
		String response = null;
		try {
			response = restTemplate.postForObject(idGenerationUrl.toString(), idGeneration, String.class);
		} catch (Exception ex) {
			throw new ValidationUrlNotFoundException(env.getProperty("invalid.id.service.url"),
					idGenerationUrl.toString(), requestInfo);
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		ErrorRes errorResponse = gson.fromJson(response, ErrorRes.class);
		IdGenerationResponse idResponse = gson.fromJson(response, IdGenerationResponse.class);

		if (errorResponse.getErrors() != null && errorResponse.getErrors().size() > 0) {
			Error error = errorResponse.getErrors().get(0);
			throw new IdGenerationException(error.getMessage(), error.getDescription(), requestInfo);
		} else if (idResponse.getResponseInfo() != null) {
			if (idResponse.getResponseInfo().getStatus().toString()
					.equalsIgnoreCase(environment.getProperty("success"))) {
				if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
					property.getPropertyDetail().setApplicationNo(idResponse.getIdResponses().get(0).getId());
			}
		}

		return property;
	}

	@Override
	public PropertyResponse searchProperty(RequestInfo requestInfo, String tenantId, Boolean active, String upicNo,
			Integer pageSize, Integer pageNumber, String[] sort, String oldUpicNo, String mobileNumber,
			String aadhaarNumber, String houseNoBldgApt, Integer revenueZone, Integer revenueWard, Integer locality,
			String ownerName, Integer demandFrom, Integer demandTo) {

		return propertySearchService.searchProperty(requestInfo, tenantId, active, upicNo, pageSize, pageNumber, sort,
				oldUpicNo, mobileNumber, aadhaarNumber, houseNoBldgApt, revenueZone, revenueWard, locality, ownerName,
				demandFrom, demandTo);

	}

}
