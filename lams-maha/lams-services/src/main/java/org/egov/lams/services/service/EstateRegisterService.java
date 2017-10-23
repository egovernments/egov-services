package org.egov.lams.services.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.util.SequenceGenUtil;
import org.egov.lams.common.web.contract.EstateRegister;
import org.egov.lams.common.web.contract.factory.ResponseFactory;
import org.egov.lams.common.web.request.EstateRegisterRequest;
import org.egov.lams.common.web.request.EstateRegisterResponse;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EstateRegisterService {

	@Autowired
	private ResponseFactory responseInfoFactory;
	
	@Autowired
	private SequenceGenUtil sequenceGenService;
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	public EstateRegisterResponse createAsync(EstateRegisterRequest estateRegisterRequest){
		
		List<String> ids=sequenceGenService.getIds(estateRegisterRequest.getLandRegisters().size(), "");
		int index=0;
		for(EstateRegister estateRegister: estateRegisterRequest.getLandRegisters()){
			estateRegister.setId(ids.get(index++));
		}
		
		return null;
	}
	
	private EstateRegisterResponse getEstateRegisterResponse(List<EstateRegister> estateRegister, RequestInfo requestInfo) {
		EstateRegisterResponse estateRegisterResponse = new EstateRegisterResponse();
		estateRegisterResponse.setLandRegisters(estateRegister);
		estateRegisterResponse.setResponseInfo(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK));
		return estateRegisterResponse;
	}
}
