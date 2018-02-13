package org.egov.works.workorder.domain.service;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonConstants;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.workorder.config.PropertiesManager;
import org.egov.works.workorder.domain.repository.MilestoneRepository;
import org.egov.works.workorder.domain.validator.MilestoneValidator;
import org.egov.works.workorder.utils.WorkOrderUtils;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramki on 19/12/17.
 */
@Service
public class MilestoneService {
    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private WorkOrderUtils workOrderUtils;
    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private PropertiesManager propertiesManager;
    @Autowired
    private MilestoneValidator milestoneValidator;
    @Autowired
    private MilestoneRepository milestoneRepository;


    public MilestoneResponse create(final MilestoneRequest milestoneRequest) {
        milestoneValidator.validate(milestoneRequest, false);
        
        List<Milestone> milestones = new ArrayList<>();
        for (Milestone milestone : milestoneRequest.getMilestones()) {
            LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = workOrderUtils.searchLoaEstimateById(milestone.getTenantId(), milestone.getLetterOfAcceptanceEstimate(), milestoneRequest.getRequestInfo());
            if(letterOfAcceptanceEstimate != null)
                milestone.setLetterOfAcceptanceEstimate(letterOfAcceptanceEstimate);
            milestone.setId(commonUtils.getUUID());
            milestone.setAuditDetails(workOrderUtils.setAuditDetails(milestoneRequest.getRequestInfo(), false));
            for (MilestoneActivity milestoneActivity : milestone.getMilestoneActivities()) {
                milestoneActivity.setId(commonUtils.getUUID());
                milestoneActivity.setAuditDetails(workOrderUtils.setAuditDetails(milestoneRequest.getRequestInfo(), false));
                milestoneActivity.setMilestone(milestone.getId());
            }
            if(milestone.getStatus() != null && milestone.getStatus().getCode().equals(CommonConstants.STATUS_APPROVED))
                milestones.add(milestone);
        }

        kafkaTemplate.send(propertiesManager.getWorksMilestoneSaveOrUpdateValidatedTopic(), milestoneRequest);
        if(milestones != null && !milestones.isEmpty()) {
            MilestoneRequest backUpdateRequest = new MilestoneRequest();
            backUpdateRequest.setMilestones(milestones);
            kafkaTemplate.send(propertiesManager.getWorksLetterofAcceptanceBackupdateOnCreateMilestoneTopic(), backUpdateRequest);
            kafkaTemplate.send(propertiesManager.getWorksWorkOrderBackupdateOnCreateMilestoneTopic(), backUpdateRequest);
        }

        MilestoneResponse milestoneResponse = new MilestoneResponse();
        milestoneResponse.setMilestones(milestoneRequest.getMilestones());
        milestoneResponse.setResponseInfo(workOrderUtils.getResponseInfo(milestoneRequest.getRequestInfo()));
        return milestoneResponse;
    }


    public MilestoneResponse search(final MilestoneSearchContract milestoneSearchContract, RequestInfo requestInfo) {
        MilestoneResponse milestoneResponse = new MilestoneResponse();
        milestoneResponse.setMilestones(milestoneRepository.search(milestoneSearchContract, requestInfo));
        milestoneResponse.setResponseInfo(workOrderUtils.getResponseInfo(requestInfo));
        return milestoneResponse;
    }

    public MilestoneResponse update(final MilestoneRequest milestoneRequest) {
        milestoneValidator.validate(milestoneRequest, true);
        List<Milestone> milestones = new ArrayList<>();
        for (Milestone milestone : milestoneRequest.getMilestones()) {
            milestone.setAuditDetails(workOrderUtils.setAuditDetails(milestoneRequest.getRequestInfo(), true));
            LetterOfAcceptanceEstimate letterOfAcceptanceEstimate = workOrderUtils.searchLoaEstimateById(milestone.getTenantId(),milestone.getLetterOfAcceptanceEstimate(), milestoneRequest.getRequestInfo());
            if(letterOfAcceptanceEstimate != null)
                milestone.setLetterOfAcceptanceEstimate(letterOfAcceptanceEstimate);
            for (MilestoneActivity milestoneActivity : milestone.getMilestoneActivities()) {
                if (milestoneActivity.getId() != null && !milestoneActivity.getId().isEmpty()) {
                    milestoneActivity.setAuditDetails(workOrderUtils.setAuditDetails(milestoneRequest.getRequestInfo(), true));
                } else {
                    milestoneActivity.setId(commonUtils.getUUID());
                    milestoneActivity.setAuditDetails(workOrderUtils.setAuditDetails(milestoneRequest.getRequestInfo(), false));
                    milestoneActivity.setMilestone(milestone.getId());
                }
            }

            if(milestone.getStatus() != null && milestone.getStatus().getCode().equals(CommonConstants.STATUS_CANCELLED))
                milestones.add(milestone);
        }

        kafkaTemplate.send(propertiesManager.getWorksMilestoneSaveOrUpdateValidatedTopic(), milestoneRequest);
        if(milestones != null && !milestones.isEmpty()) {
            MilestoneRequest backUpdateRequest = new MilestoneRequest();
            backUpdateRequest.setMilestones(milestones);
            kafkaTemplate.send(propertiesManager.getWorksLetterofAcceptanceBackupdateOnCancelMilestoneTopic(), backUpdateRequest);
            kafkaTemplate.send(propertiesManager.getWorksWorkOrderBackupdateOnCancelMilestoneTopic(), backUpdateRequest);
        }
        MilestoneResponse milestoneResponse = new MilestoneResponse();
        milestoneResponse.setMilestones(milestoneRequest.getMilestones());
        milestoneResponse.setResponseInfo(workOrderUtils.getResponseInfo(milestoneRequest.getRequestInfo()));
        return milestoneResponse;
    }
}
