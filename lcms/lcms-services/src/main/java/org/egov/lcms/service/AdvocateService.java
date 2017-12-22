package org.egov.lcms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.Advocate;
import org.egov.lcms.models.AdvocateResponse;
import org.egov.lcms.models.AdvocateSearchCriteria;
import org.egov.lcms.models.Agency;
import org.egov.lcms.models.AgencyRequest;
import org.egov.lcms.models.AgencyResponse;
import org.egov.lcms.models.PersonDetails;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.lcms.repository.AdvocateRepository;
import org.egov.lcms.util.ConstantUtility;
import org.egov.lcms.util.UniqueCodeGeneration;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 
* 
* Author		Date			eGov-JIRA ticket	Commit message
* ---------------------------------------------------------------------------
* Yosadhara		28th Oct 2017						Initial commit for Advocate service 
* Prasad		08th Nov 2017						Added isSummon value for getUniqueCode method calling
* Veswanth		14th Nov 2017						Added validation for agency and agency address
* Veswanth 		15th Nov 2017						Added agencyCode for new advocate in agency update
* Veswanth 		15th Nov 2017						Added agency and advocate differentiating string in corresponding codes
* Narendra 		16th Nov 2017						Added default title
* Narendra 		28th Nov 2017						Added agencyName for personDetails
*/
@Service
public class AdvocateService {

	@Autowired
	ResponseFactory responseInfoFactory;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	UniqueCodeGeneration uniqueCodeGeneration;

