package org.egov.works.workorder.domain.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.workorder.config.PropertiesManager;
import org.egov.works.workorder.domain.repository.ContractorAdvanceRequisitionRepository;
import org.egov.works.workorder.domain.repository.RequisitionRepository;
import org.egov.works.workorder.domain.validator.ContractorAdvanceValidator;
import org.egov.works.workorder.utils.WorkOrderUtils;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<ContractorAdvanceRequisition> contractorAdvanceRequisitionList = new ArrayList<>();
        for (ContractorAdvanceRequisition contractorAdvanceRequisition : contractorAdvanceRequisitionRequest
                .getContractorAdvanceRequisitions()) {

            LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = workOrderUtils.searchLoaEstimateById(contractorAdvanceRequisition.getTenantId(),contractorAdvanceRequisition.getLetterOfAcceptanceEstimate(), contractorAdvanceRequisitionRequest.getRequestInfo());
            if(letterOfAcceptanceEstimate != null)
                contractorAdvanceRequisition.setLetterOfAcceptanceEstimate(letterOfAcceptanceEstimate);
            // Set id from of Advance requisition which you get after rest call
            contractorAdvanceRequisition.setId(requisitionRepository.createAdvanceRequisition());
            contractorAdvanceRequisition
                    .setAuditDetails(workOrderUtils.setAuditDetails(contractorAdvanceRequisitionRequest.getRequestInfo(), false));

            LetterOfAcceptance letterOfAcceptance = contractorAdvanceValidator.getLOA(contractorAdvanceRequisitionRequest,
                    contractorAdvanceRequisition);

            contractorAdvanceRequisition
                    .setLetterOfAcceptanceEstimate(letterOfAcceptance.getLetterOfAcceptanceEstimates().get(0));

            if(contractorAdvanceRequisition.getStatus() != null && (contractorAdvanceRequisition.getStatus().equals(CommonConstants.STATUS_CREATED) ||
                    contractorAdvanceRequisition.getStatus().equals(CommonConstants.STATUS_APPROVED))) {
                contractorAdvanceRequisitionList.add(contractorAdvanceRequisition);
            }

        }
        kafkaTemplate.send(propertiesManager.getWorksAdvanceSaveOrUpdateValidatedTopic(), contractorAdvanceRequisitionRequest);
        if(contractorAdvanceRequisitionList != null && !contractorAdvanceRequisitionList.isEmpty()) {
            ContractorAdvanceRequisitionRequest backUpdateRequest = new ContractorAdvanceRequisitionRequest();
            backUpdateRequest.setContractorAdvanceRequisitions(contractorAdvanceRequisitionList);
            kafkaTemplate.send(propertiesManager.getWorksLetterofAcceptanceBackupdateOnCreateContractorAdvanceTopic(), backUpdateRequest);
            kafkaTemplate.send(propertiesManager.getWorksWorkOrderBackupdateOnCreateContractorAdvanceTopic(), backUpdateRequest);

        }
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
        List<ContractorAdvanceRequisition> contractorAdvanceRequisitionList = new ArrayList<>();
        for (ContractorAdvanceRequisition contractorAdvanceRequisition : contractorAdvanceRequisitionRequest
                .getContractorAdvanceRequisitions()) {

            LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = workOrderUtils.searchLoaEstimateById(contractorAdvanceRequisition.getTenantId(),contractorAdvanceRequisition.getLetterOfAcceptanceEstimate(), contractorAdvanceRequisitionRequest.getRequestInfo());
            if(letterOfAcceptanceEstimate != null)
                contractorAdvanceRequisition.setLetterOfAcceptanceEstimate(letterOfAcceptanceEstimate);

            // Set id from of Advance requisition which you get after rest call
            if (StringUtils.isBlank(contractorAdvanceRequisition.getId()))
                contractorAdvanceRequisition.setId(requisitionRepository.createAdvanceRequisition());
            
            LetterOfAcceptance letterOfAcceptance = contractorAdvanceValidator.getLOA(contractorAdvanceRequisitionRequest,
                    contractorAdvanceRequisition);

            contractorAdvanceRequisition
                    .setLetterOfAcceptanceEstimate(letterOfAcceptance.getLetterOfAcceptanceEstimates().get(0));

            
            contractorAdvanceRequisition
                    .setAuditDetails(workOrderUtils.setAuditDetails(contractorAdvanceRequisitionRequest.getRequestInfo(), false));

            if(contractorAdvanceRequisition.getStatus() != null && (contractorAdvanceRequisition.getStatus().equals(CommonConstants.STATUS_CANCELLED))) {
                contractorAdvanceRequisitionList.add(contractorAdvanceRequisition);
            }

        }
        kafkaTemplate.send(propertiesManager.getWorksAdvanceSaveOrUpdateValidatedTopic(), contractorAdvanceRequisitionRequest);
        if(contractorAdvanceRequisitionList != null && !contractorAdvanceRequisitionList.isEmpty()) {
            ContractorAdvanceRequisitionRequest backUpdateRequest = new ContractorAdvanceRequisitionRequest();
            backUpdateRequest.setContractorAdvanceRequisitions(contractorAdvanceRequisitionList);
            kafkaTemplate.send(propertiesManager.getWorksLetterofAcceptanceBackupdateOnCancelContractorAdvanceTopic(), backUpdateRequest);
            kafkaTemplate.send(propertiesManager.getWorksWorkOrderBackupdateOnCancelContractorAdvanceTopic(), backUpdateRequest);

        }
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
