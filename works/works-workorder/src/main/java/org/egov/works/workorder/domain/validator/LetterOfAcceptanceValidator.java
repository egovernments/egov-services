package org.egov.works.workorder.domain.validator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.egov.tracer.model.CustomException;
import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.domain.service.EstimateService;
import org.egov.works.workorder.domain.service.OfflineStatusService;
import org.egov.works.workorder.web.contract.DetailedEstimate;
import org.egov.works.workorder.web.contract.DetailedEstimateStatus;
import org.egov.works.workorder.web.contract.LetterOfAcceptance;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceEstimate;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceRequest;
import org.egov.works.workorder.web.contract.OfflineStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ramki on 11/11/17.
 */
@Service
public class LetterOfAcceptanceValidator {

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private OfflineStatusService offlineStatusService;


    public void validateLetterOfAcceptance(final LetterOfAcceptanceRequest letterOfAcceptanceRequest) {
        DetailedEstimate detailedEstimate = null;
        OfflineStatus offlineStatus = null;
        HashMap<String, String> messages = new HashMap<>();
        for (LetterOfAcceptance letterOfAcceptance : letterOfAcceptanceRequest.getLetterOfAcceptances()) {

            for (LetterOfAcceptanceEstimate letterOfAcceptanceEstimate : letterOfAcceptance.getLetterOfAcceptanceEstimates()) {
                List<DetailedEstimate> detailedEstimates = estimateService.getDetailedEstimate(
                        letterOfAcceptanceEstimate.getDetailedEstimate().getEstimateNumber(),
                        letterOfAcceptanceEstimate.getTenantId(), letterOfAcceptanceRequest.getRequestInfo()).getDetailedEstimates();

                if (!detailedEstimates.isEmpty())
                    detailedEstimate = detailedEstimates.get(0);
                
                validateDetailedEstimate(detailedEstimate, messages);
                
                if (messages != null && !messages.isEmpty())
                    throw new CustomException(messages);
                
                List<OfflineStatus> offlineStatuses = offlineStatusService.getOfflineStatus(letterOfAcceptanceEstimate.getDetailedEstimate().getEstimateNumber(), letterOfAcceptance.getTenantId(), letterOfAcceptanceRequest.getRequestInfo()).getOfflineStatuses();
                if (!offlineStatuses.isEmpty())
                    offlineStatus = offlineStatuses.get(0);
                
                validateOfflineStatus(offlineStatus, messages);
                
                if (messages != null && !messages.isEmpty())
                    throw new CustomException(messages);
            }

            validateLOA(offlineStatus, messages, letterOfAcceptance);

            if (messages != null && !messages.isEmpty())
                throw new CustomException(messages);

        }

    }


    private void validateLOA(OfflineStatus offlineStatus, HashMap<String, String> messages,
                             LetterOfAcceptance letterOfAcceptance) {
        if (letterOfAcceptance.getLoaDate() > new Date().getTime()) {
            messages.put(Constants.KEY_FUTUREDATE_LOADATE, Constants.MESSAGE_FUTUREDATE_LOADATE);
        }

        if (offlineStatus != null && letterOfAcceptance.getLoaDate() > offlineStatus.getStatusDate())
            messages.put(Constants.KEY_FUTUREDATE_LOADATE_OFFLINESTATUS, Constants.MESSAGE_FUTUREDATE_LOADATE_OFFLINESTATUS);

        if (letterOfAcceptance.getFileDate() > new Date().getTime()) {
            messages.put(Constants.KEY_FUTUREDATE_FILEDATE, Constants.MESSAGE_FUTUREDATE_FILEDATE);
        }
    }


    private void validateDetailedEstimate(DetailedEstimate detailedEstimate,
                                          HashMap<String, String> messages) {
    	
        if (detailedEstimate == null)
            messages.put(Constants.KEY_DETAILEDESTIMATE_EXIST, Constants.MESSAGE_DETAILEDESTIMATE_EXIST);
        
        if (detailedEstimate != null && !detailedEstimate.getStatus().toString().equalsIgnoreCase(DetailedEstimateStatus.TECHNICAL_SANCTIONED.toString())) {
            messages.put(Constants.KEY_DETAILEDESTIMATE_STATUS, Constants.MESSAGE_DETAILEDESTIMATE_STATUS);
        }
    }


	private void validateOfflineStatus(OfflineStatus offlineStatus, HashMap<String, String> messages) {
		if (offlineStatus == null) {
            messages.put(Constants.KEY_DETAILEDESTIMATE_OFFLINE_STATUS, Constants.MESSAGE_DETAILEDESTIMATE_OFFLINE_STATUS);
        }
	}

}
