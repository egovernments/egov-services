package org.egov.lcms.service;

import java.util.List;
import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.Advocate;
import org.egov.lcms.models.AdvocateRequest;
import org.egov.lcms.models.AdvocateResponse;
import org.egov.lcms.models.AdvocateSearchCriteria;
import org.egov.lcms.repository.AdvocateRepository;
import org.egov.lcms.util.UniqueCodeGeneration;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

	public AdvocateResponse createAdvocate(AdvocateRequest advocateRequest) throws Exception{

		RequestInfo requestInfo = advocateRequest.getRequestInfo();
		String code = null;
		String name = null;

		for (Advocate advocate : advocateRequest.getAdvocates()) {

			name = advocate.getFirstName() + " " + advocate.getLastName();

			if (advocate.getIsActive() == null)
				advocate.setIsActive(true);

			if (advocate.getIsTerminate() == null)
				advocate.setIsTerminate(false);

			if (!advocate.getIsIndividual() && advocate.getOrganizationName() == null) {

				throw new CustomException(propertiesManager.getInvalidOrganizationCode(),
						propertiesManager.getOrganizationExceptionMessage());
			}
			code = uniqueCodeGeneration.getUniqueCode(advocate.getTenantId(), requestInfo,
					propertiesManager.getAdvocateUlbFormat(), propertiesManager.getAdvocateUlbName(), Boolean.FALSE,
					null,Boolean.FALSE);
			advocate.setCode(code);
			advocate.setName(name);
		}

		kafkaTemplate.send(propertiesManager.getCreateAdvocateTopic(), advocateRequest);
		
		return new AdvocateResponse(
				responseInfoFactory.getResponseInfo(advocateRequest.getRequestInfo(), HttpStatus.CREATED),
				advocateRequest.getAdvocates());
	}

	public AdvocateResponse updateAdvocate(AdvocateRequest advocateRequest) throws Exception {

		String name = null;

		for (Advocate advocate : advocateRequest.getAdvocates()) {

			name = advocate.getFirstName() + " " + advocate.getLastName();

			if (advocate.getIsActive() == null)
				advocate.setIsActive(true);

			if (advocate.getIsTerminate() == null)
				advocate.setIsTerminate(false);

			if (!advocate.getIsIndividual() && advocate.getOrganizationName() == null) {

				throw new CustomException(propertiesManager.getInvalidOrganizationCode(),
						propertiesManager.getOrganizationExceptionMessage());
			}

			advocate.setName(name);
		}

		kafkaTemplate.send(propertiesManager.getUpdateAdvocateTopic(), advocateRequest);

		return new AdvocateResponse(
				responseInfoFactory.getResponseInfo(advocateRequest.getRequestInfo(), HttpStatus.CREATED),
				advocateRequest.getAdvocates());
	}

	public AdvocateResponse searchAdvocate(AdvocateSearchCriteria advocateSearchCriteria, RequestInfo requestInfo) {

		List<Advocate> advocates = advocateRepository.search(advocateSearchCriteria);
		return new AdvocateResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), advocates);
	}
}
