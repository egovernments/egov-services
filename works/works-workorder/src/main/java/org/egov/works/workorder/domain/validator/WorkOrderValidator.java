package org.egov.works.workorder.domain.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.domain.repository.WorkOrderRepository;
import org.egov.works.workorder.domain.repository.WorksMastersRepository;
import org.egov.works.workorder.domain.service.LetterOfAcceptanceService;
import org.egov.works.workorder.domain.service.OfflineStatusService;
import org.egov.works.workorder.utils.WorkOrderUtils;
import org.egov.works.workorder.web.contract.*;
import org.egov.works.workorder.web.repository.MdmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

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

    @Autowired
    private WorkOrderUtils workOrderUtils;

    @Autowired
    private WorksMastersRepository worksMastersRepository;

    public void validateWorkOrder(final WorkOrderRequest workOrderRequest, Boolean isUpdate) {
        HashMap<String, String> messages = new HashMap<>();
        for (WorkOrder workOrder : workOrderRequest.getWorkOrders()) {

            if (workOrder.getLetterOfAcceptance() != null && workOrder.getLetterOfAcceptance().getSpillOverFlag()) {
                checkWorkOrderNumberExist(workOrderRequest, messages, workOrder, isUpdate);
            }
            if (isUpdate) {
                validateWorkOrderExist(workOrderRequest, messages, workOrder);
                validateRemarksData(workOrderRequest, messages, workOrder);
            }
            if (!isUpdate) {
                validateWorkOrderCreated(workOrderRequest, messages, workOrder);
                validateWorkOrderDetail(workOrderRequest, messages, workOrder);
            }
            LetterOfAcceptanceResponse letterOfAcceptanceResponse = getLetterOfAcceptanceResponse(workOrderRequest, workOrder);
            validateLOA(messages, letterOfAcceptanceResponse);
            if (workOrder.getLetterOfAcceptance() != null && !workOrder.getLetterOfAcceptance().getSpillOverFlag())
                validateOfflineStatus(workOrderRequest, messages, workOrder);
            validateWorkOrder(messages, workOrder, letterOfAcceptanceResponse);
            validateStatus(workOrder, messages, workOrderRequest.getRequestInfo());
            //TODO : FIX remarks master topic
           // validateRemarks(workOrder, messages, workOrderRequest.getRequestInfo());
            if (!messages.isEmpty())
                throw new CustomException(messages);

        }
    }

    private void validateRemarks(WorkOrder workOrder, HashMap<String, String> messages, RequestInfo requestInfo) {
        for(WorkOrderDetail workOrderDetail : workOrder.getWorkOrderDetails()) {
            if(StringUtils.isNotBlank(workOrderDetail.getRemarks())) {
                List<Remarks> remarks = worksMastersRepository.SearchRemarks(workOrder.getTenantId(),workOrderDetail.getRemarks(), requestInfo);
                if(remarks != null && remarks.isEmpty())
                    messages.put(Constants.KEY_WORKORDER_REMARKS_INVALID,
                            Constants.MESSAGE_WORKORDER_REMARKS_INVALID);

            }
        }
    }

    private void validateStatus(WorkOrder workOrder, HashMap<String, String> messages, RequestInfo requestInfo) {
        if(workOrder.getStatus() != null && StringUtils.isNotBlank(workOrder.getStatus().getCode())) {
            List<String> filetsNamesList = new ArrayList<>(Arrays.asList(CommonConstants.CODE,CommonConstants.MODULE_TYPE));
            List<String> filetsValuesList = new ArrayList<>(Arrays.asList(workOrder.getStatus().getCode().toUpperCase(), CommonConstants.WORKORDER));
            JSONArray dBStatusArray = workOrderUtils.getMDMSData(CommonConstants.WORKS_STATUS_APPCONFIG, filetsNamesList,
                    filetsValuesList, workOrder.getTenantId(), requestInfo,
                    CommonConstants.MODULENAME_WORKS);
            if(dBStatusArray != null && dBStatusArray.isEmpty())
                messages.put(Constants.KEY_WORKORDER_STATUS_INVALID,
                        Constants.MESSAGE_WORKORDER_STATUS_INVALID);
        } else
            messages.put(Constants.KEY_WORKORDER_STATUS_REQUIRED,
                    Constants.MESSAGE_WORKORDER_STATUS_REQUIRED);
    }

    private void validateWorkOrderDetail(final WorkOrderRequest workOrderRequest, HashMap<String, String> messages,
            WorkOrder workOrder) {
        Remarks remarks = getRemarks(workOrder.getTenantId(), "Remarks", workOrderRequest.getRequestInfo());
        // prepair false work order details
        List<RemarksDetail> remarksDetails = new ArrayList<>();
        List<WorkOrderDetail> passedWorkOrderDetail = new ArrayList<>();
        
        if (remarks.getRemarksDetails() != null && !remarks.getRemarksDetails().isEmpty())
            for (RemarksDetail remarksDetail : remarks.getRemarksDetails()) {
                if (!remarksDetail.getEditable())
                    remarksDetails.add(remarksDetail);
            }

        if (workOrder.getWorkOrderDetails() != null && !workOrder.getWorkOrderDetails().isEmpty())
            for (WorkOrderDetail workOrderDetail : workOrder.getWorkOrderDetails()) {
                if (!workOrderDetail.getEditable())
                    passedWorkOrderDetail.add(workOrderDetail);
            }

        if (remarksDetails != null && !remarksDetails.isEmpty() && passedWorkOrderDetail != null
                && passedWorkOrderDetail.isEmpty() && remarksDetails.size() == passedWorkOrderDetail.size()) {
            messages.put(Constants.KEY_WORKORDER_REMARKS_INVALID_DATA, Constants.MESSAGE_WORKORDER_REMARKS_INVALID_DATA);
        }
        Boolean flag = false;
        if (remarksDetails != null && !remarksDetails.isEmpty() && passedWorkOrderDetail != null
                && passedWorkOrderDetail.isEmpty())
        for(WorkOrderDetail workOrderDetail: passedWorkOrderDetail) {
            flag = false;
            for(RemarksDetail remarksDetail: remarksDetails) {
                if(remarksDetail.getRemarksDescription().equalsIgnoreCase(workOrderDetail.getRemarks())) {
                    flag = true;
                }
            }
            if(!flag) {
                messages.put(Constants.KEY_WORKORDER_REMARKS_EDITABLE, Constants.MESSAGE_WORKORDER_REMARKS_EDITABLE);
                break;
            }
        }
    }

    private void validateRemarksData(final WorkOrderRequest workOrderRequest, HashMap<String, String> messages,
            WorkOrder workOrder) {
        WorkOrderSearchContract workOrderSearchContract = new WorkOrderSearchContract();
        workOrderSearchContract.setIds(Arrays.asList(workOrder.getId()));
        workOrderSearchContract.setTenantId(workOrder.getTenantId());
        List<WorkOrder> workOrders = workOrderRepository.search(workOrderSearchContract, workOrderRequest.getRequestInfo());

        for (WorkOrderDetail savedOrderDetails : workOrders.get(0).getWorkOrderDetails()) {
            for (WorkOrderDetail workOrderDetails : workOrder.getWorkOrderDetails()) {
                if (savedOrderDetails.getId().equalsIgnoreCase(workOrderDetails.getId()) && !savedOrderDetails.getEditable()
                        && !savedOrderDetails.getRemarks().equalsIgnoreCase(workOrderDetails.getRemarks())) {
                    messages.put(Constants.KEY_WORKORDER_REMARKS_EDITABLE,
                            Constants.MESSAGE_WORKORDER_REMARKS_EDITABLE);
                }
            }
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

        if (offlineStatus != null && offlineStatus.getStatusDate() > workOrder.getWorkOrderDate()) {
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
                if (!workOrder2.getStatus().getCode().equalsIgnoreCase(CommonConstants.STATUS_CANCELLED)) {
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
            if (!workOrder2.getStatus().getCode().equalsIgnoreCase(CommonConstants.STATUS_CANCELLED)) {
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
        letterOfAcceptanceSearchCriteria.setStatuses(Arrays.asList(CommonConstants.STATUS_APPROVED));
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

    private Remarks getRemarks(final String tenantId, final String filterFieldValue, final RequestInfo requestInfo) {

        ObjectMapper objectMapper = new ObjectMapper();
        Remarks remarks = new Remarks();
        JSONArray responseJSONArray;
        responseJSONArray = mdmsRepository.getByCriteria(tenantId,
                CommonConstants.MODULENAME_WORKS, CommonConstants.REMARKS_OBJECTNAME, "typeOfDocument", "Work Order / Notice",
                requestInfo);
        if (responseJSONArray != null && !responseJSONArray.isEmpty()) {
            remarks = objectMapper.convertValue(responseJSONArray.get(0), Remarks.class);
        }
        return remarks;
    }

}
