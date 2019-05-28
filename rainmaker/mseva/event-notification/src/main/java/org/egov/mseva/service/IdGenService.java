package org.egov.mseva.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IdGenService {
	
/*	@Autowired
	private RestCallRepository repository;
	
	@Autowired
	private PropertiesManager properties;

	
	*//**
	 * Makes call to the idgen service to fetch ids for the employee object. Format of the id configurable.
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param count
	 * @param name
	 * @param format
	 * @return
	 *//*
	public IdGenerationResponse getId(RequestInfo requestInfo, String tenantId, Integer count, String name, String format) {
		StringBuilder uri = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();
		uri.append(properties.getIdGenHost()).append(properties.getIdGenEndpoint());
		List<IdRequest> reqList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			reqList.add(IdRequest.builder().idName(name).format(format).tenantId(tenantId).build());
		}
		IdGenerationRequest request = IdGenerationRequest.builder().idRequests(reqList).requestInfo(requestInfo).build();
		IdGenerationResponse response = null;
		try {
			response = mapper.convertValue(repository.fetchResult(uri, request), IdGenerationResponse.class);
		}catch(Exception e) {
			log.error("Exception while generating ids: ",e);
			log.error("Request: "+request);
			throw new CustomException(ErrorConstants.HRMS_GENERATE_ID_ERROR_CODE,ErrorConstants.HRMS_GENERATE_ID_ERROR_MSG);

		}
		return response;
	}*/
}
