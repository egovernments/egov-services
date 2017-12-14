package org.egov.works.services.domain.service;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.services.config.PropertiesManager;
import org.egov.works.services.domain.repository.EstimateAppropriationRepository;
import org.egov.works.services.domain.repository.IdGenerationRepository;
import org.egov.works.services.domain.validator.RequestValidator;
import org.egov.works.services.utils.ServiceUtils;
import org.egov.works.services.web.contract.EstimateAppropriation;
import org.egov.works.services.web.contract.EstimateAppropriationRequest;
import org.egov.works.services.web.contract.EstimateAppropriationResponse;
import org.egov.works.services.web.contract.EstimateAppropriationSearchContract;
import org.egov.works.services.web.contract.RequestInfo;
import org.egov.works.services.web.contract.factory.ResponseInfoFactory;
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
    private CommonUtils commonUtils;

    @Autowired
    private ServiceUtils serviceUtils;

    @Autowired
    private RequestValidator requestValidator;
    
    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    public EstimateAppropriationResponse create(final EstimateAppropriationRequest estimateAppropriationRequest) {
        String budgetRefNumber;
        for (EstimateAppropriation estimateAppropriation : estimateAppropriationRequest.getEstimateAppropriations()) {
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
            estimateAppropriation.setAuditDetails(serviceUtils
                    .setAuditDetails(estimateAppropriationRequest.getRequestInfo(), true));

        }
        kafkaTemplate.send(propertiesManager.getEstimateAppropriationsCreateTopic(), estimateAppropriationRequest);
        EstimateAppropriationResponse estimateAppropriationResponse = new EstimateAppropriationResponse();
        estimateAppropriationResponse
                .setEstimateAppropriations(estimateAppropriationRequest.getEstimateAppropriations());
        return estimateAppropriationResponse;

    }

    public EstimateAppropriationResponse search(EstimateAppropriationSearchContract estimateAppropriationSearchContract,final RequestInfo requestInfo) {
        requestValidator.validateAppropriationSearchContract(estimateAppropriationSearchContract);
        EstimateAppropriationResponse appropriationResponse = new EstimateAppropriationResponse();
        appropriationResponse.setEstimateAppropriations(estimateAppropriationRepository.search(estimateAppropriationSearchContract));
        appropriationResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, Boolean.TRUE));
        return appropriationResponse;
    }
}
