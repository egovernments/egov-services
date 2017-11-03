package org.egov.lcms.repository;

import java.net.URI;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.DepartmentResponse;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Prasad
 *
 */
@Slf4j
@Repository
public class DepartmentRepository {
	
	@Autowired
	PropertiesManager propertiesManager;
	
	@Autowired
	RestTemplate restTemplate;
	
	
	public DepartmentResponse getDepartments(String tenantId, String departmentCode, RequestInfoWrapper requestInfo)
			throws Exception {
		DepartmentResponse response = null;
		


		
		final StringBuffer commomServiceUrl = new StringBuffer();
		commomServiceUrl.append(propertiesManager.getCommonServiceBasepath());
		commomServiceUrl.append(propertiesManager.getCommonServiceSearchPath());
		final MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<String, String>();
		requestMap.add("tenantId", tenantId);
		requestMap.add("code", departmentCode);

		final URI uri = UriComponentsBuilder.fromHttpUrl(commomServiceUrl.toString()).queryParams(requestMap).build().encode()
				.toUri();
	
		try {
			response = restTemplate.postForObject(uri, requestInfo, DepartmentResponse.class);
			log.info("Get Department response is :" + response);
			
		
		} catch (Exception exception) {
		 
			throw new CustomException(propertiesManager.getSearchDeparatmentErrorCode(), propertiesManager.getSearchDepartmentErrorMessage());
		
		}
	

		return response;
}
	
}
