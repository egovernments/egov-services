package org.egov.works.workorder.domain.validator;

import org.egov.tracer.model.CustomException;
import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.domain.repository.LetterOfAcceptanceEstimateRepository;
import org.egov.works.workorder.domain.repository.LetterOfAcceptanceRepository;
import org.egov.works.workorder.domain.repository.WorkOrderRepository;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramki on 21/12/17.
 */
@Service
public class MilestoneValidator {

    @Autowired
    private LetterOfAcceptanceEstimateRepository letterOfAcceptanceEstimateRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private LetterOfAcceptanceRepository letterOfAcceptanceRepository;

    public void validate(MilestoneRequest milestoneRequest, Boolean isUpdate) {
        Map<String, String> validationMessages = new HashMap<>();
        WorkOrder workOrder = null;
        for (Milestone milestone : milestoneRequest.getMilestones()) {
            if (milestone.getLetterOfAcceptanceEstimate().getId() != null && !milestone.getLetterOfAcceptanceEstimate().getId().isEmpty()) {
                List<LetterOfAcceptanceEstimate> loaes = letterOfAcceptanceEstimateRepository.searchLOAs(LetterOfAcceptanceEstimateSearchContract.
                                builder().tenantId(milestone.getTenantId()).ids(Arrays.asList(milestone.getLetterOfAcceptanceEstimate().getId())).build(),
                        milestoneRequest.getRequestInfo());
                if (loaes != null && loaes.size() > 0) {
                    LetterOfAcceptance letterOfAcceptance = letterOfAcceptanceRepository.searchLOAs(LetterOfAcceptanceSearchContract.builder().tenantId(milestone.getTenantId()).ids(Arrays.asList(loaes.get(0).getLetterOfAcceptance())).build(), milestoneRequest.getRequestInfo()).get(0);
                    workOrder = workOrderRepository.search(WorkOrderSearchContract.builder().tenantId(milestone.getTenantId()).letterOfAcceptances(Arrays.asList(letterOfAcceptance.getId())).build(), milestoneRequest.getRequestInfo()).get(0);
                    if (!workOrder.getStatus().getCode().equals(WorkOrderStatus.APPROVED.toString()))
                        validationMessages.put(Constants.KEY_MILESTONE_WORKORDER_SHOULD_BE_APPROVED_STATE, Constants.MESSAGE_MILESTONE_WORKORDER_SHOULD_BE_APPROVED_STATE);
                } else {
                    validationMessages.put(Constants.KEY_MILESTONE_LOA_ESTIMATE_ID_INVALID, Constants.MESSAGE_MILESTONE_LOA_ESTIMATE_ID_INVALID + milestone.getLetterOfAcceptanceEstimate().getId());
                }
            } else
                validationMessages.put(Constants.KEY_MILESTONE_LOA_ESTIMATE_ID_REQUIRED, Constants.MESSAGE_MILESTONE_LOA_ESTIMATE_ID_REQUIRED);
            validateActivities(milestone.getMilestoneActivities(), validationMessages, workOrder, isUpdate);
        }
        if (validationMessages.size() > 0) throw new CustomException(validationMessages);
    }

    private void validateActivities(List<MilestoneActivity> milestoneActivities, Map<String, String> validationMessages, WorkOrder workOrder, Boolean isUpdate) {
        BigDecimal totalPercentage = BigDecimal.ZERO;
        Long currentDateInMillis = System.currentTimeMillis();
        for (MilestoneActivity milestoneActivity : milestoneActivities) {
            totalPercentage.add(milestoneActivity.getPercentage());
            if (milestoneActivity.getScheduleStartDate() > currentDateInMillis || (milestoneActivity.getScheduleEndDate() != null && milestoneActivity.getScheduleEndDate() > currentDateInMillis))
                validationMessages.put(Constants.KEY_MILESTONE_ACTIVITY_SSD_SED_CANNOTBE_FUTURE, Constants.MESSAGE_MILESTONE_ACTIVITY_SSD_SED_CANNOTBE_FUTURE);
            if (milestoneActivity.getScheduleEndDate() != null && milestoneActivity.getScheduleStartDate() > milestoneActivity.getScheduleEndDate())
                validationMessages.put(Constants.KEY_MILESTONE_ACTIVITY_SED_CANNOT_BEFORE_SSD, Constants.MESSAGE_MILESTONE_ACTIVITY_SED_CANNOT_BEFORE_SSD);
            if (milestoneActivity.getScheduleStartDate() < workOrder.getWorkOrderDate())
                validationMessages.put(Constants.KEY_MILESTONE_ACTIVITY_SSD_CANNOT_BEFORE_WOD, Constants.MESSAGE_MILESTONE_ACTIVITY_SSD_CANNOT_BEFORE_WOD);
        }
        if (totalPercentage.compareTo(new BigDecimal(100)) == 1)
            validationMessages.put(Constants.KEY_MILESTONE_ACTIVITY_TOTALPERCENTAGE_SHOULDNOT_CROSS100, Constants.MESSAGE_MILESTONE_ACTIVITY_TOTALPERCENTAGE_SHOULDNOT_CROSS100);
    }
}
