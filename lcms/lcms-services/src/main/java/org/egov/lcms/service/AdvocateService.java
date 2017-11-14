package org.egov.lcms.service;

import java.util.ArrayList;
import java.util.List;
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
import org.egov.lcms.util.UniqueCodeGeneration;
import org.egov.lcms.utility.ConstantUtility;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public AgencyResponse createAgency(AgencyRequest agencyRequest) throws Exception {

		RequestInfo requestInfo = agencyRequest.getRequestInfo();

		for (Agency agency : agencyRequest.getAgencies()) {

			if (agency.getIsIndividual() == Boolean.FALSE) {
				// Adding Agency details to DB

				if (agency.getIsActive() == null)
					agency.setIsActive(true);

				if (agency.getIsTerminate() == null)
					agency.setIsTerminate(false);
				
				if(agency.getAgencyAddress() == null)
					throw new CustomException(propertiesManager.getAgencyAddressErrorCode(),propertiesManager.getAgencyAddressErrorMsg());
				
				if(agency.getName() == null)
					throw new CustomException(propertiesManager.getAgencyNameErrorCode(), propertiesManager.getAgencyNameErrorMsg());

				String agencyCode = uniqueCodeGeneration.getUniqueCode(agency.getTenantId(), requestInfo,
						propertiesManager.getAgencyUlbFormat(), propertiesManager.getAgencyUlbName(), Boolean.FALSE,
						null, Boolean.FALSE);
				agency.setCode(agencyCode);

				// Adding personal details to DB
				if (agency.getPersonDetails() != null) {
					for (PersonDetails personDetails : agency.getPersonDetails()) {

						String name = personDetails.getFirstName() + " " + personDetails.getLastName();

						String personCode = uniqueCodeGeneration.getUniqueCode(agency.getTenantId(), requestInfo,
								propertiesManager.getPersonDetailsUlbFormat(),
								propertiesManager.getPersonDetailsUlbName(), Boolean.FALSE, null, Boolean.FALSE);
						personDetails.setCode(personCode);
						personDetails.setName(name);
						personDetails.setAgencyCode(agency.getCode());
						personDetails.setAgencyName(agency.getName());
					}
				}

				kafkaTemplate.send(propertiesManager.getAgencyCreated(), agencyRequest);
			}

			// Add advocate to DB
			if (agency.getAdvocates() != null) {
				for (Advocate advocate : agency.getAdvocates()) {

					String name = advocate.getFirstName() + " " + advocate.getLastName();

					if (advocate.getIsActive() == null)
						advocate.setIsActive(true);

					if (advocate.getIsTerminate() == null)
						advocate.setIsTerminate(false);

					advocate.setIsIndividual(agency.getIsIndividual());

					if (!advocate.getIsIndividual() && advocate.getAgencyName() == null) {

						throw new CustomException(propertiesManager.getInvalidOrganizationCode(),
								propertiesManager.getOrganizationExceptionMessage());
					}

					advocate.setAgencyCode(agency.getCode());

					String advocateCode = uniqueCodeGeneration.getUniqueCode(advocate.getTenantId(), requestInfo,
							propertiesManager.getAdvocateUlbFormat(), propertiesManager.getAdvocateUlbName(),
							Boolean.FALSE, null, Boolean.FALSE);
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
				validatePersonDetails(agency, createAgencyRequest);

				if (createAgencyRequest.getAgencies().get(0).getPersonDetails() != null
						&& createAgencyRequest.getAgencies().get(0).getPersonDetails().size() > 0) {
					kafkaTemplate.send(propertiesManager.getCreatePersonalDetailsTopic(), createAgencyRequest);
				}
				kafkaTemplate.send(propertiesManager.getAgencyUpdated(), agencyRequest);
			}

			if (agency.getAdvocates() != null) {

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

	private void validatePersonDetails(Agency agency, AgencyRequest createAgencyRequest) throws Exception {

		if (agency.getPersonDetails() != null) {
			List<PersonDetails> personDetailsOnDb = advocateRepository.getPersonalDetailsUsingCode(agency.getCode());
			List<String> personDetailsCodes = personDetailsOnDb.stream().map(details -> details.getCode())
					.collect(Collectors.toList());

			List<PersonDetails> createPersonDetails = new ArrayList<PersonDetails>();
			List<PersonDetails> updatePersonDetails = new ArrayList<PersonDetails>();

			for (PersonDetails reqPersonDetails : agency.getPersonDetails()) {

				String name = reqPersonDetails.getFirstName() + " " + reqPersonDetails.getLastName();

				reqPersonDetails.setName(name);
				reqPersonDetails.setAgencyCode(agency.getCode());

				if (reqPersonDetails.getCode() == null) {
					String code = uniqueCodeGeneration.getUniqueCode(agency.getTenantId(),
							createAgencyRequest.getRequestInfo(), propertiesManager.getPersonDetailsUlbFormat(),
							propertiesManager.getPersonDetailsUlbName(), Boolean.FALSE, null, Boolean.FALSE);
					reqPersonDetails.setCode(code);
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

	private void validateAdvocates(Agency agency, AgencyRequest createAgencyRequest) throws Exception {

		if (agency.getAdvocates() != null) {
			List<Advocate> advocatesOnDb = advocateRepository.getAdvocatesUsingCode(agency.getCode());
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

				if (!reqAdvocate.getIsIndividual() && reqAdvocate.getAgencyName() == null) {

					throw new CustomException(propertiesManager.getInvalidOrganizationCode(),
							propertiesManager.getOrganizationExceptionMessage());
				}

				reqAdvocate.setName(name);
				reqAdvocate.setAgencyCode(agency.getCode());

				if (reqAdvocate.getCode() == null) {
					String advocateCode = uniqueCodeGeneration.getUniqueCode(agency.getTenantId(),
							createAgencyRequest.getRequestInfo(), propertiesManager.getAdvocateUlbFormat(),
							propertiesManager.getAdvocateUlbName(), Boolean.FALSE, null, Boolean.FALSE);
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

	public AgencyResponse searchAgency(String tenantId, Boolean isIndividual, String advocateName, String agencyName,
			RequestInfoWrapper requestInfoWrapper) {
		List<Agency> agencies = advocateRepository.searchAgencies(tenantId, isIndividual, advocateName, agencyName,
				requestInfoWrapper);
		return new AgencyResponse(
				responseInfoFactory.getResponseInfo(requestInfoWrapper.getRequestInfo(), HttpStatus.CREATED), agencies);
	}

	public AdvocateResponse searchAdvocate(AdvocateSearchCriteria advocateSearchCriteria, RequestInfo requestInfo) {

		List<Advocate> advocates = advocateRepository.search(advocateSearchCriteria);
		return new AdvocateResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), advocates);
	}
}
