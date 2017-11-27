package org.egov.works.workorder.domain.validator;

import org.egov.tracer.model.CustomException;
import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.domain.service.LetterOfAcceptanceService;
import org.egov.works.workorder.domain.service.OfflineStatusService;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ritesh on 27/11/17.
 */
@Service
public class WorkOrderValidator {

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private OfflineStatusService offlineStatusService;

    public void validateWorkOrder(final WorkOrderRequest workOrderRequest) {
        LetterOfAcceptance letterOfAcceptance = new LetterOfAcceptance();
        HashMap<String, String> messages =  new HashMap<>();
        OfflineStatus offlineStatus = null;
        for (WorkOrder workOrder : workOrderRequest.getWorkOrders()) {
            letterOfAcceptance = new LetterOfAcceptance();
            LetterOfAcceptanceResponse letterOfAcceptanceResponse = getLetterOfAcceptanceResponse(workOrderRequest, workOrder);

            validateLOA(messages, letterOfAcceptanceResponse);

            List<OfflineStatus> offlineStatuses = offlineStatusService
                    .getOfflineStatus(workOrder.getLetterOfAcceptance().getLoaNumber(),
                            letterOfAcceptance.getTenantId(), workOrderRequest.getRequestInfo())
                    .getOfflineStatuses();
            if (!offlineStatuses.isEmpty())
                offlineStatus = offlineStatuses.get(0);

            validateOfflineStatus(offlineStatus,messages);
            validateWorkOrder(letterOfAcceptance, messages, workOrder, letterOfAcceptanceResponse);

            if(messages != null && !messages.isEmpty())
                throw new CustomException(messages);


        }
    }

    public LetterOfAcceptanceResponse getLetterOfAcceptanceResponse(WorkOrderRequest workOrderRequest, WorkOrder workOrder) {
        LetterOfAcceptanceSearchContract letterOfAcceptanceSearchCriteria = new LetterOfAcceptanceSearchContract();
        if (workOrder.getLetterOfAcceptance() != null && workOrder.getLetterOfAcceptance().getLoaNumber() != null)
            letterOfAcceptanceSearchCriteria.setLoaNumbers(Arrays.asList(workOrder.getLetterOfAcceptance().getLoaNumber()));
        letterOfAcceptanceSearchCriteria.setTenantId(workOrder.getTenantId());
        letterOfAcceptanceSearchCriteria.setIds(Arrays.asList(workOrder.getLetterOfAcceptance().getId()));
        letterOfAcceptanceSearchCriteria.setStatuses(Arrays.asList(LOAStatus.APPROVED.toString()));
        return letterOfAcceptanceService.search(letterOfAcceptanceSearchCriteria, workOrderRequest.getRequestInfo());
    }

    private void validateLOA(HashMap<String, String> messages, LetterOfAcceptanceResponse letterOfAcceptanceResponse) {
        if(letterOfAcceptanceResponse.getLetterOfAcceptances().isEmpty()) {
            messages.put(Constants.KEY_INVALID_LOANUMBER,Constants.MESSAGE_INVALID_LOANUMBER);
        }
    }

    private void validateWorkOrder(LetterOfAcceptance letterOfAcceptance, HashMap<String, String> messages, WorkOrder workOrder, LetterOfAcceptanceResponse letterOfAcceptanceResponse) {
        if(letterOfAcceptanceResponse.getLetterOfAcceptances() != null && !letterOfAcceptanceResponse.getLetterOfAcceptances().isEmpty())
            letterOfAcceptance = letterOfAcceptanceResponse.getLetterOfAcceptances().get(0);

        if(letterOfAcceptance.getLoaDate() > workOrder.getWorkOrderDate())
            messages.put(Constants.KEY_INVALID_WORKORDERDATE,Constants.MESSAGE_INVALID_WORKORDERDATE);
    }

    private void validateOfflineStatus(OfflineStatus offlineStatus, HashMap<String, String> messages) {
        if (offlineStatus == null) {
            messages.put(Constants.KEY_LOA_OFFLINE_STATUS,
                    Constants.MESSAGE_LOA_OFFLINE_STATUS);
        }
    }
}
