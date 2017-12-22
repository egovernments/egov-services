package org.egov.lcms.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.enums.EntryType;
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
import org.egov.lcms.repository.SummonRepository;
import org.egov.lcms.util.UniqueCodeGeneration;
import org.egov.lcms.util.SummonValidator;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/** 
* 
* Author		Date			eGov-JIRA ticket	Commit message
* ---------------------------------------------------------------------------
* Prasad		26th Oct 2017						Initial commit for Summon service 
* Shubham		30th Oct 2017						Added lcmsLegacyLoad code
* Narendra		01st Nov 2017						Added summon validator class for summon related validations
* Prasad		20th Nov 2017						Added case status search for searchResult level false case
* Narendra		29th Nov 2017						Fixed duplicate advocates issue
* Yosadhara		01st Nov 2017						Summon update API implementation
*/
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

	@Autowired
	private SummonValidator summonValidator;

	@Autowired
	private SummonRepository summonRepository;

	/**
	 * This method is to create Summon
	 * 
	 * @param summonRequest
	 * @return SummonResponse
	 * @throws Exception
	 */
	public SummonResponse createSummon(SummonRequest summonRequest) throws Exception {

		for (Summon summon : summonRequest.getSummons()) {

			if (summon.getIsUlbinitiated() == null) {
				summon.setIsUlbinitiated(Boolean.FALSE);
			}

			if (summon.getIsSummon()) {
				summon.setEntryType(EntryType.fromValue(propertiesManager.getSummonType()));
			} else {
				summon.setEntryType(EntryType.fromValue(propertiesManager.getWarrantType()));
			}
		}
		generateSummonReferenceNumber(summonRequest);

		kafkaTemplate.send(propertiesManager.getCreateSummonvalidated(), summonRequest);

		pushSummonToIndexer(summonRequest);
		return new SummonResponse(
				responseInfoFactory.getResponseInfo(summonRequest.getRequestInfo(), HttpStatus.CREATED),
				summonRequest.getSummons());

	}

	/**
	 * This method is to update Summon
	 * 
	 * @param caseRequest
	 * @return CaseResponse
	 * @throws Exception
	 */
	public CaseResponse updateSummon(CaseRequest caseRequest) throws Exception {
		for (Case caseObj : caseRequest.getCases()) {

			SummonRequest summonRequest = new SummonRequest();

			if (caseObj.getSummon().getIsUlbinitiated() == null) {
				caseObj.getSummon().setIsUlbinitiated(Boolean.FALSE);
			}

			if (caseObj.getSummon().getIsSummon()) {
				caseObj.getSummon().setEntryType(EntryType.fromValue(propertiesManager.getSummonType()));
			} else {
				caseObj.getSummon().setEntryType(EntryType.fromValue(propertiesManager.getWarrantType()));
			}
			List<Summon> summons = new ArrayList<Summon>();
			summons.add(caseObj.getSummon());
			summonRequest.setSummons(summons);
			summonRequest.setRequestInfo(caseRequest.getRequestInfo());
			kafkaTemplate.send(propertiesManager.getUpdateSummonvalidated(), summonRequest);

			pushSummonToIndexer(summonRequest);
		}
		return new CaseResponse(responseInfoFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());

	}
	
	/**
	 * This method is to push Summon to indexer 
	 * 
	 * @param summonRequest
	 */
	private void pushSummonToIndexer(SummonRequest summonRequest) {

		for (Summon summon : summonRequest.getSummons()) {
			Case caseobj = new Case();
			caseobj.setCode(summon.getCode());
			caseobj.setSummon(summon);
			kafkaTemplate.send(propertiesManager.getPushSummonCreateToIndexer(), summonRequest);
		}

	}
	
	/**
	 * This method is to generate Summon Reference number 
	 * 
	 * @param summonRequest
	 * @throws Exception
	 */
	private void generateSummonReferenceNumber(SummonRequest summonRequest) throws Exception {

		for (Summon summon : summonRequest.getSummons()) {

			String code = uniqueCodeGeneration.getUniqueCode(summon.getTenantId(), summonRequest.getRequestInfo(),
					propertiesManager.getSummonCodeFormat(), propertiesManager.getSummonName(), Boolean.FALSE, null,
					Boolean.FALSE);

			String summonRefrence = uniqueCodeGeneration.getUniqueCode(summon.getTenantId(),
					summonRequest.getRequestInfo(), propertiesManager.getSummonRefrenceFormat(),
					propertiesManager.getSummonReferenceGenName(), Boolean.FALSE, null, Boolean.TRUE);

			summon.setSummonReferenceNo(summonRefrence);
			summon.setCode(code);

		}

	}

	/**
	 * This method is to assign advocate for the case
	 * 
	 * @param caseRequest
	 * @return CaseResponse
	 * @throws Exception
	 */
	public CaseResponse assignAdvocate(CaseRequest caseRequest) throws Exception {
		summonValidator.validateSummon(caseRequest);
		validateAssignAdvocate(caseRequest);
		updateAdvocateDetails(caseRequest);
		return new CaseResponse(responseInfoFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	/**
	 * This method is to create or update advocate for the case
	 * 
	 * @param caseRequest
	 * @throws Exception
	 */
	private void updateAdvocateDetails(CaseRequest caseRequest) throws Exception {

		for (Case caseObj : caseRequest.getCases()) {
			List<AdvocateDetails> insertAdvocate = new ArrayList<AdvocateDetails>();
			List<AdvocateDetails> updateAdvocate = new ArrayList<AdvocateDetails>();

			List<String> advocateCodes = summonRepository.getAdvocateCodes(caseObj);

			List<AdvocateDetails> advocateDetails = caseObj.getAdvocateDetails();

			for (AdvocateDetails advocateDetail : advocateDetails) {

				if (advocateDetail.getCode() == null) {

					String advocateCode = uniqueCodeGeneration.getUniqueCode(caseObj.getTenantId(),
							caseRequest.getRequestInfo(), propertiesManager.getAdvocateDetailsCodeFormat(),
							propertiesManager.getAdvocateDetailsCodeName(), Boolean.FALSE, null, Boolean.FALSE);

					advocateDetail.setCode(advocateCode);
					insertAdvocate.add(advocateDetail);
				}

				else {
					if (advocateCodes.contains(advocateDetail.getCode().trim())) {

						updateAdvocate.add(advocateDetail);

						advocateCodes.remove(advocateDetail.getCode().trim());
					}
				}

			}

			if (insertAdvocate.size() > 0) {
				caseObj.setAdvocateDetails(insertAdvocate);
				kafkaTemplate.send(propertiesManager.getAssignAdvocate(), caseRequest);
			}

			if (updateAdvocate.size() > 0) {
				caseObj.setAdvocateDetails(updateAdvocate);
				kafkaTemplate.send(propertiesManager.getUpdateAssignAdvocate(), caseRequest);
			}

			if (advocateCodes.size() > 0)
				summonRepository.deleteAdvocateDetails(advocateCodes);
		}
	}

	/**
	 * This method is to generate advocate code
	 * 
	 * @param caseRequest
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private void generateAdvocateCode(CaseRequest caseRequest) throws Exception {
		List<Case> cases = caseRequest.getCases();

		for (Case caseObj : cases) {
			caseObj.setCode(caseObj.getSummon().getCode());
			for (AdvocateDetails advocatedetail : caseObj.getAdvocateDetails()) {
				String advocateCode = uniqueCodeGeneration.getUniqueCode(caseObj.getTenantId(),
						caseRequest.getRequestInfo(), propertiesManager.getAdvocateDetailsCodeFormat(),
						propertiesManager.getAdvocateDetailsCodeName(), Boolean.FALSE, null, Boolean.FALSE);

				advocatedetail.setCode(advocateCode);
			}
		}

	}

	/**
	 * This method is to search cases based on case search criterias
	 *  
	 * @param caseSearchCriteria
	 * @param requestInfo
	 * @return {@link CaseResponse}
	 * @throws Exception
	 */
	public CaseResponse caseSearch(CaseSearchCriteria caseSearchCriteria, RequestInfo requestInfo) throws Exception {

		List<Case> cases = caseSearchRepository.searchCases(caseSearchCriteria, requestInfo);

		return new CaseResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), cases);

	}

	/**
	 * This method is to validate assign advocate details
	 * 
	 * @param caseRequest
	 */
	private void validateAssignAdvocate(CaseRequest caseRequest) {

		List<Case> cases = caseRequest.getCases();
		for (Case caseObj : cases) {

			if (caseObj.getAdvocateDetails() == null || caseObj.getAdvocateDetails().size() <= 0) {
				throw new CustomException(propertiesManager.getAdvocateDetailsMandatorycode(),
						propertiesManager.getAdvocateDetailsMandatoryMessage());
			}

			summonValidator.validateDuplicateAdvocates(caseObj);

			for (AdvocateDetails advocateDetails : caseObj.getAdvocateDetails()) {

				if (advocateDetails.getAdvocate() == null) {

					throw new CustomException(propertiesManager.getAdvocateMandatoryCode(),
							propertiesManager.getAdvocateMandatoryMessage());
				}

				if (advocateDetails.getAssignedDate() == null) {

					throw new CustomException(propertiesManager.getAdvocateAssignDateCode(),
							propertiesManager.getAdvocateAssignDateMessage());
				}

			}

			if (caseObj.getAdvocateDetails() == null || caseObj.getAdvocateDetails().size() <= 0) {

				throw new CustomException(propertiesManager.getAdvocateDetailsSize(),
						propertiesManager.getAdvocateDetailsSizeMessage());

			}
		}

	}

}
