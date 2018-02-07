package org.egov.lcms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.enums.EntryType;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.AdvocateDetails;
import org.egov.lcms.models.AuditDetails;
import org.egov.lcms.models.Bench;
import org.egov.lcms.models.Case;
import org.egov.lcms.models.CaseCategory;
import org.egov.lcms.models.CaseDetails;
import org.egov.lcms.models.CaseDetailsResponse;
import org.egov.lcms.models.CaseRequest;
import org.egov.lcms.models.CaseResponse;
import org.egov.lcms.models.CaseSearchCriteria;
import org.egov.lcms.models.CaseStatus;
import org.egov.lcms.models.CaseType;
import org.egov.lcms.models.Court;
import org.egov.lcms.models.Department;
import org.egov.lcms.models.Event;
import org.egov.lcms.models.EventResponse;
import org.egov.lcms.models.EventSearchCriteria;
import org.egov.lcms.models.HearingDetails;
import org.egov.lcms.models.ParaWiseComment;
import org.egov.lcms.models.ReferenceEvidence;
import org.egov.lcms.models.Register;
import org.egov.lcms.models.RegisterSearchCriteria;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.lcms.models.Side;
import org.egov.lcms.models.Summon;
import org.egov.lcms.repository.AdvocateRepository;
import org.egov.lcms.repository.CaseSearchRepository;
import org.egov.lcms.repository.MdmsRepository;
import org.egov.lcms.repository.OpinionRepository;
import org.egov.lcms.repository.RegisterRepository;
import org.egov.lcms.util.SummonValidator;
import org.egov.lcms.util.UniqueCodeGeneration;
import org.egov.mdms.model.MdmsResponse;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

/**
 * 
 * Author Date eGov-JIRA ticket Commit message
 * ---------------------------------------------------------------------------
 * Veswanth 28th Oct 2017 Initial commit for case service Shubham 31st Oct 2017
 * Added legacy create API files Veswanth 31st Oct 2017 Hearing details API
 * implementation Prasad 31st Nov 2017 Added validation for caseRegister,
 * assignAdvocate, summon and vakalatnama Narendra 01st Nov 2017 Added summon
 * validator class for case validations Prasad 20th Nov 2017 Added address in
 * courtdetails for case search Veswanth 30th Nov 2017 Added master details
 * fetching for all the cases Narendra 15th Dec 2017 Added entry type in legacy
 * case
 */
