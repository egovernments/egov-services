package org.egov.works.workorder.domain.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.workorder.config.PropertiesManager;
import org.egov.works.workorder.domain.repository.ContractorAdvanceRequisitionRepository;
import org.egov.works.workorder.domain.repository.RequisitionRepository;
import org.egov.works.workorder.domain.validator.ContractorAdvanceValidator;
import org.egov.works.workorder.utils.WorkOrderUtils;
import org.egov.works.workorder.web.contract.ContractorAdvanceRequisition;
import org.egov.works.workorder.web.contract.ContractorAdvanceRequisitionRequest;
import org.egov.works.workorder.web.contract.ContractorAdvanceRequisitionResponse;
import org.egov.works.workorder.web.contract.ContractorAdvanceSearchContract;
import org.egov.works.workorder.web.contract.LetterOfAcceptance;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractorAdvanceService {

    @Autowired
    private WorkOrderUtils workOrderUtils;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private ContractorAdvanceValidator contractorAdvanceValidator;

    @Autowired
    private RequisitionRepository requisitionRepository;

    @Autowired
    private ContractorAdvanceRequisitionRepository contractorAdvanceRequisitionRepository;

    public ContractorAdvanceRequisitionResponse create(
            final ContractorAdvanceRequisitionRequest contractorAdvanceRequisitionRequest) {
        contractorAdvanceValidator.validateContractorAdvance(contractorAdvanceRequisitionRequest, Boolean.FALSE);
        for (ContractorAdvanceRequisition contractorAdvanceRequisition : contractorAdvanceRequisitionRequest
                .getContractorAdvanceRequisitions()) {

            // Set id from of Advance requisition which you get after rest call
            contractorAdvanceRequisition.setId(requisitionRepository.createAdvanceRequisition());
            contractorAdvanceRequisition
                    .setAuditDetails(workOrderUtils.setAuditDetails(contractorAdvanceRequisitionRequest.getRequestInfo(), false));

            LetterOfAcceptance letterOfAcceptance = contractorAdvanceValidator.getLOA(contractorAdvanceRequisitionRequest,
                    contractorAdvanceRequisition);

            contractorAdvanceRequisition
                    .setLetterOfAcceptanceEstimate(letterOfAcceptance.getLetterOfAcceptanceEstimates().get(0));

        }
        kafkaTemplate.send(propertiesManager.getWorksAdvanceSaveOrUpdateValidatedTopic(), contractorAdvanceRequisitionRequest);
        ContractorAdvanceRequisitionResponse contractorAdvanceRequisitionResponse = new ContractorAdvanceRequisitionResponse();
        contractorAdvanceRequisitionResponse
                .setContractorAdvanceRequisitions(contractorAdvanceRequisitionRequest.getContractorAdvanceRequisitions());
        contractorAdvanceRequisitionResponse
                .setResponseInfo(workOrderUtils.getResponseInfo(contractorAdvanceRequisitionRequest.getRequestInfo()));
        return contractorAdvanceRequisitionResponse;
    }

    public ContractorAdvanceRequisitionResponse update(
            final ContractorAdvanceRequisitionRequest contractorAdvanceRequisitionRequest) {
        contractorAdvanceValidator.validateContractorAdvance(contractorAdvanceRequisitionRequest, Boolean.TRUE);
        for (ContractorAdvanceRequisition contractorAdvanceRequisition : contractorAdvanceRequisitionRequest
                .getContractorAdvanceRequisitions()) {

            // Set id from of Advance requisition which you get after rest call
            if (StringUtils.isBlank(contractorAdvanceRequisition.getId()))
                contractorAdvanceRequisition.setId(requisitionRepository.createAdvanceRequisition());
            
            LetterOfAcceptance letterOfAcceptance = contractorAdvanceValidator.getLOA(contractorAdvanceRequisitionRequest,
                    contractorAdvanceRequisition);

            contractorAdvanceRequisition
                    .setLetterOfAcceptanceEstimate(letterOfAcceptance.getLetterOfAcceptanceEstimates().get(0));

            
            contractorAdvanceRequisition
                    .setAuditDetails(workOrderUtils.setAuditDetails(contractorAdvanceRequisitionRequest.getRequestInfo(), false));

        }
        kafkaTemplate.send(propertiesManager.getWorksAdvanceSaveOrUpdateValidatedTopic(), contractorAdvanceRequisitionRequest);
        ContractorAdvanceRequisitionResponse contractorAdvanceRequisitionResponse = new ContractorAdvanceRequisitionResponse();
        contractorAdvanceRequisitionResponse
                .setContractorAdvanceRequisitions(contractorAdvanceRequisitionRequest.getContractorAdvanceRequisitions());
        contractorAdvanceRequisitionResponse
                .setResponseInfo(workOrderUtils.getResponseInfo(contractorAdvanceRequisitionRequest.getRequestInfo()));
        return contractorAdvanceRequisitionResponse;
    }

    public ContractorAdvanceRequisitionResponse search(final ContractorAdvanceSearchContract contractorAdvanceSearchContract,
            final RequestInfo requestInfo) {
        ContractorAdvanceRequisitionResponse contractorAdvanceRequisitionResponse = new ContractorAdvanceRequisitionResponse();
        contractorAdvanceRequisitionResponse.setContractorAdvanceRequisitions(
                contractorAdvanceRequisitionRepository.searchContractorAdvances(contractorAdvanceSearchContract, requestInfo));
        return contractorAdvanceRequisitionResponse;
    }

}
