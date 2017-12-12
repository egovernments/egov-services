package org.egov.works.workorder.domain.validator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.web.contract.Remarks;
import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.domain.repository.WorkOrderRepository;
import org.egov.works.workorder.domain.service.LetterOfAcceptanceService;
import org.egov.works.workorder.domain.service.OfflineStatusService;
import org.egov.works.workorder.web.contract.LOAStatus;
import org.egov.works.workorder.web.contract.LetterOfAcceptance;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceResponse;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceSearchContract;
import org.egov.works.workorder.web.contract.OfflineStatus;
import org.egov.works.workorder.web.contract.WorkOrder;
import org.egov.works.workorder.web.contract.WorkOrderRequest;
import org.egov.works.workorder.web.contract.WorkOrderSearchContract;
import org.egov.works.workorder.web.contract.WorkOrderStatus;
import org.egov.works.workorder.web.repository.MdmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
    @Autowired
    private MdmsRepository mdmsRepository;

    public void validateWorkOrder(final WorkOrderRequest workOrderRequest, Boolean isUpdate) {
        HashMap<String, String> messages = new HashMap<>();
        for (WorkOrder workOrder : workOrderRequest.getWorkOrders()) {
            if (workOrder.getLetterOfAcceptance() != null && workOrder.getLetterOfAcceptance().getSpillOverFlag()) {
                checkWorkOrderNumberExist(workOrderRequest, messages, workOrder, isUpdate);
            }
            if (isUpdate) {
                validateWorkOrderExist(workOrderRequest, messages, workOrder);
            }
            if (!isUpdate)
                validateWorkOrderCreated(workOrderRequest, messages, workOrder);
            LetterOfAcceptanceResponse letterOfAcceptanceResponse = getLetterOfAcceptanceResponse(workOrderRequest, workOrder);
            validateLOA(messages, letterOfAcceptanceResponse);
            if (workOrder.getLetterOfAcceptance() != null && !workOrder.getLetterOfAcceptance().getSpillOverFlag())
                validateOfflineStatus(workOrderRequest, messages, workOrder);
            validateWorkOrder(messages, workOrder, letterOfAcceptanceResponse);
            if (!messages.isEmpty())
                throw new CustomException(messages);

        }
    }

    private void validateOfflineStatus(final WorkOrderRequest workOrderRequest, HashMap<String, String> messages,
            WorkOrder workOrder) {
        OfflineStatus offlineStatus = null;
        List<OfflineStatus> offlineStatuses = offlineStatusService
                .getOfflineStatusForWorkOrder(workOrder.getLetterOfAcceptance().getLoaNumber(),
                        workOrder.getTenantId(), workOrderRequest.getRequestInfo())
                .getOfflineStatuses();
        if (!offlineStatuses.isEmpty())
            offlineStatus = offlineStatuses.get(0);

        if (offlineStatus == null) {
            messages.put(Constants.KEY_LOA_OFFLINE_STATUS,
                    Constants.MESSAGE_LOA_OFFLINE_STATUS);
        }
        
        if(offlineStatus != null && offlineStatus.getStatusDate() > workOrder.getWorkOrderDate()) {
            messages.put(Constants.KEY_OFFLINESTATUS_WORKORDERDATE_INVALID,
                    Constants.MESSAGE_OFFLINESTATUS_WORKORDERDATE_INVALID);
        }
    }

    private void checkWorkOrderNumberExist(WorkOrderRequest workOrderRequest, HashMap<String, String> messages,
            WorkOrder workOrder, Boolean isUpdate) {
        WorkOrderSearchContract workOrderSearchContract = new WorkOrderSearchContract();
        workOrderSearchContract.setWorkOrderNumbers(Arrays.asList(workOrder.getWorkOrderNumber()));
        workOrderSearchContract.setTenantId(workOrder.getTenantId());
        if (workOrder.getId() != null)
            workOrderSearchContract.setIds(Arrays.asList(workOrder.getId()));
        List<WorkOrder> workOrders = workOrderRepository.search(workOrderSearchContract, workOrderRequest.getRequestInfo());

        if (!isUpdate)
            for (WorkOrder workOrder2 : workOrders) {
                if (!workOrder2.getStatus().toString().equalsIgnoreCase(WorkOrderStatus.CANCELLED.toString())) {
                    messages.put(Constants.KEY_INVALID_WORKORDER_EXISTS, Constants.MESSAGE_INVALID_WORKORDER_EXISTS);
                    break;
                }

            }

        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);
    }

    private void validateWorkOrderExist(WorkOrderRequest workOrderRequest, HashMap<String, String> messages,
            WorkOrder workOrder) {
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

    private void validateWorkOrderCreated(WorkOrderRequest workOrderRequest, HashMap<String, String> messages,
            WorkOrder workOrder) {
        WorkOrderSearchContract workOrderSearchContract = new WorkOrderSearchContract();
        workOrderSearchContract.setLoaNumbers(Arrays.asList(workOrder.getLetterOfAcceptance().getLoaNumber()));
        workOrderSearchContract.setTenantId(workOrder.getTenantId());
        workOrderSearchContract.setIds(Arrays.asList(workOrder.getId()));

        List<WorkOrder> workOrders = workOrderRepository.search(workOrderSearchContract, workOrderRequest.getRequestInfo());

        for (WorkOrder workOrder2 : workOrders) {
            if (!workOrder2.getStatus().toString().equalsIgnoreCase(WorkOrderStatus.CANCELLED.toString())) {
                messages.put(Constants.KEY_INVALID_LOA_WORKORDER, Constants.MESSAGE_INVALID_LOA_WORKORDER);
                break;
            }

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

    private void validateWorkOrder(HashMap<String, String> messages, WorkOrder workOrder,
            LetterOfAcceptanceResponse letterOfAcceptanceResponse) {
        LetterOfAcceptance letterOfAcceptance = new LetterOfAcceptance();
        if (letterOfAcceptanceResponse.getLetterOfAcceptances() != null
                && !letterOfAcceptanceResponse.getLetterOfAcceptances().isEmpty())
            letterOfAcceptance = letterOfAcceptanceResponse.getLetterOfAcceptances().get(0);

        if (messages != null && !messages.isEmpty())
            throw new CustomException(messages);

        if (workOrder.getWorkOrderDate() != null && letterOfAcceptance.getLoaDate() != null
                && letterOfAcceptance.getLoaDate() > workOrder.getWorkOrderDate())
            messages.put(Constants.KEY_INVALID_WORKORDERDATE, Constants.MESSAGE_INVALID_WORKORDERDATE);

        if (workOrder.getLetterOfAcceptance().getSpillOverFlag()) {
            if (StringUtils.isBlank(workOrder.getWorkOrderNumber()))
                messages.put(Constants.KEY_WORKORDER_WORKORDERNUMBER_REQUIRED,
                        Constants.MESSAGE_WORKORDER_WORKORDERNUMBER_REQUIRED);
        }
        
        

    }

}