@Service
@Slf4j
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

	@Autowired
	private SummonValidator summonValidator;

	@Autowired
	AdvocateRepository advocateRepository;

	@Autowired
	MdmsRepository mdmsRepository;

	@Autowired
	RegisterRepository registerRepository;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * This method is to create Para wise comment
	 * 
	 * @param caseRequest
	 * @return CaseResponse
	 * @throws Exception
	 */
	public CaseResponse createParaWiseComment(CaseRequest caseRequest) throws Exception {
		List<Case> cases = caseRequest.getCases();
		RequestInfo requestInfo = caseRequest.getRequestInfo();
		for (Case casee : cases) {
			if (casee.getParawiseComments() != null && casee.getParawiseComments().size() > 0) {
				for (ParaWiseComment parawiseComment : casee.getParawiseComments()) {
					String code = uniqueCodeGeneration.getUniqueCode(casee.getTenantId(), requestInfo,
							propertiesManager.getParaWiseCommentsUlbFormat(),
							propertiesManager.getParaWiseCommentsUlbName(), Boolean.FALSE, null, Boolean.FALSE);
					parawiseComment.setCode(code);
				}
			}
		}

		kafkaTemplate.send(propertiesManager.getParaWiseCreateValidated(), caseRequest);
		return getResponseInfo(caseRequest);
	}

	/**
	 * This method is to update Para wise comments
	 * 
	 * @param caseRequest
	 * @return CaseResponse
	 */
	public CaseResponse updateParaWiseComment(CaseRequest caseRequest) {
		kafkaTemplate.send(propertiesManager.getParaWiseUpdateValidated(), caseRequest);
		return getResponseInfo(caseRequest);
	}

	/**
	 * This method is to get response info for CaseRequest object
	 * 
	 * @param caseRequest
	 * @return CaseResponse
	 */
	private CaseResponse getResponseInfo(CaseRequest caseRequest) {
		ResponseInfo responseInfo = responseFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED);
		CaseResponse caseResponse = new CaseResponse();

		caseResponse.setResponseInfo(responseInfo);
		caseResponse.setCases(caseRequest.getCases());
		return caseResponse;
	}

	/**
	 * This method is to create Case
	 * 
	 * @param caseRequest
	 * @return {@link CaseResponse}
	 */
	public CaseResponse createCase(CaseRequest caseRequest) throws Exception {
		summonValidator.validateSummon(caseRequest);
		validateCase(caseRequest);
		generateCaseReferenceNumber(caseRequest);
		kafkaTemplate.send(propertiesManager.getUpdateSummonValidate(), caseRequest);
		return new CaseResponse(responseFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	/**
	 * This method is to validate the Case object
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

			else if (caseObj.getDepartmentPerson() == null || caseObj.getDepartmentPerson().isEmpty()) {
				throw new CustomException(propertiesManager.getRequiredDepartmentPersonCode(),
						propertiesManager.getRequiredDepartmentPersonCode());
			}
		}
	}

	/**
	 * This method is to generate the case code and case reference number
	 * 
	 * @param caseRequest
	 * @throws Exception
	 */
	private void generateCaseReferenceNumber(CaseRequest caseRequest) throws Exception {
		for (Case caseobj : caseRequest.getCases()) {

			String caseReferenceNumber = uniqueCodeGeneration.getUniqueCode(caseobj.getTenantId(),
					caseRequest.getRequestInfo(), propertiesManager.getCaseReferenceFormat(),
					propertiesManager.getCaseReferenceGenName(), Boolean.TRUE,
					caseobj.getSummon().getCaseType().getCode(), Boolean.FALSE);
			caseobj.setCaseRefernceNo(caseReferenceNumber);
			caseobj.setCode(caseobj.getSummon().getCode());

		}
	}

	/**
	 * This method is to search the cases based on the given parameters
	 * 
	 * @param caseSearchCriteria
	 * @param requestInfo
	 * @return {@link CaseResponse}
	 */
	public CaseResponse caseSearch(CaseSearchCriteria caseSearchCriteria, RequestInfo requestInfo) throws Exception {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		List<Case> cases = caseSearchRepository.searchCases(caseSearchCriteria, requestInfo);
		if (cases != null && cases.size() > 0) {
			addDepartmentDetails(cases, cases.get(0).getTenantId(), requestInfo);
			addMasterDetails(cases, caseSearchCriteria.getSearchResultLevel(), caseSearchCriteria.getTenantId(),
					requestInfo);
		}

		return new CaseResponse(responseFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), cases);
	}

	/**
	 * This method is to add the master details for the list of case objects
	 * 
	 * @param cases
	 * @param serachResultLevel
	 * @param tenantId
	 * @param requestInfo
	 * @throws Exception
	 */
	private void addMasterDetails(List<Case> cases, String serachResultLevel, String tenantId, RequestInfo requestInfo)
			throws Exception {
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		Map<String, String> masterMap = new HashMap<>();

		setMasterMapValues(masterMap, cases);

		for (Case caseObj : cases) {

			if (caseObj.getSummon() != null) {

				List<String> caseStatusCodes = new ArrayList<String>();
				String caseStatusCode = "";
				if (serachResultLevel != null) {
					for (HearingDetails hearingDetail : caseObj.getHearingDetails()) {
						if (hearingDetail.getCaseStatus() != null)
							caseStatusCodes.add(hearingDetail.getCaseStatus().getCode());
					}

					int count = 1;
					for (String caseStatus : caseStatusCodes) {
						if (count < caseStatusCodes.size())
							caseStatusCode = caseStatusCode + caseStatus + ",";
						else
							caseStatusCode = caseStatusCode + caseStatus;
						count++;

					}

					if (!caseStatusCode.isEmpty() || caseStatusCode.length() > 1) {
						masterMap.put("caseStatus", caseStatusCode);
					}
				}
				getStampDetails(caseObj);
			}
		}

		if (!masterMap.isEmpty()) {
			MdmsResponse mdmsResponse = mdmsRepository.getMasterData(tenantId, masterMap, requestInfoWrapper,
					propertiesManager.getLcmsModuleName());
			Map<String, Map<String, JSONArray>> response = mdmsResponse.getMdmsRes();

			Map<String, JSONArray> mastersmap = response.get(propertiesManager.getLcmsModuleName());

			for (String key : mastersmap.keySet()) {

				String masterName = key;

				addParticularMastervalues(masterName, cases, mastersmap);
			}
		}
	}

	private void setMasterMapValues(Map<String, String> masterMap, List<Case> cases) {

		List<String> caseTypeCodes = cases.stream()
				.filter(caseData -> caseData.getSummon() != null && caseData.getSummon().getCaseType() != null
						&& caseData.getSummon().getCaseType().getCode() != null)
				.map(caseCode -> caseCode.getSummon().getCaseType().getCode()).collect(Collectors.toList());

		List<String> caseCategoryCodes = cases.stream()
				.filter(caseData -> caseData.getSummon() != null && caseData.getSummon().getCaseCategory() != null
						&& caseData.getSummon().getCaseCategory().getCode() != null)
				.map(caseCode -> caseCode.getSummon().getCaseCategory().getCode()).collect(Collectors.toList());

		List<String> courtCodes = cases.stream()
				.filter(caseData -> caseData.getSummon() != null && caseData.getSummon().getCourtName() != null
						&& caseData.getSummon().getCourtName().getCode() != null)
				.map(caseCode -> caseCode.getSummon().getCourtName().getCode()).collect(Collectors.toList());

		List<String> benchCodes = cases.stream()
				.filter(caseData -> caseData.getSummon() != null && caseData.getSummon().getBench() != null
						&& caseData.getSummon().getBench().getCode() != null)
				.map(caseCode -> caseCode.getSummon().getBench().getCode()).collect(Collectors.toList());

		List<String> sideCodes = cases.stream()
				.filter(caseData -> caseData.getSummon() != null && caseData.getSummon().getSide() != null
						&& caseData.getSummon().getSide().getCode() != null)
				.map(caseCode -> caseCode.getSummon().getSide().getCode()).collect(Collectors.toList());

		String caseTypesCode = mdmsRepository
				.getCommaSepratedValues(caseTypeCodes.toArray(new String[caseTypeCodes.size()]));

		String caseCategoriesCodes = mdmsRepository
				.getCommaSepratedValues(caseCategoryCodes.toArray(new String[caseCategoryCodes.size()]));

		String caseCourtsCodes = mdmsRepository
				.getCommaSepratedValues(courtCodes.toArray(new String[courtCodes.size()]));

		String caseBenchesCodes = mdmsRepository
				.getCommaSepratedValues(benchCodes.toArray(new String[benchCodes.size()]));

		String caseSidesCodes = mdmsRepository.getCommaSepratedValues(sideCodes.toArray(new String[sideCodes.size()]));

		if (caseTypesCode != null && !caseTypesCode.isEmpty()) {
			masterMap.put("caseType", caseTypesCode);
		}

		if (caseCategoriesCodes != null && !caseCategoriesCodes.isEmpty()) {
			masterMap.put("caseCategory", caseCategoriesCodes);
		}

		if (caseCourtsCodes != null && !caseCourtsCodes.isEmpty()) {
			masterMap.put("court", caseCourtsCodes);
		}

		if (caseBenchesCodes != null && !caseBenchesCodes.isEmpty()) {
			masterMap.put("bench", caseBenchesCodes);
		}

		if (caseSidesCodes != null && !caseSidesCodes.isEmpty()) {
			masterMap.put("side", caseSidesCodes);
		}
	}

	/**
	 * This method is to add particular master values such as court, side,
	 * caseType, caseCategory, bench, caseStatus, Department
	 * 
	 * @param masterName
	 * @param cases
	 * @param mastersmap
	 * @throws Exception
	 */
	private void addParticularMastervalues(String masterName, List<Case> cases, Map<String, JSONArray> mastersmap)
			throws Exception {

		for (Case caseObj : cases) {

			switch (masterName) {
			case "court": {
				List<Court> courts = objectMapper.readValue(mastersmap.get(masterName).toJSONString(),
						new TypeReference<List<Court>>() {
						});
				if (courts != null && courts.size() > 0)
					caseObj.getSummon().setCourtName(courts.get(0));
				break;
			}

			case "side": {
				List<Side> sides = objectMapper.readValue(mastersmap.get(masterName).toJSONString(),
						new TypeReference<List<Side>>() {
						});
				if (caseObj.getSummon() != null && caseObj.getSummon().getSide() != null && sides != null
						&& sides.size() > 0) {
					List<Side> sideObj = sides.stream()
							.filter(side -> side.getCode().equalsIgnoreCase(caseObj.getSummon().getSide().getCode()))
							.collect(Collectors.toList());

					if (sideObj != null && sideObj.size() > 0)
						caseObj.getSummon().setSide(sideObj.get(0));
				}
				break;
			}

			case "caseType": {
				List<CaseType> caseTypes = objectMapper.readValue(mastersmap.get(masterName).toJSONString(),
						new TypeReference<List<CaseType>>() {
						});
				if (caseObj.getSummon() != null && caseObj.getSummon().getCaseType() != null && caseTypes != null
						&& caseTypes.size() > 0) {
					List<CaseType> caseTypeObj = caseTypes.stream()
							.filter(caseType -> caseType.getCode()
									.equalsIgnoreCase(caseObj.getSummon().getCaseType().getCode()))
							.collect(Collectors.toList());

					if (caseTypeObj != null && caseTypeObj.size() > 0)
						caseObj.getSummon().setCaseType(caseTypeObj.get(0));
				}
				break;
			}

			case "caseCategory": {
				List<CaseCategory> caseCategories = objectMapper.readValue(mastersmap.get(masterName).toJSONString(),
						new TypeReference<List<CaseCategory>>() {
						});
				if (caseObj.getSummon() != null && caseObj.getSummon().getCaseCategory() != null
						&& caseCategories != null && caseCategories.size() > 0) {
					List<CaseCategory> caseCategoriesObj = caseCategories.stream()
							.filter(caseCategory -> caseCategory.getCode()
									.equalsIgnoreCase(caseObj.getSummon().getCaseCategory().getCode()))
							.collect(Collectors.toList());
					if (caseCategoriesObj != null && caseCategoriesObj.size() > 0)
						caseObj.getSummon().setCaseCategory(caseCategoriesObj.get(0));
				}

				break;
			}

			case "bench": {
				List<Bench> benchs = objectMapper.readValue(mastersmap.get(masterName).toJSONString(),
						new TypeReference<List<Bench>>() {
						});
				if (caseObj.getSummon() != null && caseObj.getSummon().getBench() != null && benchs != null
						&& benchs.size() > 0) {
					List<Bench> benchObj = benchs.stream()
							.filter(bench -> bench.getCode().equalsIgnoreCase(caseObj.getSummon().getBench().getCode()))
							.collect(Collectors.toList());
					if (benchObj != null && benchObj.size() > 0)
						caseObj.getSummon().setBench(benchObj.get(0));
				}
				break;
			}

			case "caseStatus": {
				List<CaseStatus> caseStatus = objectMapper.readValue(mastersmap.get(masterName).toJSONString(),
						new TypeReference<List<CaseStatus>>() {
						});

				if (caseStatus != null && caseStatus.size() > 0) {

					List<HearingDetails> hearingDetails = caseObj.getHearingDetails();

					for (HearingDetails hearingDetail : hearingDetails) {

						addHearingDetail(hearingDetail, caseStatus);
					}

					if (hearingDetails != null && caseObj.getCaseStatus() != null
							&& caseObj.getCaseStatus().getCode() != null) {
						List<HearingDetails> details = hearingDetails.stream()
								.filter(status -> status.getCaseStatus().getCode()
										.equalsIgnoreCase(caseObj.getCaseStatus().getCode()))
								.collect(Collectors.toList());

						if (details != null && details.size() > 0)
							caseObj.setCaseStatus(details.get(0).getCaseStatus());
					}
				}

				break;
			}

			case "Department": {
				if (caseObj.getSummon() != null && caseObj.getSummon().getDepartmentName() != null
						&& mastersmap.get(masterName) != null) {
					List<Department> departments = objectMapper.readValue(mastersmap.get(masterName).toJSONString(),
							new TypeReference<List<Department>>() {
							});
					if (departments != null) {
						List<Department> departmentList = departments.stream()
								.filter(department -> department.getCode()
										.equalsIgnoreCase(caseObj.getSummon().getDepartmentName().getCode()))
								.collect(Collectors.toList());
						if (departmentList != null && departmentList.size() > 0)
							caseObj.getSummon().setDepartmentName((departmentList.get(0)));
					}
				}

				break;
			}

			default:
				break;
			}
		}

	}

	/**
	 * This method is to filter Hearing details
	 * 
	 * @param hearingDetail
	 * @param caseStatus
	 */
	private void addHearingDetail(HearingDetails hearingDetail, List<CaseStatus> caseStatus) {
		if (hearingDetail.getCaseStatus() != null && hearingDetail.getCaseStatus().getCode() != null) {
			List<CaseStatus> caseStatusList = caseStatus.stream().filter(
					CaseStatus -> CaseStatus.getCode().equalsIgnoreCase(hearingDetail.getCaseStatus().getCode()))
					.collect(Collectors.toList());
			if (caseStatusList != null && caseStatusList.size() > 0)
				hearingDetail.setCaseStatus((caseStatusList.get(0)));
		}

	}

	/**
	 * This method is to add stamp details for the given case object
	 * 
	 * @param caseObj
	 */
	private void getStampDetails(Case caseObj) {
		RegisterSearchCriteria registerSearchCriteria = new RegisterSearchCriteria();

		Summon summon = caseObj.getSummon();
		if (summon.getRegister() != null && summon.getRegister().getCode() != null) {
			registerSearchCriteria.setCode(new String[] { caseObj.getSummon().getRegister().getCode() });
			registerSearchCriteria.setTenantId(caseObj.getTenantId());
			List<Register> registers = registerRepository.search(registerSearchCriteria);
			if (registers != null && registers.size() > 0)
				caseObj.getSummon().setRegister(registers.get(0));

		}

	}

	/**
	 * This method is to add department details for the given cases
	 * 
	 * @param cases
	 * @param tenantId
	 * @param requestInfo
	 * @throws Exception
	 */
	private void addDepartmentDetails(List<Case> cases, String tenantId, RequestInfo requestInfo) throws Exception {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		List<String> departmentCodes = cases.stream()
				.filter(caseData -> caseData.getSummon() != null && caseData.getSummon().getDepartmentName() != null
						&& caseData.getSummon().getDepartmentName().getCode() != null)
				.map(departmentCode -> departmentCode.getSummon().getDepartmentName().getCode())
				.collect(Collectors.toList());

		Map<String, String> masterCodeAndValue = new HashMap<String, String>();
		String departmentCode = mdmsRepository
				.getCommaSepratedValues(departmentCodes.toArray(new String[departmentCodes.size()]));

		if (departmentCode != null && !departmentCode.isEmpty()) {
			masterCodeAndValue.put("Department", departmentCode);
			MdmsResponse mdmsResponse = mdmsRepository.getMasterData(tenantId, masterCodeAndValue, requestInfoWrapper,
					propertiesManager.getCommonMasterModuleName());
			Map<String, Map<String, JSONArray>> response = mdmsResponse.getMdmsRes();
			Map<String, JSONArray> mastersmap = response.get("common-masters");

			addParticularMastervalues("Department", cases, mastersmap);
		}
	}

	/**
	 * This method is to create the vakalatnama for the case
	 * 
	 * @param caseRequest
	 * @return CaseResponse
	 * @throws Exception
	 */
	public CaseResponse createVakalatnama(CaseRequest caseRequest) throws Exception {
		summonValidator.validateSummon(caseRequest);
		validateVakalatnama(caseRequest);
		kafkaTemplate.send(propertiesManager.getCreateVakalatnama(), caseRequest);
		return new CaseResponse(responseFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	/**
	 * This method is to create leagcy case
	 * 
	 * @param caseRequest
	 * @return {@link CaseResponse}
	 * @throws Exception
	 */
	public CaseResponse createLegacyCase(CaseRequest caseRequest) throws Exception {

		for (Case caseObj : caseRequest.getCases()) {
			if (caseObj.getSummon().getIsSummon()) {
				caseObj.getSummon().setEntryType(EntryType.fromValue(propertiesManager.getSummonType()));
			} else {
				caseObj.getSummon().setEntryType(EntryType.fromValue(propertiesManager.getWarrantType()));
			}
			summonValidator.validateDuplicateAdvocates(caseObj);

			String summonCode = uniqueCodeGeneration.getUniqueCode(caseObj.getTenantId(), caseRequest.getRequestInfo(),
					propertiesManager.getSummonCodeFormat(), propertiesManager.getSummonName(), Boolean.FALSE, null,
					Boolean.FALSE);
			caseObj.setCode(summonCode);

			String summonRefrence = uniqueCodeGeneration.getUniqueCode(caseObj.getTenantId(),
					caseRequest.getRequestInfo(), propertiesManager.getSummonRefrenceFormat(),
					propertiesManager.getSummonReferenceGenName(), Boolean.FALSE, null, Boolean.TRUE);

			caseObj.getSummon().setSummonReferenceNo(summonRefrence);
			caseObj.getSummon().setCode(summonCode);

			String caseReferenceNumber = uniqueCodeGeneration.getUniqueCode(caseObj.getTenantId(),
					caseRequest.getRequestInfo(), propertiesManager.getCaseReferenceFormat(),
					propertiesManager.getCaseReferenceGenName(), Boolean.TRUE,
					caseObj.getSummon().getCaseType().getCode(), Boolean.FALSE);

			caseObj.setCaseRefernceNo(caseReferenceNumber);

			kafkaTemplate.send(propertiesManager.getCreateLegacyCase(), caseRequest);

			if (caseObj.getCaseVoucher() != null) {
				String caseVoucherCode = uniqueCodeGeneration.getUniqueCode(caseObj.getTenantId(),
						caseRequest.getRequestInfo(), propertiesManager.getVoucherCodeFormat(),
						propertiesManager.getVoucherCodeFormatName(), Boolean.FALSE, null, Boolean.FALSE);
				caseObj.getCaseVoucher().setCode(caseVoucherCode);
				caseObj.getCaseVoucher().setCaseCode(summonCode);
				if (caseObj.getTenantId() != null && !caseObj.getTenantId().trim().isEmpty()) {
					caseObj.getCaseVoucher().setTenantId(caseObj.getTenantId());
				}

				kafkaTemplate.send(propertiesManager.getCreateLegacyCaseVoucher(), caseRequest);
			}

			if (caseObj.getAdvocateDetails() != null) {
				for (AdvocateDetails advocatedetail : caseObj.getAdvocateDetails()) {
					String advocateCode = uniqueCodeGeneration.getUniqueCode(caseObj.getTenantId(),
							caseRequest.getRequestInfo(), propertiesManager.getAdvocateDetailsCodeFormat(),
							propertiesManager.getAdvocateDetailsCodeName(), Boolean.FALSE, null, Boolean.FALSE);
					advocatedetail.setCode(advocateCode);
				}
				kafkaTemplate.send(propertiesManager.getCreateLegacyCaseAdvocate(), caseRequest);
			}

			if (caseObj.getHearingDetails() != null) {
				for (HearingDetails hearingDetail : caseObj.getHearingDetails()) {

					String hearingcode = uniqueCodeGeneration.getUniqueCode(caseObj.getTenantId(),
							caseRequest.getRequestInfo(), propertiesManager.getHearingDetailsUlbFormat(),
							propertiesManager.getHearingDetailsUlbName(), Boolean.FALSE, null, Boolean.FALSE);
					hearingDetail.setCode(hearingcode);

				}
				kafkaTemplate.send(propertiesManager.getCreateLegacyHearing(), caseRequest);
			}

			kafkaTemplate.send(propertiesManager.getCreateLegacyCaseIndexer(), caseRequest);
		}

		return new CaseResponse(responseFactory.getResponseInfo(caseRequest.getRequestInfo(), HttpStatus.CREATED),
				caseRequest.getCases());
	}

	/**
	 * This method is to create Hearing detail
	 * 
	 * @param caseRequest
	 * @return CaseResponse
	 * @throws Exception
	 */
	public CaseResponse createHearingDetails(CaseRequest caseRequest) throws Exception {
		List<Case> cases = caseRequest.getCases();
		RequestInfo requestInfo = caseRequest.getRequestInfo();
		for (Case casee : cases) {
			if (casee.getHearingDetails() != null && casee.getHearingDetails().size() > 0) {
				for (HearingDetails hearingDetails : casee.getHearingDetails()) {
					String code = uniqueCodeGeneration.getUniqueCode(casee.getTenantId(), requestInfo,
							propertiesManager.getHearingDetailsUlbFormat(),
							propertiesManager.getHearingDetailsUlbName(), Boolean.FALSE, null, Boolean.FALSE);
					hearingDetails.setCode(code);

					List<Map<String, Object>> hearingDateValues = caseSearchRepository
							.searchHearingDetailsQuery(casee.getCode(), casee.getTenantId());
					log.info("hearing details current date and time values are : " + hearingDateValues);

					if (hearingDateValues != null && hearingDateValues.size() > 0) {
						for (Map<String, Object> mapValues : hearingDateValues) {

							if (mapValues.get("nexthearingtime") != null)
								hearingDetails.setCurrentHearingTime(mapValues.get("nexthearingtime").toString());

							if (mapValues.get("nexthearingdate") != null)
								hearingDetails.setCurrentHearingDate(
										Long.valueOf(mapValues.get("nexthearingdate").toString()));
						}
					} else {
						hearingDetails.setCurrentHearingDate(casee.getSummon().getHearingDate());
						hearingDetails.setCurrentHearingTime(casee.getSummon().getHearingTime());
					}

					if (hearingDetails.getNextHearingDate() != null) {
						createEvents(casee, hearingDetails, requestInfo);
					}

					if (hearingDetails.getCaseStatus() != null)
						casee.setCaseStatus(hearingDetails.getCaseStatus());
				}
			}
		}

		kafkaTemplate.send(propertiesManager.getHearingCreateValidated(), caseRequest);
		return getResponseInfo(caseRequest);
	}

	/**
	 * This method is to create Events
	 * 
	 * @param casee
	 * @param hearingDetails
	 * @param requestInfo
	 * @throws Exception
	 */
	private void createEvents(Case casee, HearingDetails hearingDetails, RequestInfo requestInfo) throws Exception {

		Event event = new Event();
		event.setTenantId(casee.getTenantId());
		event.setCaseNo(casee.getSummon().getCaseNo());
		event.setEntity(propertiesManager.getCaseEntityName());
		event.setModuleName(propertiesManager.getLcmsModuleName());
		event.setEntityCode(casee.getCode());
		event.setDepartmentConcernPerson(casee.getDepartmentPerson());
		event.setNextHearingTime(hearingDetails.getNextHearingTime());
		event.setNextHearingDate(hearingDetails.getNextHearingDate());
		event.setHearingDetailsCode(hearingDetails.getCode());

		if (event.getAuditDetails() == null) {
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(requestInfo.getUserInfo().getId().toString());
			auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
			auditDetails.setCreatedTime(BigDecimal.valueOf((new Date().getTime())));
			auditDetails.setLastModifiedTime(BigDecimal.valueOf((new Date().getTime())));
			event.setAuditDetails(auditDetails);
		}

		String eventCode = uniqueCodeGeneration.getUniqueCode(event.getTenantId(), requestInfo,
				propertiesManager.getEventUlbFormat(), propertiesManager.getEventUlbName(), Boolean.FALSE, null,
				Boolean.FALSE);

		event.setCode(eventCode);

		kafkaTemplate.send(propertiesManager.getEventCreateValidated(), event);

	}

	/**
	 * This method is to update the Hearing details
	 * 
	 * @param caseRequest
	 * @return CaseResponse
	 */
	public CaseResponse updateHearingDetails(CaseRequest caseRequest) {

		for (Case casee : caseRequest.getCases()) {
			if (casee.getHearingDetails() != null) {
				for (HearingDetails hearingDetails : casee.getHearingDetails()) {
					casee.setCaseStatus(hearingDetails.getCaseStatus());
				}
			}
		}

		kafkaTemplate.send(propertiesManager.getHearingUpdateValidated(), caseRequest);
		return getResponseInfo(caseRequest);
	}

	/**
	 * This method is to validate the Vakalatnama object which is inside Case
	 * objet
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

			for (AdvocateDetails advocateDetails : caseObj.getAdvocateDetails()) {

				if (advocateDetails.getAdvocate().getName() == null
						|| advocateDetails.getAdvocate().getName().isEmpty())
					throw new CustomException(propertiesManager.getAdvocatenameCode(),
							propertiesManager.getAdvocatenameMessage());

			}
		}
	}

	/**
	 * This method is to create the Reference Evidence
	 * 
	 * @param caseRequest
	 * @return CaseResponse
	 * @throws Exception
	 */
	public CaseResponse createReferenceEvidence(CaseRequest caseRequest) throws Exception {
		List<Case> cases = caseRequest.getCases();
		for (Case casee : cases) {
			if (casee.getReferenceEvidences() != null && casee.getReferenceEvidences().size() > 0) {
				for (ReferenceEvidence evidence : casee.getReferenceEvidences()) {
					String code = uniqueCodeGeneration.getUniqueCode(casee.getTenantId(), caseRequest.getRequestInfo(),
							propertiesManager.getEvidenceUlbFormat(), propertiesManager.getEvidenceUlbName(),
							Boolean.FALSE, null, Boolean.FALSE);
					evidence.setCode(code);
					evidence.setCaseCode(casee.getCode());
					evidence.setTenantId(casee.getTenantId());
					evidence.setCaseNo(casee.getSummon().getCaseNo());
				}
			}
		}

		kafkaTemplate.send(propertiesManager.getEvidenceCreateTopic(), caseRequest);
		return getResponseInfo(caseRequest);
	}

	/**
	 * This method is to update Reference Evidence
	 * 
	 * @param caseRequest
	 * @return CaseResponse
	 */
	public CaseResponse updateReferenceEvidence(CaseRequest caseRequest) {
		List<Case> cases = caseRequest.getCases();
		for (Case casee : cases) {
			if (casee.getReferenceEvidences() != null && casee.getReferenceEvidences().size() > 0) {
				for (ReferenceEvidence evidence : casee.getReferenceEvidences()) {
					evidence.setCaseCode(casee.getCode());
					evidence.setTenantId(casee.getTenantId());
					evidence.setCaseNo(casee.getSummon().getCaseNo());
				}
			}
		}

		kafkaTemplate.send(propertiesManager.getEvidenceUpdateTopic(), caseRequest);
		return getResponseInfo(caseRequest);
	}

	/**
	 * This method is to fetch the Case details
	 * 
	 * @param tenantId
	 * @param advocateName
	 * @param requestInfo
	 * @return CaseDetailsResponse
	 */
	public CaseDetailsResponse getCaseNo(String tenantId, String advocateName, RequestInfo requestInfo) {

		List<CaseDetails> caseDetails = caseSearchRepository.getCaseDetails(tenantId, advocateName);
		ResponseInfo responseInfo = responseFactory.getResponseInfo(requestInfo, HttpStatus.CREATED);

		return new CaseDetailsResponse(responseInfo, caseDetails);
	}

	/**
	 * This method is to fetch Events based on Event search criteria
	 * 
	 * @param eventSearchCriteria
	 * @param requestInfo
	 * @return EventResponse
	 */
	public EventResponse getEvent(EventSearchCriteria eventSearchCriteria, RequestInfo requestInfo) {

		if (requestInfo.getUserInfo() != null & requestInfo.getUserInfo().getName() != null) {
			log.info("department concern person is : " + requestInfo.getUserInfo().getName());
			eventSearchCriteria.setDepartmentConcernPerson(requestInfo.getUserInfo().getName());
		}
		List<Event> events = caseSearchRepository.getEvent(eventSearchCriteria);
		ResponseInfo responseInfo = responseFactory.getResponseInfo(requestInfo, HttpStatus.CREATED);

		return new EventResponse(responseInfo, events);
	}
}