	@Autowired
	AdvocateRepository advocateRepository;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * This method is to create Advocate or Agency
	 * 
	 * @param agencyRequest
	 * @return AgencyResponse
	 * @throws Exception
	 */
	public AgencyResponse createAgency(AgencyRequest agencyRequest) throws Exception {

		RequestInfo requestInfo = agencyRequest.getRequestInfo();

		for (Agency agency : agencyRequest.getAgencies()) {

			if (agency.getIsIndividual() == Boolean.FALSE) {
				// Adding Agency details to DB

				if (agency.getIsActive() == null)
					agency.setIsActive(true);

				if (agency.getIsTerminate() == null)
					agency.setIsTerminate(false);

				if (agency.getStatus() == null)
					agency.setStatus(propertiesManager.getAgencyStatus());

				if (agency.getAgencyAddress() == null)
					throw new CustomException(propertiesManager.getAgencyAddressErrorCode(),
							propertiesManager.getAgencyAddressErrorMsg());

				if (agency.getName() == null)
					throw new CustomException(propertiesManager.getAgencyNameErrorCode(),
							propertiesManager.getAgencyNameErrorMsg());

				if (agency.getDateOfEmpanelment() == null || agency.getStandingCommitteeDecisionDate() == null
						|| agency.getEmpanelmentFromDate() == null || agency.getNewsPaperAdvertismentDate() == null
						|| agency.getEmpanelmentToDate() == null) {
					throw new CustomException(propertiesManager.getEmpanelmentErrorCode(),
							propertiesManager.getEmpanelmentErrorMsg());
				}

				if (agency.getBankName() == null || agency.getBankBranch() == null || agency.getBankAccountNo() == null
						|| agency.getIfscCode() == null || agency.getMicr() == null) {
					throw new CustomException(propertiesManager.getBankDetailsErrorCode(),
							propertiesManager.getBankDetailsErrorMsg());
				}

				String agencyCode = uniqueCodeGeneration.getUniqueCode(agency.getTenantId(), requestInfo,
						propertiesManager.getAgencyUlbFormat(), propertiesManager.getAgencyUlbName(), Boolean.FALSE,
						null, Boolean.FALSE);

				agencyCode = agencyCode.substring(0, agency.getTenantId().length())
						+ propertiesManager.getAgencySubStringCode()
						+ agencyCode.substring(agency.getTenantId().length(), agencyCode.length());
				agency.setCode(agencyCode);

				// Adding personal details to DB
				if (agency.getPersonDetails() != null && agency.getPersonDetails().size() > 0) {
					for (PersonDetails personDetails : agency.getPersonDetails()) {

						String name = personDetails.getFirstName() + " " + personDetails.getLastName();

						String personCode = uniqueCodeGeneration.getUniqueCode(agency.getTenantId(), requestInfo,
								propertiesManager.getPersonDetailsUlbFormat(),
								propertiesManager.getPersonDetailsUlbName(), Boolean.FALSE, null, Boolean.FALSE);
						personDetails.setCode(personCode);
						personDetails.setName(name);
						personDetails.setTenantId(agency.getTenantId());
						personDetails.setAgencyCode(agency.getCode());
						personDetails.setAgencyName(agency.getName());
						if (personDetails.getTitle() == null) {
							personDetails.setTitle(propertiesManager.getDefaultTitle());
						}
					}
				}

				kafkaTemplate.send(propertiesManager.getAgencyCreated(), agencyRequest);
			}

			// Add advocate to DB
			if (agency.getAdvocates() != null && agency.getAdvocates().size() > 0) {
				for (Advocate advocate : agency.getAdvocates()) {

					String name = advocate.getFirstName() + " " + advocate.getLastName();

					if (advocate.getIsActive() == null)
						advocate.setIsActive(true);

					if (advocate.getIsTerminate() == null)
						advocate.setIsTerminate(false);

					if (advocate.getStatus() == null)
						advocate.setStatus(propertiesManager.getAgencyStatus());

					advocate.setIsIndividual(agency.getIsIndividual());
					advocate.setTenantId(agency.getTenantId());
					if (advocate.getTitle() == null) {
						advocate.setTitle(propertiesManager.getDefaultTitle());
					}

					if (!advocate.getIsIndividual()) {

						advocate.setAgencyName(agency.getName());
						advocate.setDateOfEmpanelment(agency.getDateOfEmpanelment());
						advocate.setStandingCommitteeDecisionDate(agency.getStandingCommitteeDecisionDate());
						advocate.setNewsPaperAdvertismentDate(agency.getNewsPaperAdvertismentDate());
						advocate.setEmpanelmentFromDate(agency.getEmpanelmentFromDate());
						advocate.setEmpanelmentToDate(agency.getEmpanelmentToDate());

					}

					advocate.setAgencyCode(agency.getCode());

					String advocateCode = uniqueCodeGeneration.getUniqueCode(advocate.getTenantId(), requestInfo,
							propertiesManager.getAdvocateUlbFormat(), propertiesManager.getAdvocateUlbName(),
							Boolean.FALSE, null, Boolean.FALSE);

					advocateCode = advocateCode.substring(0, advocate.getTenantId().length())
							+ propertiesManager.getAdvocateSubStringCode()
							+ advocateCode.substring(advocate.getTenantId().length(), advocateCode.length());
					advocate.setCode(advocateCode);
					advocate.setName(name);

					kafkaTemplate.send(propertiesManager.getCreateAdvocateTopic(), agencyRequest);

				}
			}
		}

		return new AgencyResponse(
				responseInfoFactory.getResponseInfo(agencyRequest.getRequestInfo(), HttpStatus.CREATED),
				agencyRequest.getAgencies());
	}

