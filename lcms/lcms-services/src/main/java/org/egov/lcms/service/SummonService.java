package org.egov.lcms.service;

import java.util.ArrayList;
import java.util.List;
import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.Case;
import org.egov.lcms.models.CaseRequest;
import org.egov.lcms.models.CaseResponse;
import org.egov.lcms.models.CaseSearchCriteria;
import org.egov.lcms.models.SummonRequest;
import org.egov.lcms.models.SummonResponse;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SummonService {

	@Autowired
	ResponseFactory responseInfoFactory;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * This API will create the summon
	 * 
	 * @param summonRequest
	 * @return {@link SummonResponse}
	 */
	public SummonResponse createSummon(SummonRequest summonRequest) {

		// kafkaTemplate.send(propertiesManager.getCreateSummonvalidated(),
		// summonRequest);
		return new SummonResponse(
				responseInfoFactory.getResponseInfo(summonRequest.getRequestInfo(), HttpStatus.CREATED),
				summonRequest.getSummons());

	}

	/**
	 * This API will create the case
	 * 
	 * @param caseRequest
	 * @return {@link CaseResponse}
	 */
	public CaseResponse createCase(CaseRequest caseRequest) {
		return new CaseResponse(responseInfoFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	public CaseResponse assignAdvocate(CaseRequest caseRequest) {
		return new CaseResponse(responseInfoFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	public CaseResponse caseSearch(CaseSearchCriteria caseSearchCriteria, RequestInfo requestInfo) {
		Case casse = new Case();
		List<Case> cases = new ArrayList<Case>();
		cases.add(casse);
		return new CaseResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), cases);

	}

	public CaseResponse createVakalatnama(CaseRequest caseRequest) {
		return new CaseResponse(responseInfoFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	public CaseResponse createParaWiseComment(CaseRequest caseRequest) {
		return new CaseResponse(responseInfoFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	public CaseResponse updateParaWiseComment(CaseRequest caseRequest) {
		return new CaseResponse(responseInfoFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	public CaseResponse createHearingDetails(CaseRequest caseRequest) {
		return new CaseResponse(responseInfoFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	public CaseResponse updateHearingDetails(CaseRequest caseRequest) {
		return new CaseResponse(responseInfoFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

}
