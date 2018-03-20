package org.egov.pgr.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.pgr.contract.SearcherRequest;
import org.egov.pgr.contract.ServiceReq;
import org.egov.pgr.contract.ServiceReqResponse;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.producer.PGRProducer;
import org.egov.pgr.repository.IdGenRepo;
import org.egov.pgr.repository.ServiceRequestRepository;
import org.egov.pgr.utils.PGRConstants;
import org.egov.pgr.utils.PGRUtils;
import org.egov.pgr.utils.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GrievanceService {
	
	
	@Value("${kafka.topics.save.servicereq}")
	private String saveTopic;

	@Value("${kafka.topics.update.servicereq}")
	private String updateTopic;
	
	@Value("${kafka.topics.notification.complaint}")
	private String complaintTopic;

	@Autowired
	private ResponseInfoFactory factory;

	@Autowired
	private IdGenRepo idGenRepo;

	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
		
	@Autowired
	private PGRUtils pGRUtils;
	
	@Autowired
	private PGRProducer pGRProducer;

	
	
	/**
	 * Method to return service requests received from the repo to the controller in
	 * the reqd format
	 * 
	 * @param requestInfo
	 * @param serviceReqSearchCriteria
	 * @return ServiceReqResponse
	 * @author vishal
	 */
	public Object getServiceRequests(RequestInfo requestInfo,
			ServiceReqSearchCriteria serviceReqSearchCriteria) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		//ServiceReqResponse serviceReqResponse = null;
		StringBuilder uri = new StringBuilder();
		SearcherRequest searcherRequest = null;
		if(null != serviceReqSearchCriteria.getServiceRequestId() && serviceReqSearchCriteria.getServiceRequestId().size() == 1) {
			searcherRequest = pGRUtils.prepareSearchRequestSpecific(uri, serviceReqSearchCriteria, requestInfo);
		}else if(null != serviceReqSearchCriteria.getAssignedTo() && !serviceReqSearchCriteria.getAssignedTo().isEmpty()) {
			searcherRequest = pGRUtils.prepareSearchRequestAssignedTo(uri, serviceReqSearchCriteria, requestInfo);
		}else {
			if(null != serviceReqSearchCriteria.getGroup() && !serviceReqSearchCriteria.getGroup().isEmpty()){
					Object response = fetchServiceCodes(requestInfo, serviceReqSearchCriteria.getTenantId(), serviceReqSearchCriteria.getGroup());
					List<String> serviceCodes = null;
					if(null == response)
						return new ServiceReqResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false),
								new ArrayList<ServiceReq>());
					try {
						serviceCodes = (List<String>) JsonPath.read(response, PGRConstants.JSONPATH_SERVICE_CODES);
					}catch(Exception e) {
						return new ServiceReqResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false),
								new ArrayList<ServiceReq>());
					}
					serviceReqSearchCriteria.setServiceCodes(serviceCodes);
			}
			searcherRequest = pGRUtils.prepareSearchRequestGeneral(uri, serviceReqSearchCriteria, requestInfo);
		}
		Object response = serviceRequestRepository.fetchResult(uri, searcherRequest);
		log.info("Searcher response: ", response);
		if (null == response) {
			return new ServiceReqResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false),
					new ArrayList<ServiceReq>());
		}
		//serviceReqResponse = mapper.convertValue(response, ServiceReqResponse.class);
		return response;
	}
	
	/**
	 * method to fetch service codes from mdms based on dept
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param department
	 * @return Object
	 * @author vishal
	 */
	public Object fetchServiceCodes(RequestInfo requestInfo,
			String tenantId, String department) {
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq mdmsCriteriaReq = pGRUtils.prepareSearchRequestForServiceCodes(uri, tenantId, department, requestInfo);
		return serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
		
	}

}
