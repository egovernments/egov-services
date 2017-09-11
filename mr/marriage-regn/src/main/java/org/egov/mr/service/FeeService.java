package org.egov.mr.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.Fee;
import org.egov.mr.repository.FeeRepository;
import org.egov.mr.util.SequenceIdGenService;
import org.egov.mr.web.contract.FeeCriteria;
import org.egov.mr.web.contract.FeeRequest;
import org.egov.mr.web.contract.FeeResponse;
import org.egov.mr.web.contract.ResponseInfoFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FeeService {

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private FeeRepository feeRepository;
	
	@Autowired
	private SequenceIdGenService sequenceGenUtil;
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public FeeResponse getFee(FeeCriteria feeCriteria, RequestInfo requestInfo) {
		return getSuccessResponseForSearch(feeRepository.findForCriteria(feeCriteria), requestInfo);
	}

	public FeeResponse createAsync(FeeRequest feeRequest) {

		List<Fee> fees = feeRequest.getFees();
		List<String> ids = sequenceGenUtil.getIds(fees.size(), "egmr_documents_id");

		int index = 0;
		for (Fee fee : fees)
			fee.setId(ids.get(index++));

			kafkaTemplate.send(propertiesManager.getCreateFeeTopicName(), feeRequest);
			
		return getFeeResponse(fees, feeRequest.getRequestInfo());
	}
	
	public FeeResponse updateAsync(FeeRequest feeRequest) {

		try {
			kafkaTemplate.send(propertiesManager.getUpdateFeeTopicName(), feeRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getFeeResponse(feeRequest.getFees(), feeRequest.getRequestInfo());
	}
	
	public void createFee(FeeRequest feeRequest){
		feeRepository.createFee(feeRequest);
	}
	
	public void updateFee(FeeRequest feeRequest){
		feeRepository.updateFee(feeRequest);
	}
	
	private FeeResponse getSuccessResponseForSearch(List<Fee> fees, RequestInfo requestInfo) {
		FeeResponse feeResponse = new FeeResponse();
		feeResponse.setFees(fees);
		log.info("marriageCertList=" + fees);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		feeResponse.setResponseInfo(responseInfo);
		return feeResponse;
	}

	private FeeResponse getFeeResponse(List<Fee> reissuCert, RequestInfo requestInfo) {
		FeeResponse feeResponse = new FeeResponse();
		feeResponse.setFees(reissuCert);
		feeResponse.setResponseInfo(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED));
		return feeResponse;
	}
}
