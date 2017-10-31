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
import org.egov.tracer.model.CustomException;
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
         validateSummon(summonRequest);
		for (Summon Summon : summonRequest.getSummons()) {
			if (Summon.getIsUlbinitiated() == null) {
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

	/**
	 * This will assign the advocate for the case
	 * @param caseRequest
	 * @return {@link CaseResponse}
	 * @throws Exception
	 */
	public CaseResponse assignAdvocate(CaseRequest caseRequest) throws Exception {

		validateAssignAdvocate(caseRequest);
		generateAdvocateCode(caseRequest);
		kafkaTemplate.send(propertiesManager.getAssignAdvocate(), caseRequest);
		return new CaseResponse(responseInfoFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	/**
	 * This API will generate the advocate code
	 * @param caseRequest
	 * @throws Exception
	 */
	private void generateAdvocateCode(CaseRequest caseRequest) throws Exception {
		List<Case> cases = caseRequest.getCases();

		for (Case caseObj : cases) {
			for (AdvocateDetails advocatedetail : caseObj.getAdvocateDetails()) {
				String advocateCode = uniqueCodeGeneration.getUniqueCode(caseObj.getTenantId(),
						caseRequest.getRequestInfo(), propertiesManager.getAdvocateDetailsCodeFormat(),
						propertiesManager.getAdvocateDetailsCodeName(), Boolean.FALSE, null);

	

				advocatedetail.setCode(advocateCode);
			}
		}

	}

	/**
	 * This will search the cases based on the given parameters
	 * @param caseSearchCriteria
	 * @param requestInfo
	 * @return {@link CaseResponse}
	 */
	public CaseResponse caseSearch(CaseSearchCriteria caseSearchCriteria, RequestInfo requestInfo) {

		List<Case> cases = caseSearchRepository.searchCases(caseSearchCriteria);

		return new CaseResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), cases);

	}

	/**
	 * * This API will validate the summon object
	 * 
	 * @param summonRequest
	 * @throws Exception
	 */
	private void validateSummon(SummonRequest summonRequest) throws Exception {
		for (Summon summon : summonRequest.getSummons()) {
			if (summon.getTenantId() == null) {
				throw new CustomException(propertiesManager.getTenantMandatoryCode(),
						propertiesManager.getTenantMandatoryMessage());
			} else if (summon.getSummonReferenceNo() == null || summon.getSummonReferenceNo().isEmpty()) {
				throw new CustomException(propertiesManager.getRefrencenoCode(),
						propertiesManager.getRefrencenoMessage());
			} else if (summon.getYear() == null) {
				throw new CustomException(propertiesManager.getYearCode(), propertiesManager.getYearMessage());
			} else if (summon.getPlantiffName() == null) {
				throw new CustomException(propertiesManager.getPlaintiffCode(),
						propertiesManager.getPlaintiffMessage());

			} else if (summon.getPlantiffAddress() == null) {
				throw new CustomException(propertiesManager.getPlaintiffaddressCode(),
						propertiesManager.getPlaintiffaddressMessage());
			} else if (summon.getDefendant() == null || summon.getDefendant().isEmpty()) {
				throw new CustomException(propertiesManager.getDefendentCode(),
						propertiesManager.getDefendentMessage());

			} else if (summon.getCourtName() == null) {
				throw new CustomException(propertiesManager.getCourtnameCode(),
						propertiesManager.getCourtnameMessage());

			} else if (summon.getWard() == null || summon.getWard().isEmpty()) {
				throw new CustomException(propertiesManager.getWardCode(), propertiesManager.getWardMessage());
			} else if (summon.getBench() == null) {
				throw new CustomException(propertiesManager.getBenchCode(), propertiesManager.getBenchMessage());
			} else if (summon.getStamp() == null) {
				throw new CustomException(propertiesManager.getStampCode(), propertiesManager.getStampMessage());
			} else if (summon.getSummonDate() == null) {
				throw new CustomException(propertiesManager.getSummondateCode(),
						propertiesManager.getSummondateMessage());
			} else if (summon.getCaseType() == null) {
				throw new CustomException(propertiesManager.getCasetypeCode(), propertiesManager.getCasetypeMessage());
			} else if (summon.getCaseNo() == null || summon.getCaseNo().isEmpty()) {
				throw new CustomException(propertiesManager.getCasenoCode(), propertiesManager.getCasenoMessage());

			} else if (summon.getCaseDetails() == null) {
				throw new CustomException(propertiesManager.getCasedetailsCode(),
						propertiesManager.getCasedetailsMessage());

			} else if (summon.getDepartmentName() == null) {
				throw new CustomException(propertiesManager.getDepartmentNameCode(),
						propertiesManager.getDepartmentNameMessage());
			} else if (summon.getHearingDate() == null) {
				throw new CustomException(propertiesManager.getHearingdateCode(),
						propertiesManager.getHearingdateMessage());
			} else if (summon.getHearingTime() == null) {
				throw new CustomException(propertiesManager.getHearingtimeCode(),
						propertiesManager.getHearingtimeMessage());
			} else if (summon.getSide() == null) {
				throw new CustomException(propertiesManager.getSideCode(), propertiesManager.getSideMessage());
			} else if (summon.getSectionApplied() == null) {
				throw new CustomException(propertiesManager.getSectionappliedCode(),
						propertiesManager.getSectionappliedMessage());
			}
		}
	}

	/**
	 * This API will validate the assign advocate details
	 * @param caseRequest
	 */
	private void validateAssignAdvocate(CaseRequest caseRequest) {

		List<Case> cases = caseRequest.getCases();
		for (Case caseObj : cases) {

			if (caseObj.getAdvocateDetails() == null || caseObj.getAdvocateDetails().size() <= 0) {
				throw new CustomException(propertiesManager.getAdvocateDetailsMandatorycode(),
						propertiesManager.getAdvocateDetailsMandatoryMessage());
			}

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
		}

	}

}
