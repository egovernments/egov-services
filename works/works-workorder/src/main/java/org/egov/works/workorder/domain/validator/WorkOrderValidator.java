package org.egov.works.workorder.domain.validator;

import org.egov.tracer.model.CustomException;
import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.domain.repository.WorkOrderRepository;
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

    @Autowired
    private WorkOrderRepository workOrderRepository;

    public void validateWorkOrder(final WorkOrderRequest workOrderRequest, Boolean isUpdate) {
        LetterOfAcceptance letterOfAcceptance = new LetterOfAcceptance();
        HashMap<String, String> messages = new HashMap<>();
        OfflineStatus offlineStatus = null;
        for (WorkOrder workOrder : workOrderRequest.getWorkOrders()) {

            if (workOrder.getLetterOfAcceptance() != null && workOrder.getLetterOfAcceptance().getSpillOverFlag()) {
                checkWorkOrderNumberExist(workOrderRequest, messages, workOrder, isUpdate);
            }
            if (isUpdate) {
                validateWorkOrderExist(workOrderRequest, messages, workOrder);
            }


            letterOfAcceptance = new LetterOfAcceptance();
            LetterOfAcceptanceResponse letterOfAcceptanceResponse = getLetterOfAcceptanceResponse(workOrderRequest, workOrder);

            validateLOA(messages, letterOfAcceptanceResponse);

            List<OfflineStatus> offlineStatuses = offlineStatusService
                    .getOfflineStatus(workOrder.getLetterOfAcceptance().getLoaNumber(),
                            letterOfAcceptance.getTenantId(), workOrderRequest.getRequestInfo())
                    .getOfflineStatuses();
            if (!offlineStatuses.isEmpty())
                offlineStatus = offlineStatuses.get(0);

            validateOfflineStatus(offlineStatus, messages);
            validateWorkOrder(letterOfAcceptance, messages, workOrder, letterOfAcceptanceResponse);

            if (messages != null && !messages.isEmpty())
                throw new CustomException(messages);


        }
    }

    private void checkWorkOrderNumberExist(WorkOrderRequest workOrderRequest, HashMap<String, String> messages, WorkOrder workOrder, Boolean isUpdate) {
        WorkOrderSearchContract workOrderSearchContract = new WorkOrderSearchContract();
        workOrderSearchContract.setWorkOrderNumbers(Arrays.asList(workOrder.getWorkOrderNumber()));
        workOrderSearchContract.setTenantId(workOrder.getTenantId());
        if (workOrder.getId() != null)
            workOrderSearchContract.setIds(Arrays.asList(workOrder.getId()));
        List<WorkOrder> workOrders = workOrderRepository.search(workOrderSearchContract, workOrderRequest.getRequestInfo());

        if (!isUpdate && !workOrders.isEmpty()) {
            messages.put(Constants.KEY_INVALID_WORKORDER_EXISTS, Constants.MESSAGE_INVALID_WORKORDER_EXISTS);
        }

        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
    }

    private void validateWorkOrderExist(WorkOrderRequest workOrderRequest, HashMap<String, String> messages, WorkOrder workOrder) {
        WorkOrderSearchContract workOrderSearchContract = new WorkOrderSearchContract();
        workOrderSearchContract.setWorkOrderNumbers(Arrays.asList(workOrder.getWorkOrderNumber()));
        workOrderSearchContract.setTenantId(workOrder.getTenantId());
        workOrderSearchContract.setIds(Arrays.asList(workOrder.getId()));

        List<WorkOrder> workOrders = workOrderRepository.search(workOrderSearchContract, workOrderRequest.getRequestInfo());

        if (workOrders.isEmpty()) {
            messages.put(Constants.KEY_INVALID_WORKORDER, Constants.MESSAGE_INVALID_WORKORDER);
        }

        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
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
        if (letterOfAcceptanceResponse.getLetterOfAcceptances().isEmpty()) {
            messages.put(Constants.KEY_INVALID_LOANUMBER, Constants.MESSAGE_INVALID_LOANUMBER);
        }
    }

    private void validateWorkOrder(LetterOfAcceptance letterOfAcceptance, HashMap<String, String> messages, WorkOrder workOrder, LetterOfAcceptanceResponse letterOfAcceptanceResponse) {
        if (letterOfAcceptanceResponse.getLetterOfAcceptances() != null && !letterOfAcceptanceResponse.getLetterOfAcceptances().isEmpty())
            letterOfAcceptance = letterOfAcceptanceResponse.getLetterOfAcceptances().get(0);

        if (letterOfAcceptance.getLoaDate() > workOrder.getWorkOrderDate())
            messages.put(Constants.KEY_INVALID_WORKORDERDATE, Constants.MESSAGE_INVALID_WORKORDERDATE);

        if (workOrder.getLetterOfAcceptance().getSpillOverFlag()) {
            if (workOrder.getWorkOrderNumber() == null || workOrder.getWorkOrderNumber().isEmpty())
                messages.put(Constants.KEY_WORKORDER_WORKORDERNUMBER_REQUIRED, Constants.MESSAGE_WORKORDER_WORKORDERNUMBER_REQUIRED);
        }

        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
    }

    private void validateOfflineStatus(OfflineStatus offlineStatus, HashMap<String, String> messages) {
        if (offlineStatus == null) {
            messages.put(Constants.KEY_LOA_OFFLINE_STATUS,
                    Constants.MESSAGE_LOA_OFFLINE_STATUS);
        }
    }
}
