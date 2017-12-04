package org.egov.works.workorder.domain.validator;

import org.egov.tracer.model.CustomException;
import org.egov.works.commons.web.contract.LOAStatus;
import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.domain.repository.LetterOfAcceptanceRepository;
import org.egov.works.workorder.domain.service.EstimateService;
import org.egov.works.workorder.domain.service.OfflineStatusService;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ramki on 11/11/17.
 */
@Service
public class LetterOfAcceptanceValidator {

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private OfflineStatusService offlineStatusService;

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    private static void validateUniqueLOANumber(LetterOfAcceptanceRequest letterOfAcceptanceRequest,
            HashMap<String, String> messages, LetterOfAcceptance letterOfAcceptance,
            LetterOfAcceptanceRepository letterOfAcceptanceRepository) {

        LetterOfAcceptanceSearchContract letterOfAcceptanceSearchContract = new LetterOfAcceptanceSearchContract();

        letterOfAcceptanceSearchContract.setTenantId(letterOfAcceptance.getTenantId());
        letterOfAcceptanceSearchContract.setLoaNumbers(Arrays.asList(letterOfAcceptance.getLoaNumber()));

        List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository.searchLOAs(letterOfAcceptanceSearchContract,
                letterOfAcceptanceRequest.getRequestInfo());

        for (LetterOfAcceptance acceptance : letterOfAcceptances) {
            if (!acceptance.getStatus().toString().equalsIgnoreCase(LOAStatus.CANCELLED.toString())) {
                messages.put(Constants.KEY_INVALID_LOA_EXISTS, Constants.MESSAGE_INVALID_LOA_EXISTS);
            }
        }

        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
    }

    public void validateLetterOfAcceptance(final LetterOfAcceptanceRequest letterOfAcceptanceRequest, Boolean isUpdate,
            Boolean isRevision) {
        DetailedEstimate detailedEstimate = null;
        HashMap<String, String> messages = new HashMap<>();
        for (LetterOfAcceptance letterOfAcceptance : letterOfAcceptanceRequest.getLetterOfAcceptances()) {

            if (!isUpdate && letterOfAcceptance.getLoaNumber() != null && !letterOfAcceptance.getLoaNumber().isEmpty()) {
                validateUniqueLOANumber(letterOfAcceptanceRequest, messages, letterOfAcceptance, letterOfAcceptanceRepository);
            }
            if (isUpdate) {
                checkLOAExists(letterOfAcceptanceRequest, messages, letterOfAcceptance);
            }

            for (LetterOfAcceptanceEstimate letterOfAcceptanceEstimate : letterOfAcceptance
                    .getLetterOfAcceptanceEstimates()) {
                List<DetailedEstimate> detailedEstimates = estimateService
                        .getDetailedEstimate(letterOfAcceptanceEstimate.getDetailedEstimate().getEstimateNumber(),
                                letterOfAcceptanceEstimate.getTenantId(), letterOfAcceptanceRequest.getRequestInfo())
                        .getDetailedEstimates();

                if (!detailedEstimates.isEmpty())
                    detailedEstimate = detailedEstimates.get(0);

                validateDetailedEstimate(detailedEstimate, messages, letterOfAcceptance);

                if (messages != null && !messages.isEmpty())
                    throw new CustomException(messages);

                if (detailedEstimate.getWorkOrderCreated() && isRevision == null || (isRevision != null && !isRevision)) {
                    validateOfflineStatus(letterOfAcceptanceRequest, messages, letterOfAcceptance,
                            letterOfAcceptanceEstimate);

                    if (messages != null && !messages.isEmpty())
                        throw new CustomException(messages);
                }
            }

            validateLOA(messages, letterOfAcceptance);

            if (messages != null && !messages.isEmpty())
                throw new CustomException(messages);

        }

    }

