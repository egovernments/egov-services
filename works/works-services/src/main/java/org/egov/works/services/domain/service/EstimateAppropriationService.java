package org.egov.works.services.domain.service;

import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.services.config.PropertiesManager;
import org.egov.works.services.domain.repository.EstimateAppropriationRepository;
import org.egov.works.services.domain.repository.IdGenerationRepository;
import org.egov.works.services.domain.validator.EstimateAppropriationValidator;
import org.egov.works.services.utils.ServiceUtils;
import org.egov.works.services.web.contract.EstimateAppropriation;
import org.egov.works.services.web.contract.EstimateAppropriationRequest;
import org.egov.works.services.web.contract.EstimateAppropriationResponse;
import org.egov.works.services.web.contract.EstimateAppropriationSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	@Autowired
	private EstimateAppropriationValidator estimateAppropriationValidator;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private ServiceUtils serviceUtils;

	public EstimateAppropriationResponse create(final EstimateAppropriationRequest estimateAppropriationRequest) {
		String budgetRefNumber;
		for (EstimateAppropriation estimateAppropriation : estimateAppropriationRequest.getEstimateAppropriations()) {
			estimateAppropriationRepository.validateEstimateAppropriation(estimateAppropriation);
			estimateAppropriation.setAuditDetails(serviceUtils
					.setAuditDetails(estimateAppropriationRequest.getRequestInfo(), false));
			estimateAppropriation.setId(commonUtils.getUUID());
			budgetRefNumber = idGenerationRepository.generateAppropriationNumber(estimateAppropriation.getTenantId(),
					estimateAppropriationRequest.getRequestInfo());
			estimateAppropriation
					.setBudgetRefNumber(propertiesManager.getAppropriationNumberPrefix() + budgetRefNumber);
		}
		kafkaTemplate.send(propertiesManager.getEstimateAppropriationsCreateTopic(), estimateAppropriationRequest);
		EstimateAppropriationResponse estimateAppropriationResponse = new EstimateAppropriationResponse();
		estimateAppropriationResponse
				.setEstimateAppropriations(estimateAppropriationRequest.getEstimateAppropriations());
		return estimateAppropriationResponse;

	}

	public EstimateAppropriationResponse update(final EstimateAppropriationRequest estimateAppropriationRequest) {

		for (EstimateAppropriation estimateAppropriation : estimateAppropriationRequest.getEstimateAppropriations()) {
			estimateAppropriationRepository.validateEstimateAppropriation(estimateAppropriation);
			estimateAppropriation.setAuditDetails(serviceUtils
					.setAuditDetails(estimateAppropriationRequest.getRequestInfo(), true));

		}
		kafkaTemplate.send(propertiesManager.getEstimateAppropriationsCreateTopic(), estimateAppropriationRequest);
		EstimateAppropriationResponse estimateAppropriationResponse = new EstimateAppropriationResponse();
		estimateAppropriationResponse
				.setEstimateAppropriations(estimateAppropriationRequest.getEstimateAppropriations());
		return estimateAppropriationResponse;

	}

	public List<EstimateAppropriation> search(EstimateAppropriationSearchContract estimateAppropriationSearchContract) {
		estimateAppropriationValidator.validateSearchContract(estimateAppropriationSearchContract);
		return estimateAppropriationRepository.search(estimateAppropriationSearchContract);
	}
}
