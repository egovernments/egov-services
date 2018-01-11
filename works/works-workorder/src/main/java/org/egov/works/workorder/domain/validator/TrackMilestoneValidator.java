package org.egov.works.workorder.domain.validator;

import org.egov.works.workorder.config.Constants;
import org.egov.works.workorder.domain.repository.MilestoneRepository;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramki on 26/12/17.
 */
@Service
public class TrackMilestoneValidator {
    @Autowired
    private MilestoneRepository milestoneRepository;

    public void validate(TrackMilestoneRequest trackMilestoneRequest, Boolean isUpdate) {
        Map<String, String> validationMessages = new HashMap<>();
        for (TrackMilestone trackMilestone : trackMilestoneRequest.getTrackMilestones()) {
            if (trackMilestone.getMilestone().getId() != null && !trackMilestone.getMilestone().getId().isEmpty()) {
                if (milestoneRepository.search(MilestoneSearchContract.builder().tenantId(trackMilestone.getTenantId()).ids(Arrays.asList(trackMilestone.getMilestone().getId())).build(), trackMilestoneRequest.getRequestInfo()).isEmpty())
                    validationMessages.put(Constants.KEY_TRACKMILESTONE_MILESTONEID_INVALID, Constants.MESSAGE_TRACKMILESTONE_MILESTONEID_INVALID + trackMilestone.getMilestone().getId());
            } else {
                validationMessages.put(Constants.KEY_TRACKMILESTONE_MILESTONEID_IS_MANDATORY, Constants.MESSAGE_TRACKMILESTONE_MILESTONEID_IS_MANDATORY);
            }
            if (trackMilestone.getStatus().getCode().equals(Constants.STATUS_TRACKMILESTONE_COMPLETED)) {
                if (trackMilestone.getTotalPercentage().compareTo(new BigDecimal(100)) == -1)
                    validationMessages.put(Constants.KEY_TRACKMILESTONE_COMPLETED, Constants.MESSAGE_TRACKMILESTONE_COMPLETED);
            } else if (trackMilestone.getStatus().getCode().equals(Constants.STATUS_TRACKMILESTONE_NOT_YET_STARTED)) {
                if (trackMilestone.getTotalPercentage().compareTo(BigDecimal.ZERO) == 1)
                    validationMessages.put(Constants.KEY_TRACKMILESTONE_NOT_STARTED, Constants.MESSAGE_TRACKMILESTONE_NOT_STARTED);
            } else if (trackMilestone.getStatus().getCode().equals(Constants.STATUS_TRACKMILESTONE_IN_PROGRESS)) {
                if (!(trackMilestone.getTotalPercentage().compareTo(BigDecimal.ZERO) == 1 && trackMilestone.getTotalPercentage().compareTo(new BigDecimal(100)) == -1))
                    validationMessages.put(Constants.KEY_TRACKMILESTONE_IN_PROGRESS, Constants.MESSAGE_TRACKMILESTONE_IN_PROGRESS);
            }
            validateTrackMilestoneActivities(trackMilestone.getTrackMilestoneActivities(), validationMessages, trackMilestoneRequest.getRequestInfo());
        }
    }

    private void validateTrackMilestoneActivities(List<TrackMilestoneActivity> trackMilestoneActivities, Map<String, String> validationMessages, RequestInfo requestInfo) {
        Long currentDateInMillis = System.currentTimeMillis();
        for (TrackMilestoneActivity trackMilestoneActivity : trackMilestoneActivities) {
            if (trackMilestoneActivity.getTenantId() == null || trackMilestoneActivity.getTenantId().isEmpty()) {
                validationMessages.put(Constants.KEY_TRACKMILESTONE_TENANTID_IS_MANDATORY, Constants.MESSAGE_TRACKMILESTONE_TENANTID_IS_MANDATORY);
                if (trackMilestoneActivity.getActualStartDate() > currentDateInMillis || trackMilestoneActivity.getActualEndDate() > currentDateInMillis)
                    validationMessages.put(Constants.KEY_TRACKMILESTONE_ACTIVITY_ASD_AED_CANNOTBE_FUTURE, Constants.MESSAGE_TRACKMILESTONE_ACTIVITY_ASD_AED_CANNOTBE_FUTURE);
                if (trackMilestoneActivity.getActualStartDate() > trackMilestoneActivity.getActualEndDate())
                    validationMessages.put(Constants.KEY_TRACKMILESTONE_ACTIVITY_AED_CANNOT_BEFORE_ASD, Constants.MESSAGE_TRACKMILESTONE_ACTIVITY_AED_CANNOT_BEFORE_ASD);
                if (trackMilestoneActivity.getMilestoneActivity().getId() != null && !trackMilestoneActivity.getMilestoneActivity().getId().isEmpty()) {
                    validationMessages.put(Constants.KEY_TRACKMILESTONE_MILESTONEACTIVITYID_IS_MANDATORY, Constants.MESSAGE_TRACKMILESTONE_MILESTONEACTIVITYID_IS_MANDATORY);
                } else {
                    if (milestoneRepository.getActivityById(trackMilestoneActivity.getMilestoneActivity().getId(), trackMilestoneActivity.getTenantId()) != null)
                        validationMessages.put(Constants.KEY_TRACKMILESTONE_MILESTONEACTIVITYID_INVALID, Constants.MESSAGE_TRACKMILESTONE_MILESTONEACTIVITYID_INVALID + trackMilestoneActivity.getMilestoneActivity().getId());
                }
            }
        }
    }
}