	/**
	 * This method is to update Advocate or Agency
	 * 
	 * @param agencyRequest
	 * @return AgencyResponse
	 * @throws Exception
	 */
	@Transactional
	public AgencyResponse updateAgency(AgencyRequest agencyRequest) throws Exception {

		for (Agency agency : agencyRequest.getAgencies()) {
			AgencyRequest createAgencyRequest = new AgencyRequest();
			List<Agency> agencies = new ArrayList<>();
			Agency agencyObject = new Agency();
			agencies.add(agencyObject);
			createAgencyRequest.setAgencies(agencies);

			createAgencyRequest.setRequestInfo(agencyRequest.getRequestInfo());

			if (agency.getIsIndividual() == Boolean.FALSE) {

				if (agency.getIsActive() == null)
					agency.setIsActive(true);

				if (agency.getIsTerminate() == null)
					agency.setIsTerminate(false);

				if (agency.getStatus() == null)
					agency.setStatus(propertiesManager.getAgencyStatus());

				if (agency.getAgencyAddress() == null)
					throw new CustomException(propertiesManager.getAgencyAddressErrorCode(),
							propertiesManager.getAgencyAddressErrorMsg());

				if (agency.getName() == null)
					throw new CustomException(propertiesManager.getAgencyNameErrorCode(),
							propertiesManager.getAgencyNameErrorMsg());

				if (agency.getDateOfEmpanelment() == null || agency.getStandingCommitteeDecisionDate() == null
						|| agency.getEmpanelmentFromDate() == null || agency.getNewsPaperAdvertismentDate() == null
						|| agency.getEmpanelmentToDate() == null) {
					throw new CustomException(propertiesManager.getEmpanelmentErrorCode(),
							propertiesManager.getEmpanelmentErrorMsg());
				}

				if (agency.getBankName() == null || agency.getBankBranch() == null || agency.getBankAccountNo() == null
						|| agency.getIfscCode() == null || agency.getMicr() == null) {
					throw new CustomException(propertiesManager.getBankDetailsErrorCode(),
							propertiesManager.getBankDetailsErrorMsg());
				}

				validatePersonDetails(agency, createAgencyRequest);

				if (createAgencyRequest.getAgencies().get(0).getPersonDetails() != null
						&& createAgencyRequest.getAgencies().get(0).getPersonDetails().size() > 0) {
					kafkaTemplate.send(propertiesManager.getCreatePersonalDetailsTopic(), createAgencyRequest);
				}
				kafkaTemplate.send(propertiesManager.getAgencyUpdated(), agencyRequest);
			}

			if (agency.getAdvocates() != null && agency.getAdvocates().size() > 0) {

				validateAdvocates(agency, createAgencyRequest);

				if (createAgencyRequest.getAgencies().get(0).getAdvocates() != null
						&& createAgencyRequest.getAgencies().get(0).getAdvocates().size() > 0) {
					kafkaTemplate.send(propertiesManager.getCreateAdvocateTopic(), createAgencyRequest);
				}

				kafkaTemplate.send(propertiesManager.getUpdateAdvocateTopic(), agencyRequest);
			}
		}

		return new AgencyResponse(
				responseInfoFactory.getResponseInfo(agencyRequest.getRequestInfo(), HttpStatus.CREATED),
				agencyRequest.getAgencies());

	}

	/**
	 * This method is to validate Agency data
	 * 
	 * @param agency
	 * @param createAgencyRequest
	 * @throws Exception
	 */
	private void validatePersonDetails(Agency agency, AgencyRequest createAgencyRequest) throws Exception {

		if (agency.getPersonDetails() != null && agency.getPersonDetails().size() > 0) {
			List<PersonDetails> personDetailsOnDb = advocateRepository.getPersonalDetailsUsingCode(agency.getTenantId(),
					agency.getCode());
			List<String> personDetailsCodes = personDetailsOnDb.stream().map(details -> details.getCode())
					.collect(Collectors.toList());

			List<PersonDetails> createPersonDetails = new ArrayList<PersonDetails>();
			List<PersonDetails> updatePersonDetails = new ArrayList<PersonDetails>();

			for (PersonDetails reqPersonDetails : agency.getPersonDetails()) {

				String name = reqPersonDetails.getFirstName() + " " + reqPersonDetails.getLastName();

				reqPersonDetails.setName(name);
				reqPersonDetails.setAgencyCode(agency.getCode());
				reqPersonDetails.setTenantId(agency.getTenantId());
				if (reqPersonDetails.getTitle() == null) {
					reqPersonDetails.setTitle(propertiesManager.getDefaultTitle());
				}

				if (reqPersonDetails.getCode() == null) {
					String code = uniqueCodeGeneration.getUniqueCode(agency.getTenantId(),
							createAgencyRequest.getRequestInfo(), propertiesManager.getPersonDetailsUlbFormat(),
							propertiesManager.getPersonDetailsUlbName(), Boolean.FALSE, null, Boolean.FALSE);
					reqPersonDetails.setCode(code);
					reqPersonDetails.setAgencyName(agency.getName());
					createPersonDetails.add(reqPersonDetails);
					break;
				} else {
					List<PersonDetails> personDetails = personDetailsOnDb.stream()
							.filter(details -> details.getCode().equalsIgnoreCase(reqPersonDetails.getCode()))
							.collect(Collectors.toList());
					if (personDetails != null && personDetails.size() == 1) {
						personDetails.set(0, reqPersonDetails);
						updatePersonDetails.add(personDetails.get(0));
						personDetailsCodes.remove(personDetails.get(0).getCode());
					}
				}
			}

			agency.setPersonDetails(updatePersonDetails);
			createAgencyRequest.getAgencies().get(0).setPersonDetails(createPersonDetails);

			personDetailsCodes.forEach(code -> {
				advocateRepository.delete(code, agency.getTenantId(), ConstantUtility.PERSONAL_DETAILS_TABLE_NAME);
			});
		}
	}

