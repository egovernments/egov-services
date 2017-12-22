package org.egov.lcms.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.IdGenerationResponse;
import org.egov.lcms.models.Opinion;
import org.egov.lcms.models.OpinionRequest;
import org.egov.lcms.models.OpinionResponse;
import org.egov.lcms.models.OpinionSearchCriteria;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.lcms.repository.IdGenerationRepository;
import org.egov.lcms.repository.OpinionRepository;
import org.egov.lcms.util.UniqueCodeGeneration;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/** 
* 
* Author		Date			eGov-JIRA ticket	Commit message
* ---------------------------------------------------------------------------
* Veswanth		26th Oct 2017						Initial commit for Opinion service 
* Veswanth		03rd Nov 2017						Added department and advocateDetails for opinion search
* Veswanth		23rd Nov 2017						Added current hearing date and time in hearing details API's
*/
@Service
public class OpinionService {

	@Autowired
	LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	ResponseFactory responseFactory;

	@Autowired
	OpinionRepository opinionRepository;

	@Autowired
	UniqueCodeGeneration uniqueCodeGeneration;

	@Autowired
	IdGenerationRepository idGenerationRepository;
	
	/**
	 * This method is to create Opinion
	 * 
	 * @param opinionRequest
	 * @return OpinionResponse
	 * @throws Exception
	 */
	public OpinionResponse createOpinion(OpinionRequest opinionRequest) throws Exception {
		List<Opinion> opinions = opinionRequest.getOpinions();
		RequestInfo requestInfo = opinionRequest.getRequestInfo();
		for (Opinion opinion : opinions) {
			String sequenceNo = "";
			IdGenerationResponse idResponse = idGenerationRepository.getIdGeneration(opinion.getTenantId(), requestInfo,
					propertiesManager.getOpinionUlbFormat(), propertiesManager.getOpinionUlbName());
			if (idResponse != null && idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0) {
				sequenceNo = idResponse.getIdResponses().get(0).getId();
			}

			String code = opinion.getDepartmentName().getCode() + sequenceNo;
			opinion.setCode(code);
		}
		kafkaTemplate.send(propertiesManager.getOpinionCreateValidated(), opinionRequest);
		return getResponseInfo(opinionRequest);
	}
	
	/**
	 * This method is to update Opinion
	 * 
	 * @param opinionRequest
	 * @return OpinionResponse
	 */
	public OpinionResponse updateOpinion(OpinionRequest opinionRequest) {
		kafkaTemplate.send(propertiesManager.getOpinionUpdateValidated(), opinionRequest);
		return getResponseInfo(opinionRequest);
	}
	
	/**
	 * This method is to search Opinion based on Opinion search criterias
	 * 
	 * @param requestInfoWrapper
	 * @param opinionRequest
	 * @return OpinionResponse
	 * @throws Exception
	 */
	public OpinionResponse searchOpinion(RequestInfoWrapper requestInfoWrapper, OpinionSearchCriteria opinionRequest)
			throws Exception {
		List<Opinion> opinions = opinionRepository.search(opinionRequest, requestInfoWrapper);
		ResponseInfo responseInfo = responseFactory.getResponseInfo(requestInfoWrapper.getRequestInfo(),
				HttpStatus.CREATED);
		OpinionResponse response = new OpinionResponse(responseInfo, opinions);
		return response;
	}
	
	/**
	 * This method is to get ResponseInfo
	 * 
	 * @param opinionRequest
	 * @return OpinionResponse
	 */
	private OpinionResponse getResponseInfo(OpinionRequest opinionRequest) {
		ResponseInfo responseInfo = responseFactory.getResponseInfo(opinionRequest.getRequestInfo(),
				HttpStatus.CREATED);
		OpinionResponse opinionResponse = new OpinionResponse();

		opinionResponse.setResponseInfo(responseInfo);
		opinionResponse.setOpinions(opinionRequest.getOpinions());
		return opinionResponse;
	}
}