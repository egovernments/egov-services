package org.egov.works.services.domain.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.services.config.PropertiesManager;
import org.egov.works.services.domain.repository.EstimateAppropriationRepository;
import org.egov.works.services.domain.repository.IdGenerationRepository;
import org.egov.works.services.web.contract.AuditDetails;
import org.egov.works.services.web.contract.EstimateAppropriation;
import org.egov.works.services.web.contract.EstimateAppropriationRequest;
import org.egov.works.services.web.contract.EstimateAppropriationSearchContract;
import org.egov.works.services.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EstimateAppropriationService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private IdGenerationRepository idGenerationRepository;

	@Autowired
	private EstimateAppropriationRepository estimateAppropriationRepository;

	public Boolean validateEstimateAppropriation(final EstimateAppropriation estimateAppropriation) {

		// TODO : need to Check Budget control type and check budget available
		// for given combination
		Boolean flag = Boolean.FALSE;
		String url = "";

		final RestTemplate restTemplate = new RestTemplate();

		RequestInfo requestInfo = new RequestInfo();

		restTemplate.postForObject(url, requestInfo, Object.class);

		return flag;

	}

	public List<EstimateAppropriation> save(final EstimateAppropriationRequest estimateAppropriationRequest) {
		RequestInfo requestInfo = estimateAppropriationRequest.getRequestInfo();
		AuditDetails auditDetails = new AuditDetails();
		String budgetRefNumber;
		for (EstimateAppropriation estimateAppropriation : estimateAppropriationRequest.getEstimateAppropriations()) {
			auditDetails.setCreatedBy(requestInfo.getUserInfo().getUserName());
			auditDetails.setCreatedTime(new Date().getTime());
			estimateAppropriation.setId(UUID.randomUUID().toString().replace("-", ""));
			estimateAppropriation.setAuditDetails(auditDetails);
			budgetRefNumber = idGenerationRepository.generateAppropriationNumber(estimateAppropriation.getTenantId(),
					estimateAppropriationRequest.getRequestInfo());
			estimateAppropriation
					.setBudgetRefNumber(propertiesManager.getAppropriationNumberPrefix() + budgetRefNumber);
		}
		kafkaTemplate.send(propertiesManager.getEstimateAppropriationsCreateTopic(), estimateAppropriationRequest);
		return estimateAppropriationRequest.getEstimateAppropriations();

	}

	public List<EstimateAppropriation> update(final EstimateAppropriationRequest estimateAppropriationRequest) {
		RequestInfo requestInfo = estimateAppropriationRequest.getRequestInfo();
		AuditDetails auditDetails = new AuditDetails();

		for (EstimateAppropriation estimateAppropriation : estimateAppropriationRequest.getEstimateAppropriations()) {
			auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getUserName());
			auditDetails.setLastModifiedTime(new Date().getTime());
			estimateAppropriation.setAuditDetails(auditDetails);
		}
		kafkaTemplate.send(propertiesManager.getEstimateAppropriationsUpdateTopic(), estimateAppropriationRequest);
		return estimateAppropriationRequest.getEstimateAppropriations();

	}

	public List<EstimateAppropriation> search(EstimateAppropriationSearchContract estimateAppropriationSearchContract) {
		return estimateAppropriationRepository.search(estimateAppropriationSearchContract);
	}

}
