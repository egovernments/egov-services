package org.egov.property.utility;

import java.util.ArrayList;
import java.util.List;
import org.egov.models.ErrorRes;
import org.egov.models.IdGenerationRequest;
import org.egov.models.IdGenerationResponse;
import org.egov.models.IdRequest;
import org.egov.models.Property;
import org.egov.models.RequestInfo;
import org.egov.models.SearchTenantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class will generate the upic number for given property
 * ,we need pass request info as well along with the property object
 * @author Prasad
 *
 */
@Service
public class UpicNoGeneration {

	@Autowired
	Environment environment;

	@Autowired
	RestTemplate restTemplate;

	public String generateUpicNo(Property property, RequestInfo requestInfo) {

		String upicNumber = null;
		StringBuilder tenantCodeUrl = new StringBuilder();
		tenantCodeUrl.append(environment.getProperty("egov.services.tenant.host"));
		tenantCodeUrl.append(environment.getProperty("egov.services.tenant.base.path"));
		tenantCodeUrl.append(environment.getProperty("egov.services.tenant.search.path"));
		String url = tenantCodeUrl.toString();
		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
				// Add query parameter
				.queryParam("code", property.getTenantId());
		try {
			SearchTenantResponse searchTenantResponse = restTemplate.postForObject(
					builder.buildAndExpand().toUri(), requestInfo,
					SearchTenantResponse.class);
			if (searchTenantResponse.getTenant().size() > 0) {
				org.egov.models.City city = searchTenantResponse.getTenant().get(0).getCity();
				String cityCode = city.getCode();
				String upicFormat = environment.getProperty("upic.number.format");
				upicNumber = getUpicNumber(property.getTenantId(), requestInfo, upicFormat);
				upicNumber = String.format("%08d", Integer.parseInt(upicNumber));
				if (cityCode != null) {
					upicNumber = cityCode + "-" + upicNumber;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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

		StringBuffer idGenerationUrl = new StringBuffer();
		idGenerationUrl.append(environment.getProperty("egov.services.egov_idgen.hostname"));
		idGenerationUrl.append(environment.getProperty("egov.services.egov_idgen.createpath"));

		// generating acknowledgement number for all properties
		String UpicNumber = null;
		List<IdRequest> idRequests = new ArrayList<>();
		IdRequest idrequest = new IdRequest();
		idrequest.setFormat(upicFormat);
		idrequest.setIdName(environment.getProperty("id.idName"));
		idrequest.setTenantId(tenantId);
		IdGenerationRequest idGeneration = new IdGenerationRequest();
		idRequests.add(idrequest);
		idGeneration.setIdRequests(idRequests);
		idGeneration.setRequestInfo(requestInfo);
		String response = null;
		try {
			response = restTemplate.postForObject(idGenerationUrl.toString(), idGeneration, String.class);
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
			if (idResponse.getResponseInfo().getStatus().toString()
					.equalsIgnoreCase(environment.getProperty("success"))) {
				if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
					UpicNumber = idResponse.getIdResponses().get(0).getId();
			}
		}

		return UpicNumber;
	}

}
