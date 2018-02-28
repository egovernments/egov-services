package org.egov.pgr.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MDMSRespository {
	
	@Autowired
	private LogAwareRestTemplate restTemplate;
	
	@Value("${mdms.host}")
	private String mdmsHost;
	
	@Value("${mdms.search.endpoint}")
	private String mdmsEndpoint;
	
	public Object fetchServiceCodes(RequestInfo requestInfo, String tenantId, String department) {
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Object response = null;
		StringBuilder uri = new StringBuilder();
		uri.append(mdmsHost).append(mdmsEndpoint);
		log.info("URI: "+uri.toString());
		
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder().name("serviceDefinitions").filter("department").build();
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder()
				.moduleName("pgr").masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
		
		try {
			log.info("Request: "+mapper.writeValueAsString(mdmsCriteriaReq));
			response = restTemplate.postForObject(uri.toString(), mdmsCriteriaReq, Map.class);
		}catch(HttpClientErrorException e) {
			log.error("Searcher threw an Exception: ",e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		}catch(Exception e) {
			log.error("Exception while fetching from searcher: ",e);
		}
		
		return response;		
	}

}
