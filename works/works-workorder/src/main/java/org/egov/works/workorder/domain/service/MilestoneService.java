package org.egov.works.workorder.domain.service;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.workorder.config.PropertiesManager;
import org.egov.works.workorder.domain.repository.MilestoneRepository;
import org.egov.works.workorder.domain.validator.MilestoneValidator;
import org.egov.works.workorder.utils.WorkOrderUtils;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        for (Milestone milestone : milestoneRequest.getMilestones()) {
            milestone.setId(commonUtils.getUUID());
            milestone.setAuditDetails(workOrderUtils.setAuditDetails(milestoneRequest.getRequestInfo(), false));
            for (MilestoneActivity milestoneActivity : milestone.getMilestoneActivities()) {
                milestoneActivity.setId(commonUtils.getUUID());
                milestoneActivity.setAuditDetails(workOrderUtils.setAuditDetails(milestoneRequest.getRequestInfo(), false));
                milestoneActivity.setMilestone(milestone.getId());
            }
        }

        kafkaTemplate.send(propertiesManager.getWorksMilestoneSaveOrUpdateValidatedTopic(), milestoneRequest);

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
        for (Milestone milestone : milestoneRequest.getMilestones()) {
            milestone.setAuditDetails(workOrderUtils.setAuditDetails(milestoneRequest.getRequestInfo(), true));
            for (MilestoneActivity milestoneActivity : milestone.getMilestoneActivities()) {
                if (milestoneActivity.getId() != null && !milestoneActivity.getId().isEmpty()) {
                    milestoneActivity.setAuditDetails(workOrderUtils.setAuditDetails(milestoneRequest.getRequestInfo(), true));
                } else {
                    milestoneActivity.setId(commonUtils.getUUID());
                    milestoneActivity.setAuditDetails(workOrderUtils.setAuditDetails(milestoneRequest.getRequestInfo(), false));
                    milestoneActivity.setMilestone(milestone.getId());
                }
            }
        }

        kafkaTemplate.send(propertiesManager.getWorksMilestoneSaveOrUpdateValidatedTopic(), milestoneRequest);

        MilestoneResponse milestoneResponse = new MilestoneResponse();
        milestoneResponse.setMilestones(milestoneRequest.getMilestones());
        milestoneResponse.setResponseInfo(workOrderUtils.getResponseInfo(milestoneRequest.getRequestInfo()));
        return milestoneResponse;
    }
}
