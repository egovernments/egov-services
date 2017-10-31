package org.egov.lcms.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.AdvocateDetails;
import org.egov.lcms.models.Case;
import org.egov.lcms.models.CaseRequest;
import org.egov.lcms.models.CaseResponse;
import org.egov.lcms.models.CaseSearchCriteria;
import org.egov.lcms.models.HearingDetails;
import org.egov.lcms.models.ParaWiseComment;
import org.egov.lcms.repository.CaseSearchRepository;
import org.egov.lcms.repository.OpinionRepository;
import org.egov.lcms.util.UniqueCodeGeneration;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CaseService {

	@Autowired
	LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	ResponseFactory responseFactory;

	@Autowired
	UniqueCodeGeneration uniqueCodeGeneration;

	@Autowired
	OpinionRepository opinionRepository;

	@Autowired
	CaseSearchRepository caseSearchRepository;

	public CaseResponse createParaWiseComment(CaseRequest caseRequest) throws Exception {
		List<Case> cases = caseRequest.getCases();
		RequestInfo requestInfo = caseRequest.getRequestInfo();
		for (Case casee : cases) {
			if (casee.getParawiseComments() != null && casee.getParawiseComments().size() > 0) {
				for (ParaWiseComment parawiseComment : casee.getParawiseComments()) {
					String code = uniqueCodeGeneration.getUniqueCode(parawiseComment.getTenantId(), requestInfo,
							propertiesManager.getParaWiseCommentsUlbFormat(),
							propertiesManager.getParaWiseCommentsUlbName(), Boolean.FALSE, null);
					parawiseComment.setCode(code);
				}
			}
		}

		kafkaTemplate.send(propertiesManager.getParaWiseCreateValidated(), caseRequest);
		return getResponseInfo(caseRequest);
	}

	public CaseResponse updateParaWiseComment(CaseRequest caseRequest) {
		kafkaTemplate.send(propertiesManager.getParaWiseUpdateValidated(), caseRequest);
		return getResponseInfo(caseRequest);
	}

	private CaseResponse getResponseInfo(CaseRequest caseRequest) {
		ResponseInfo responseInfo = responseFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED);
		CaseResponse opinionResponse = new CaseResponse();

		opinionResponse.setResponseInfo(responseInfo);
		opinionResponse.setCases(caseRequest.getCases());
		return opinionResponse;
	}

	/**
	 * This API will create the case
	 * 
	 * @param caseRequest
	 * @return {@link CaseResponse}
	 */
	public CaseResponse createCase(CaseRequest caseRequest) throws Exception {

		validateCase(caseRequest);
		generateCaseReferenceNumber(caseRequest);
		kafkaTemplate.send(propertiesManager.getUpdateSummonValidate(), caseRequest);
		return new CaseResponse(responseFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	/**
	 * This will validate the case object
	 * 
	 * @param caseRequest
	 * @throws Exception
	 */
	private void validateCase(CaseRequest caseRequest) throws Exception {
		List<Case> cases = caseRequest.getCases();

		for (Case caseObj : cases) {
			if (caseObj.getCaseRegistrationDate() == null) {
				throw new CustomException(propertiesManager.getRequiredCaseGenerationCode(),
						propertiesManager.getRequiedCaseGenerationDateMessage());
			}

			else if (caseObj.getDepartmentPerson() == null || !caseObj.getDepartmentPerson().isEmpty()) {
				throw new CustomException(propertiesManager.getRequiredDepartmentPersonCode(),
						propertiesManager.getRequiredDepartmentPersonCode());
			}

		}
	}

	/**
	 * This will generate the case code and case reference number
	 * 
	 * @param caseRequest
	 * @throws Exception
	 */
	private void generateCaseReferenceNumber(CaseRequest caseRequest) throws Exception {
		for (Case caseobj : caseRequest.getCases()) {

			String code = uniqueCodeGeneration.getUniqueCode(caseobj.getTenantId(), caseRequest.getRequestInfo(),
					propertiesManager.getCaseCodeFormat(), propertiesManager.getCaseCodeName(), Boolean.FALSE, null);
			String caseReferenceNumber = uniqueCodeGeneration.getUniqueCode(caseobj.getTenantId(),
					caseRequest.getRequestInfo(), propertiesManager.getCaseReferenceFormat(),
					propertiesManager.getCaseReferenceGenName(), Boolean.TRUE,
					caseobj.getSummon().getCaseType().getCode());
			caseobj.setCode(caseReferenceNumber);
			caseobj.setCode(code);

		}

	}

	/**
	 * This will search the cases based on the given parameters
	 * 
	 * @param caseSearchCriteria
	 * @param requestInfo
	 * @return {@link CaseResponse}
	 */
	public CaseResponse caseSearch(CaseSearchCriteria caseSearchCriteria, RequestInfo requestInfo) {

		List<Case> cases = caseSearchRepository.searchCases(caseSearchCriteria);

		return new CaseResponse(responseFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), cases);

	}

	/**
	 * This will create the vakalatnama for the case
	 * 
	 * @param caseRequest
	 * @return
	 */
	public CaseResponse createVakalatnama(CaseRequest caseRequest) throws Exception {
		validateVakalatnama(caseRequest);
		kafkaTemplate.send(propertiesManager.getCreateVakalatnama(), caseRequest);
		return new CaseResponse(responseFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	/**
	 * This API will load legacy
	 * 
	 * @param caseRequest
	 * @return {@link CaseResponse}
	 */
	public CaseResponse legacyDataLoad(CaseRequest caseRequest) throws Exception {

		for (Case caseObj : caseRequest.getCases()) {

			if (caseObj.getSummon().getIsUlbinitiated() == null) {
				caseObj.getSummon().setIsUlbinitiated(Boolean.FALSE);
			}

			if (caseObj.getSummon().getIsSummon() == null) {
				caseObj.getSummon().setIsSummon(Boolean.FALSE);
			}

			if (caseObj.getHearingDetails() != null) {
				for (HearingDetails hearingDetail : caseObj.getHearingDetails()) {

					String hearingcode = uniqueCodeGeneration.getUniqueCode(caseObj.getTenantId(),
							caseRequest.getRequestInfo(), propertiesManager.getHearingDetailsUlbFormat(),
							propertiesManager.getHearingDetailsUlbName(), Boolean.FALSE, null);
					hearingDetail.setCode(hearingcode);

				}
			}

			String summonCode = uniqueCodeGeneration.getUniqueCode(caseObj.getTenantId(), caseRequest.getRequestInfo(),
					propertiesManager.getSummonCodeFormat(), propertiesManager.getSummonName(), Boolean.FALSE, null);
			caseObj.setCode(summonCode);

			String summonRefrence = uniqueCodeGeneration.getUniqueCode(caseObj.getSummon().getTenantId(),
					caseRequest.getRequestInfo(), propertiesManager.getSummonRefrenceFormat(),
					propertiesManager.getSummonReferenceGenName(), Boolean.FALSE, null);

			caseObj.getSummon().setSummonReferenceNo(summonRefrence);
			caseObj.getSummon().setCode(summonCode);

			if (caseObj.getCaseVoucher() != null) {
				String caseVoucherCode = uniqueCodeGeneration.getUniqueCode(caseObj.getSummon().getTenantId(),
						caseRequest.getRequestInfo(), propertiesManager.getVoucherCodeFormat(),
						propertiesManager.getVoucherCodeFormatName(), Boolean.FALSE, null);
				caseObj.getCaseVoucher().setCode(caseVoucherCode);
				caseObj.getCaseVoucher().setCaseCode(summonCode);
			}

			String caseReferenceNumber = uniqueCodeGeneration.getUniqueCode(caseObj.getTenantId(),
					caseRequest.getRequestInfo(), propertiesManager.getCaseReferenceFormat(),
					propertiesManager.getCaseReferenceGenName(), Boolean.TRUE,
					caseObj.getSummon().getCaseType().getCode());

			caseObj.setCaseRefernceNo(caseReferenceNumber);
			if (caseObj.getAdvocatesDetails() != null) {
				for (AdvocateDetails advocatedetail : caseObj.getAdvocatesDetails()) {
					String advocateCode = uniqueCodeGeneration.getUniqueCode(caseObj.getTenantId(),
							caseRequest.getRequestInfo(), propertiesManager.getAdvocateDetailsCodeFormat(),
							propertiesManager.getAdvocateDetailsCodeName(), Boolean.FALSE, null);
					advocatedetail.setCode(advocateCode);
				}

			}

		}

		kafkaTemplate.send(propertiesManager.getLoadLegacyData(), caseRequest);

		return new CaseResponse(responseFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	public CaseResponse createHearingDetails(CaseRequest caseRequest) throws Exception {
		List<Case> cases = caseRequest.getCases();
		RequestInfo requestInfo = caseRequest.getRequestInfo();
		for (Case casee : cases) {
			if (casee.getHearingDetails() != null && casee.getHearingDetails().size() > 0) {
				for (HearingDetails hearingDetails : casee.getHearingDetails()) {
					String code = uniqueCodeGeneration.getUniqueCode(hearingDetails.getTenantId(), requestInfo,
							propertiesManager.getHearingDetailsUlbFormat(),
							propertiesManager.getHearingDetailsUlbName(), Boolean.FALSE, null);
					hearingDetails.setCode(code);
				}
			}
		}

		kafkaTemplate.send(propertiesManager.getHearingCreateValidated(), caseRequest);
		return getResponseInfo(caseRequest);
	}

	public CaseResponse updateHearingDetails(CaseRequest caseRequest) {
		kafkaTemplate.send(propertiesManager.getHearingUpdateValidated(), caseRequest);
		return getResponseInfo(caseRequest);
	}

	/**
	 * This APi wil validate the vakalatnama
	 * 
	 * @param caseRequest
	 * @throws Exception
	 */
	private void validateVakalatnama(CaseRequest caseRequest) throws Exception {
		for (Case caseObj : caseRequest.getCases()) {
			if (caseObj.getTenantId() == null) {
				throw new CustomException(propertiesManager.getTenantMandatoryCode(),
						propertiesManager.getTenantMandatoryMessage());
			} else if (caseObj.getIsVakalatnamaGenerated() == null) {
				throw new CustomException(propertiesManager.getIsVakalatNamaRequiredCode(),
						propertiesManager.getIsVakalatNamaRequiredMessage());

			} else if (caseObj.getSummon().getSummonReferenceNo() == null
					|| caseObj.getSummon().getSummonReferenceNo().isEmpty()) {
				throw new CustomException(propertiesManager.getSummonrefrencenoCode(),
						propertiesManager.getSummonrefrencenoMessage());
			} else if (caseObj.getWitness() == null || caseObj.getWitness().size() <= 0) {
				throw new CustomException(propertiesManager.getWitnessCode(), propertiesManager.getWitnessMessage());
			} else if (caseObj.getDepartmentPerson() == null || caseObj.getDepartmentPerson().isEmpty()) {
				throw new CustomException(propertiesManager.getDepartmenteCode(),
						propertiesManager.getDepartmentMessage());
			} else if (caseObj.getDays() == null) {
				throw new CustomException(propertiesManager.getDaysCode(), propertiesManager.getDaysMessage());
			} else if (caseObj.getCaseRefernceNo() == null || caseObj.getCaseRefernceNo().isEmpty()) {
				throw new CustomException(propertiesManager.getRefrencecasenoCode(),
						propertiesManager.getRefrencecasenoMessage());
			}

			for (AdvocateDetails advocateDetails : caseObj.getAdvocatesDetails()) {

				if (advocateDetails.getAdvocate().getName() == null
						|| advocateDetails.getAdvocate().getName().isEmpty())
					throw new CustomException(propertiesManager.getAdvocatenameCode(),
							propertiesManager.getAdvocatenameMessage());

			}

		}

	}

}