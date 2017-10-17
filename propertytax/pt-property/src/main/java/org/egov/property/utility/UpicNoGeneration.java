package org.egov.property.utility;

import org.egov.models.ErrorRes;
import org.egov.models.IdGenerationResponse;
import org.egov.models.Property;
import org.egov.models.RequestInfo;
import org.egov.property.config.PropertiesManager;
import org.egov.property.model.City;
import org.egov.property.model.SearchTenantResponse;
import org.egov.property.repository.IdgenRepository;
import org.egov.property.repository.TenantRepository;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * This class will generate the upic number for given property ,we need pass
 * request info as well along with the property object
 * 
 * @author Prasad
 *
 */
@Slf4j
@Service
public class UpicNoGeneration {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	LogAwareRestTemplate restTemplate;

	@Autowired
	TenantRepository tenantRepository;

	@Autowired
	IdgenRepository idgenRepository;

	public String generateUpicNo(Property property, RequestInfo requestInfo) throws Exception {
		String upicNumber = null;
		String response = null;
		response = tenantRepository.getTenantRepository(property.getTenantId(), requestInfo);
		SearchTenantResponse searchTenantResponse = null;
		if (response != null && !response.isEmpty()) {
			ObjectMapper mapper = new ObjectMapper();
			searchTenantResponse = mapper.readValue(response, SearchTenantResponse.class);
			City city = searchTenantResponse.getTenant().get(0).getCity();
			String cityCode = city.getCode();
			String upicFormat = propertiesManager.getUpicNumberFormat();
			upicNumber = getUpicNumber(property.getTenantId(), requestInfo, upicFormat);
			upicNumber = String.format("%08d", Integer.parseInt(upicNumber));
			if (cityCode != null) {
				upicNumber = cityCode + upicNumber;
			}
		}

		return upicNumber;
	}

	/**
	 * Description: Generating sequence number
	 * 
	 * @param tenantId
	 * @param propertyRequest
	 * @param upicFormat
	 * @return UpicNumber
	 */
	public String getUpicNumber(String tenantId, RequestInfo requestInfo, String upicFormat) {

		String response = null;
		String UpicNumber = null;
		response = idgenRepository.getIdGeneration(tenantId, requestInfo, upicFormat, propertiesManager.getIdName());
		log.info("UpicNoGeneration After calling the id generation service response :" + response);
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
