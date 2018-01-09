package org.egov.works.workorder.domain.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.domain.repository.ContractorAdvanceRequisitionRepository;
import org.egov.works.workorder.domain.repository.LetterOfAcceptanceRepository;
import org.egov.works.workorder.web.contract.AdvanceRequisitionStatus;
import org.egov.works.workorder.web.contract.ContractorAdvanceRequisition;
import org.egov.works.workorder.web.contract.ContractorAdvanceRequisitionRequest;
import org.egov.works.workorder.web.contract.ContractorAdvanceSearchContract;
import org.egov.works.workorder.web.contract.LOAStatus;
import org.egov.works.workorder.web.contract.LetterOfAcceptance;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceSearchContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractorAdvanceValidator {

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;
    
    @Autowired
    private ContractorAdvanceRequisitionRepository contractorAdvanceRequisitionRepository;

    public void validateContractorAdvance(final ContractorAdvanceRequisitionRequest contractorAdvanceRequisitionRequest,
            Boolean isUpdate) {

        HashMap<String, String> messages = new HashMap<>();
        for (ContractorAdvanceRequisition contractorAdvanceRequisition : contractorAdvanceRequisitionRequest
                .getContractorAdvanceRequisitions()) {
            checkLOAExist(contractorAdvanceRequisitionRequest, messages, contractorAdvanceRequisition);
            if (messages != null && !messages.isEmpty())
                throw new CustomException(messages);

            checkAdvanceCreated(contractorAdvanceRequisitionRequest, messages, contractorAdvanceRequisition);
            
            if (messages != null && !messages.isEmpty())
                throw new CustomException(messages);
        }

    }

    private void checkAdvanceCreated(final ContractorAdvanceRequisitionRequest contractorAdvanceRequisitionRequest,
            HashMap<String, String> messages, ContractorAdvanceRequisition contractorAdvanceRequisition) {
        ContractorAdvanceSearchContract contractorAdvanceSearchContract = new ContractorAdvanceSearchContract();
        contractorAdvanceSearchContract.setLetterOfAcceptanceEstimate(contractorAdvanceRequisition.getLetterOfAcceptanceEstimate().getId());
        contractorAdvanceSearchContract.setTenantId(contractorAdvanceRequisition.getTenantId());
//        contractorAdvanceSearchContract.setStatuses(Arrays.asList(AdvanceRequisitionStatus.APPROVED.toString()));
        List<ContractorAdvanceRequisition> contractorAdvanceRequisitions = contractorAdvanceRequisitionRepository.searchContractorAdvances(contractorAdvanceSearchContract, contractorAdvanceRequisitionRequest.getRequestInfo());
        
        if(!contractorAdvanceRequisitions.isEmpty()) {
            messages.put(Constants.KEY_CONTRACTORADVANCE_EXISTS, Constants.MESSAGE_CONTRACTORADVANCE_EXISTS);
        }
    }

    private void checkLOAExist(final ContractorAdvanceRequisitionRequest contractorAdvanceRequisitionRequest,
            HashMap<String, String> messages, ContractorAdvanceRequisition contractorAdvanceRequisition) {

        if (contractorAdvanceRequisition.getLetterOfAcceptanceEstimate() == null) {
            messages.put(Constants.KEY_CONTRACTORADVANCE_LOA_NULL, Constants.MESSAGE_CONTRACTORADVANCE_LOA_NULL);
        }

        if (contractorAdvanceRequisition.getLetterOfAcceptanceEstimate() != null
                && StringUtils.isNotBlank(contractorAdvanceRequisition.getLetterOfAcceptanceEstimate().getLetterOfAcceptance())) {

            LetterOfAcceptanceSearchContract letterOfAcceptanceSearchContract = new LetterOfAcceptanceSearchContract();
            letterOfAcceptanceSearchContract.setIds(
                    Arrays.asList(contractorAdvanceRequisition.getLetterOfAcceptanceEstimate().getLetterOfAcceptance()));
            letterOfAcceptanceSearchContract.setStatuses(Arrays.asList(LOAStatus.APPROVED.toString()));
            List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository
                    .searchLOAs(letterOfAcceptanceSearchContract, contractorAdvanceRequisitionRequest.getRequestInfo());

            if (letterOfAcceptances.isEmpty()) {
                messages.put(Constants.KEY_CONTRACTORADVANCE_LOA_NOTEXISTS, Constants.MESSAGE_CONTRACTORADVANCE_LOA_NOTEXISTS);
            }

        }
    }

    public LetterOfAcceptance getLOA(final ContractorAdvanceRequisitionRequest contractorAdvanceRequisitionRequest,
            ContractorAdvanceRequisition contractorAdvanceRequisition) {
        List<LetterOfAcceptance> letterOfAcceptances = new ArrayList<>();
        if (contractorAdvanceRequisition.getLetterOfAcceptanceEstimate() != null
                && StringUtils.isNotBlank(contractorAdvanceRequisition.getLetterOfAcceptanceEstimate().getLetterOfAcceptance())) {

            LetterOfAcceptanceSearchContract letterOfAcceptanceSearchContract = new LetterOfAcceptanceSearchContract();
            letterOfAcceptanceSearchContract.setIds(
                    Arrays.asList(contractorAdvanceRequisition.getLetterOfAcceptanceEstimate().getLetterOfAcceptance()));
            letterOfAcceptanceSearchContract.setStatuses(Arrays.asList(LOAStatus.APPROVED.toString()));
            letterOfAcceptances = letterOfAcceptanceRepository
                    .searchLOAs(letterOfAcceptanceSearchContract, contractorAdvanceRequisitionRequest.getRequestInfo());

        }
        return letterOfAcceptances.get(0);
    }
}