	/**
	 * This method is to validate Advocate data
	 * 
	 * @param agency
	 * @param createAgencyRequest
	 * @throws Exception
	 */
	private void validateAdvocates(Agency agency, AgencyRequest createAgencyRequest) throws Exception {

		if (agency.getAdvocates() != null && agency.getAdvocates().size() > 0) {
			List<Advocate> advocatesOnDb = new ArrayList<Advocate>();
			if (agency.getIsIndividual()) {
				advocatesOnDb = advocateRepository.getAdvocatesUsingCode(agency.getTenantId(),
						agency.getAdvocates().get(0).getCode(), agency.getIsIndividual());
			} else {
				advocatesOnDb = advocateRepository.getAdvocatesUsingCode(agency.getTenantId(), agency.getCode(),
						agency.getIsIndividual());
			}

			List<String> advocateCodes = advocatesOnDb.stream().map(Advocate -> Advocate.getCode())
					.collect(Collectors.toList());

			List<Advocate> createAdvocates = new ArrayList<Advocate>();
			List<Advocate> updateAdvocates = new ArrayList<Advocate>();

			for (Advocate reqAdvocate : agency.getAdvocates()) {

				String name = reqAdvocate.getFirstName() + " " + reqAdvocate.getLastName();

				if (reqAdvocate.getIsActive() == null)
					reqAdvocate.setIsActive(true);

				if (reqAdvocate.getIsTerminate() == null)
					reqAdvocate.setIsTerminate(false);

				if (reqAdvocate.getStatus() == null)
					reqAdvocate.setStatus(propertiesManager.getAgencyStatus());

				reqAdvocate.setIsIndividual(agency.getIsIndividual());
				reqAdvocate.setTenantId(agency.getTenantId());

				if (!reqAdvocate.getIsIndividual()) {

					reqAdvocate.setAgencyName(agency.getName());
					reqAdvocate.setDateOfEmpanelment(agency.getDateOfEmpanelment());
					reqAdvocate.setStandingCommitteeDecisionDate(agency.getStandingCommitteeDecisionDate());
					reqAdvocate.setNewsPaperAdvertismentDate(agency.getNewsPaperAdvertismentDate());
					reqAdvocate.setEmpanelmentFromDate(agency.getEmpanelmentFromDate());
					reqAdvocate.setEmpanelmentToDate(agency.getEmpanelmentToDate());
					if (reqAdvocate.getTitle() != null) {
						reqAdvocate.setTitle(propertiesManager.getDefaultTitle());
					}
				}

				reqAdvocate.setName(name);
				reqAdvocate.setAgencyCode(agency.getCode());
				reqAdvocate.setTenantId(agency.getTenantId());
				reqAdvocate.setAgencyCode(agency.getCode());

				if (reqAdvocate.getCode() == null) {
					String advocateCode = uniqueCodeGeneration.getUniqueCode(agency.getTenantId(),
							createAgencyRequest.getRequestInfo(), propertiesManager.getAdvocateUlbFormat(),
							propertiesManager.getAdvocateUlbName(), Boolean.FALSE, null, Boolean.FALSE);

					advocateCode = advocateCode.substring(0, reqAdvocate.getTenantId().length())
							+ propertiesManager.getAdvocateSubStringCode()
							+ advocateCode.substring(reqAdvocate.getTenantId().length(), advocateCode.length());
					reqAdvocate.setCode(advocateCode);
					createAdvocates.add(reqAdvocate);
					break;
				} else {
					List<Advocate> advocates = advocatesOnDb.stream()
							.filter(Advocate -> Advocate.getCode().equalsIgnoreCase(reqAdvocate.getCode()))
							.collect(Collectors.toList());
					if (advocates != null && advocates.size() == 1) {
						advocates.set(0, reqAdvocate);
						updateAdvocates.add(advocates.get(0));
						advocateCodes.remove(advocates.get(0).getCode());
					}
				}
			}

			agency.setAdvocates(updateAdvocates);
			createAgencyRequest.getAgencies().get(0).setAdvocates(createAdvocates);

			advocateCodes.forEach(code -> {
				advocateRepository.delete(code, agency.getTenantId(), ConstantUtility.ADVOCATE_TABLE_NAME);
			});
		}
	}

