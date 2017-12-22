package org.egov.lcms.service;

import java.util.List;


import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.AdvocatePayment;
import org.egov.lcms.models.AdvocatePaymentRequest;
import org.egov.lcms.models.AdvocatePaymentResponse;
import org.egov.lcms.models.AdvocatePaymentSearchCriteria;
import org.egov.lcms.repository.AdvocatePaymentRepository;
import org.egov.lcms.util.UniqueCodeGeneration;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/** 
* 
* Author		Date			eGov-JIRA ticket	Commit message
* ---------------------------------------------------------------------------
* Shubham		28th Oct 2017						Initial commit for AdvocatePayment service 
* Shubham		03rd Nov 2017						Added requestInfo and throws statement for advocatePayment search
* Shubham		06th Nov 2017						Added null validation for partialPayment property
* Prasad 		08th Nov 2017						Added isSummon parameter to getUniqueCode method
*/
@Service
public class AdvocatePaymentService {
	
	@Autowired
	ResponseFactory responseInfoFactory;
	
	@Autowired
	PropertiesManager propertiesManager;
	
	@Autowired
	UniqueCodeGeneration uniqueCodeGeneration;
	
	@Autowired
	AdvocatePaymentRepository advocatePaymentRepository;
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	/**
	 * This method is to create AdvocatePayment
	 * 
	 * @param advocatePaymentRequest
	 * @return AdvocatePaymentResponse
	 * @throws Exception
	 */
	public AdvocatePaymentResponse createAdvocatePayment(AdvocatePaymentRequest advocatePaymentRequest) throws Exception {
		
		for(AdvocatePayment advocatePayment : advocatePaymentRequest.getAdvocatePayments()){
			if(advocatePayment.getIsPartialPayment() == null){
				advocatePayment.setIsPartialPayment(Boolean.FALSE);
			}
			String code = uniqueCodeGeneration.getUniqueCode(advocatePayment.getTenantId(), advocatePaymentRequest.getRequestInfo(),
					propertiesManager.getAdvocatePaymentUlbFormat(), propertiesManager.getAdvocatePaymentUlbName(),Boolean.FALSE,null,Boolean.FALSE);
			       advocatePayment.setCode(code);
		}

		kafkaTemplate.send(propertiesManager.getAdvocatePaymentCreate(), advocatePaymentRequest);
		
		return new AdvocatePaymentResponse(responseInfoFactory.getResponseInfo(advocatePaymentRequest.getRequestInfo(), HttpStatus.CREATED), advocatePaymentRequest.getAdvocatePayments());
	}
	
	/**
	 * This method is to update AdvocatePayment
	 * 
	 * @param advocatePaymentRequest
	 * @return AdvocatePaymentResponse
	 */
	public AdvocatePaymentResponse updateAdvocatePayment(AdvocatePaymentRequest advocatePaymentRequest) {
		
		for(AdvocatePayment advocatePayment : advocatePaymentRequest.getAdvocatePayments()){
			if(advocatePayment.getIsPartialPayment() == null){
				advocatePayment.setIsPartialPayment(Boolean.FALSE);
			}
		
		}
		kafkaTemplate.send(propertiesManager.getAdvocatePaymentUpdate(), advocatePaymentRequest);
		return new AdvocatePaymentResponse(responseInfoFactory.getResponseInfo(advocatePaymentRequest.getRequestInfo(), HttpStatus.CREATED), advocatePaymentRequest.getAdvocatePayments());
	}
	
	/**
	 * This method is to search the AdvocatePayment 
	 * based on AdvocatePayment search criterias
	 * 
	 * @param requestInfo
	 * @param advocatePaymentSearchCriteria
	 * @return AdvocatePaymentResponse
	 * @throws Exception
	 */
	public AdvocatePaymentResponse searchAdvocatePayment(RequestInfo requestInfo,
			AdvocatePaymentSearchCriteria advocatePaymentSearchCriteria) throws Exception {
		
		List<AdvocatePayment> advocatePayments = advocatePaymentRepository.search(advocatePaymentSearchCriteria, requestInfo);
		
		return new AdvocatePaymentResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), advocatePayments);

	}

}
