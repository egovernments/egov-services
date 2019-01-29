package org.egov.hrms.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.hrms.config.PropertiesManager;
import org.egov.hrms.repository.RestCallRepository;
import org.egov.hrms.web.contract.EmployeeRequest;
import org.egov.hrms.web.contract.IdGenerationRequest;
import org.egov.hrms.web.contract.IdGenerationResponse;
import org.egov.hrms.web.contract.IdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IdGenService {
	
	@Autowired
	private RestCallRepository repository;
	
	@Autowired
	private PropertiesManager properties;

	
	public void setIds(EmployeeRequest employeeRequest) {
		String tenantId = employeeRequest.getEmployees().get(0).getTenantId();
		IdGenerationResponse response = getId(employeeRequest.getRequestInfo(), tenantId, employeeRequest.getEmployees().size(),
				properties.getHrmsIdGenKey(), properties.getHrmsIdGenFormat());
		if(null != response) {
			for(int i = 0; i < employeeRequest.getEmployees().size(); i++) {
				
				employeeRequest.getEmployees().get(i).setCode(response.getIdResponses().get(i).getId());
			}
		}
	}
	
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
		}
		return response;
	}
}