	/**
	 * This method is to fetch Agency based on searching criteria
	 * 
	 * @param tenantId
	 * @param code
	 * @param isIndividual
	 * @param advocateName
	 * @param agencyName
	 * @param requestInfoWrapper
	 * @return AgencyResponse
	 */
	public AgencyResponse searchAgency(String tenantId, String code, Boolean isIndividual, String advocateName,
			String agencyName, RequestInfoWrapper requestInfoWrapper) {
		List<Agency> agencies = advocateRepository.searchAgencies(tenantId, code, isIndividual, advocateName,
				agencyName, requestInfoWrapper);
		return new AgencyResponse(
				responseInfoFactory.getResponseInfo(requestInfoWrapper.getRequestInfo(), HttpStatus.CREATED), agencies);
	}

	/**
	 * This method is to fetch Advocate based on searching criteria
	 * 
	 * @param advocateSearchCriteria
	 * @param requestInfo
	 * @return AdvocateResponse
	 */
	public AdvocateResponse searchAdvocate(AdvocateSearchCriteria advocateSearchCriteria, RequestInfo requestInfo) {

		List<Advocate> advocates = advocateRepository.search(advocateSearchCriteria);
		if (advocateSearchCriteria.getStatus() != null && !advocateSearchCriteria.getStatus().isEmpty()) {
			List<Advocate> agencyAdvocates = advocates.stream()
					.filter(advocate -> advocate.getAgencyCode() != null && !advocate.getAgencyCode().isEmpty())
					.collect(Collectors.toList());
			List<Advocate> inactiveAdvocateList = null;
			if (agencyAdvocates != null && agencyAdvocates.size() > 0) {

				Set<String> agencyCodes = agencyAdvocates.stream().map(advocate -> advocate.getAgencyCode())
						.collect(Collectors.toSet());

				List<Agency> agencies = advocateRepository.getAgenciesWithAgencyCodeList(agencyCodes,
						advocateSearchCriteria.getStatus());

				inactiveAdvocateList = new ArrayList<Advocate>();
				for (Advocate advocate : agencyAdvocates) {
					List<Agency> agencyList = agencies.stream()
							.filter(agency -> agency.getCode().equalsIgnoreCase(advocate.getAgencyCode()))
							.collect(Collectors.toList());
					if (agencyList.size()==0 ) {
						inactiveAdvocateList.add(advocate);
					}
				}
			}
			if (inactiveAdvocateList != null && inactiveAdvocateList.size() > 0) {
				advocates.removeAll(inactiveAdvocateList);
			}
		}

		return new AdvocateResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), advocates);
	}
}
