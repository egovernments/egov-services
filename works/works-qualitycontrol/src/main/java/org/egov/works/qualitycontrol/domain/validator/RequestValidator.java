package org.egov.works.qualitycontrol.domain.validator;

import org.egov.tracer.model.CustomException;
import org.egov.works.qualitycontrol.config.Constants;
import org.egov.works.qualitycontrol.persistence.repository.FileStoreRepository;
import org.egov.works.qualitycontrol.persistence.repository.LetterOfAcceptanceRepository;
import org.egov.works.qualitycontrol.persistence.repository.WorkOrderRepository;
import org.egov.works.qualitycontrol.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RequestValidator {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    @Autowired
    private FileStoreRepository fileStoreRepository;
    
    public void validateQualityTesting(final QualityTestingRequest qualityTestingRequest) {
        Map<String, String> messages = new HashMap<>();
        for(QualityTesting qualityTesting : qualityTestingRequest.getQualityTestings()) {
            validateLetterOfAcceptance(qualityTesting, messages, qualityTestingRequest.getRequestInfo());
            validateDocuments(qualityTesting,qualityTestingRequest.getRequestInfo(),messages);
        }

        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
    }

    private void validateLetterOfAcceptance(QualityTesting qualityTesting, Map<String, String> messages, RequestInfo requestInfo) {

        List<WorkOrder> workOrderList = workOrderRepository.searchWorkorderByLOA(qualityTesting.getTenantId(),
                Arrays.asList(qualityTesting.getLetterOfAcceptanceEstimate().getLetterOfAcceptance()),requestInfo);

        List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository.searchLOA(qualityTesting.getTenantId(),
                Arrays.asList(qualityTesting.getLetterOfAcceptanceEstimate().getLetterOfAcceptance()),requestInfo);

        if(letterOfAcceptances != null && letterOfAcceptances.isEmpty())
            messages.put(Constants.KEY_QUALITYTESTING_WORKS_LOA_NOT_EXISTS, Constants.MESSAGE_QUALITYTESTING_WORKS_LOA_NOT_EXISTS);

        if(workOrderList != null && workOrderList.isEmpty())
            messages.put(Constants.KEY_QUALITYTESTING_WORKS_WORKORDR_NOT_EXISTS, Constants.MESSAGE_QUALITYTESTING_WORKS_WORKORDR_NOT_EXISTS);
    }

    private void validateDocuments(QualityTesting qualityTesting, RequestInfo requestInfo,
                                   Map<String, String> messages) {
        if (qualityTesting.getTestReports() != null) {
            for (DocumentDetail documentDetail : qualityTesting.getTestReports()) {
                boolean fileExists = fileStoreRepository.searchFileStore(qualityTesting.getTenantId(),
                        documentDetail.getFileStore(), requestInfo);
                if (!fileExists)
                    messages.put(Constants.KEY_QUALITYTESTING_INVALID_FILESTOREID, Constants.MESSAGE_QUALITYTESTING_INVALID_FILESTOREID);
            }
        }
    }
}