    private OfflineStatus validateOfflineStatus(final LetterOfAcceptanceRequest letterOfAcceptanceRequest,
            HashMap<String, String> messages, LetterOfAcceptance letterOfAcceptance,
            LetterOfAcceptanceEstimate letterOfAcceptanceEstimate) {
        OfflineStatus offlineStatus = null;
        List<OfflineStatus> offlineStatuses = offlineStatusService
                .getOfflineStatus(letterOfAcceptanceEstimate.getDetailedEstimate().getEstimateNumber(),
                        letterOfAcceptance.getTenantId(), letterOfAcceptanceRequest.getRequestInfo())
                .getOfflineStatuses();
        if (!offlineStatuses.isEmpty())
            offlineStatus = offlineStatuses.get(0);

        if (offlineStatus == null) {
            messages.put(Constants.KEY_DETAILEDESTIMATE_OFFLINE_STATUS,
                    Constants.MESSAGE_DETAILEDESTIMATE_OFFLINE_STATUS);
        }

        if (offlineStatus != null && letterOfAcceptance.getLoaDate() > offlineStatus.getStatusDate())
            messages.put(Constants.KEY_FUTUREDATE_LOADATE_OFFLINESTATUS,
                    Constants.MESSAGE_FUTUREDATE_LOADATE_OFFLINESTATUS);

        return offlineStatus;
    }

    private void checkLOAExists(LetterOfAcceptanceRequest letterOfAcceptanceRequest, HashMap<String, String> messages,
            LetterOfAcceptance letterOfAcceptance) {
        LetterOfAcceptanceSearchContract letterOfAcceptanceSearchContract = new LetterOfAcceptanceSearchContract();

        letterOfAcceptanceSearchContract.setTenantId(letterOfAcceptance.getTenantId());
        if (letterOfAcceptance.getId() != null && letterOfAcceptance.getId().isEmpty())
            letterOfAcceptanceSearchContract.setIds(Arrays.asList(letterOfAcceptance.getId()));
        letterOfAcceptanceSearchContract.setLoaNumbers(Arrays.asList(letterOfAcceptance.getLoaNumber()));
        
        List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository.searchLOAs(letterOfAcceptanceSearchContract,
                letterOfAcceptanceRequest.getRequestInfo());

        if (letterOfAcceptances.isEmpty()) {
            messages.put(Constants.KEY_INVALID_LOA, Constants.MESSAGE_INVALID_LOA);
        }

        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
    }

    private void validateLOA(HashMap<String, String> messages,
            LetterOfAcceptance letterOfAcceptance) {
        if (letterOfAcceptance.getLoaDate() > new Date().getTime()) {
            messages.put(Constants.KEY_FUTUREDATE_LOADATE, Constants.MESSAGE_FUTUREDATE_LOADATE);
        }

        if (letterOfAcceptance.getFileDate() > new Date().getTime()) {
            messages.put(Constants.KEY_FUTUREDATE_FILEDATE, Constants.MESSAGE_FUTUREDATE_FILEDATE);
        }

    }

    private void validateDetailedEstimate(DetailedEstimate detailedEstimate, HashMap<String, String> messages,
            LetterOfAcceptance letterOfAcceptance) {

        if (detailedEstimate == null)
            messages.put(Constants.KEY_DETAILEDESTIMATE_EXIST, Constants.MESSAGE_DETAILEDESTIMATE_EXIST);

        if (detailedEstimate != null && !detailedEstimate.getStatus().toString()
                .equalsIgnoreCase(DetailedEstimateStatus.TECHNICAL_SANCTIONED.toString())) {
            messages.put(Constants.KEY_DETAILEDESTIMATE_STATUS, Constants.MESSAGE_DETAILEDESTIMATE_STATUS);
        }

        if (detailedEstimate.getWorkOrderCreated()
                && (letterOfAcceptance.getLoaNumber() == null || letterOfAcceptance.getLoaNumber().isEmpty())) {
            messages.put(Constants.KEY_WORKORDER_LOANUMBER_REQUIRED, Constants.MESSAGE_WORKORDER_LOANUMBER_REQUIRED);
        }
    }

    @SuppressWarnings("static-access")
    public LetterOfAcceptance searchAbstractEstimate(LetterOfAcceptance letterOfAcceptance,
            final RequestInfo requestInfo) {

        @SuppressWarnings("unused")
        LetterOfAcceptance savedLetterOfAcceptance = new LetterOfAcceptance();

        LetterOfAcceptanceSearchContract letterOfAcceptanceSearchCriteria = new LetterOfAcceptanceSearchContract();
        letterOfAcceptanceSearchCriteria.builder().tenantId(letterOfAcceptance.getTenantId())
                .loaNumbers(Arrays.asList(letterOfAcceptance.getLoaNumber())).build();

        List<LetterOfAcceptance> letterOfAcceptances = letterOfAcceptanceRepository
                .searchLOAs(letterOfAcceptanceSearchCriteria, requestInfo);

        if (!letterOfAcceptances.isEmpty())
            savedLetterOfAcceptance = letterOfAcceptances.get(0);

        return null;
    }

}
