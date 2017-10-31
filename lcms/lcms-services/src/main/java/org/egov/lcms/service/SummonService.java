package org.egov.lcms.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.AdvocateDetails;
import org.egov.lcms.models.Case;
import org.egov.lcms.models.CaseRequest;
import org.egov.lcms.models.CaseResponse;
import org.egov.lcms.models.CaseSearchCriteria;
import org.egov.lcms.models.Summon;
import org.egov.lcms.models.SummonRequest;
import org.egov.lcms.models.SummonResponse;
import org.egov.lcms.repository.CaseSearchRepository;
import org.egov.lcms.repository.IdGenerationRepository;
import org.egov.lcms.util.UniqueCodeGeneration;
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
	UniqueCodeGeneration uniqueCodeGeneration;

	@Autowired
	IdGenerationRepository idGenerationRepository;

	@Autowired
	CaseSearchRepository caseSearchRepository;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * This API will create the summon
	 * 
	 * @param summonRequest
	 * @return {@link SummonResponse}
	 */
	public SummonResponse createSummon(SummonRequest summonRequest) throws Exception {

		for(Summon Summon: summonRequest.getSummons()){
			if(Summon.getIsUlbinitiated() == null){
				Summon.setIsUlbinitiated(Boolean.FALSE);
			}
		}
		generateSummonReferenceNumber(summonRequest);

		kafkaTemplate.send(propertiesManager.getCreateSummonvalidated(), summonRequest);
		return new SummonResponse(
				responseInfoFactory.getResponseInfo(summonRequest.getRequestInfo(), HttpStatus.CREATED),
				summonRequest.getSummons());

	}

	
	private void generateSummonReferenceNumber(SummonRequest summonRequest) throws Exception {

		for (Summon summon : summonRequest.getSummons()) {

			String code = uniqueCodeGeneration.getUniqueCode(summon.getTenantId(), summonRequest.getRequestInfo(),
					propertiesManager.getSummonCodeFormat(), propertiesManager.getSummonName(), Boolean.FALSE, null);

			String summonRefrence = uniqueCodeGeneration.getUniqueCode(summon.getTenantId(),
					summonRequest.getRequestInfo(), propertiesManager.getSummonRefrenceFormat(),
					propertiesManager.getSummonReferenceGenName(), Boolean.FALSE, null);

			summon.setSummonReferenceNo(summonRefrence);
			summon.setCode(code);

		}

	}


	

	public CaseResponse assignAdvocate(CaseRequest caseRequest) throws Exception {

		generateAdvocateCode(caseRequest);
		kafkaTemplate.send(propertiesManager.getAssignAdvocate(), caseRequest);
		return new CaseResponse(responseInfoFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	private void generateAdvocateCode(CaseRequest caseRequest) throws Exception {
		List<Case> cases = caseRequest.getCases();
		for ( Case caseObj : cases){
			for ( AdvocateDetails advocatedetail : caseObj.getAdvocatesDetails()){
				String advocateCode = uniqueCodeGeneration.getUniqueCode(caseObj.getTenantId(), caseRequest.getRequestInfo(), propertiesManager.getAdvocateDetailsCodeFormat(), propertiesManager.getAdvocateDetailsCodeName(), Boolean.FALSE, null);
				advocatedetail.setCode(advocateCode);
			}
		}
		
	}

	public CaseResponse caseSearch(CaseSearchCriteria caseSearchCriteria, RequestInfo requestInfo) {

		List<Case> cases = caseSearchRepository.searchCases(caseSearchCriteria);

		return new CaseResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), cases);

	}

	public CaseResponse createVakalatnama(CaseRequest caseRequest) {
		validateVakalatName(caseRequest);
		kafkaTemplate.send(propertiesManager.getCreateVakalatnama(), caseRequest);
		return new CaseResponse(responseInfoFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	private void validateVakalatName(CaseRequest caseRequest) {
		for ( Case caseObj : caseRequest.getCases() ){
			
		}
		
		
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
		generateUniqueCodeForHearing(caseRequest);
		return new CaseResponse(responseInfoFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	private void generateUniqueCodeForHearing(CaseRequest caseRequest) {
		
		
	
		
	}

	public CaseResponse updateHearingDetails(CaseRequest caseRequest) {
		return new CaseResponse(responseInfoFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

}
