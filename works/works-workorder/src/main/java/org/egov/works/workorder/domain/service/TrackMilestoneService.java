package org.egov.works.workorder.domain.service;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.workorder.config.PropertiesManager;
import org.egov.works.workorder.domain.repository.TrackMilestoneRepository;
import org.egov.works.workorder.domain.validator.TrackMilestoneValidator;
import org.egov.works.workorder.utils.WorkOrderUtils;
import org.egov.works.workorder.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ramki on 26/12/17.
 */
@Service
public class TrackMilestoneService {
    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private WorkOrderUtils workOrderUtils;
    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private PropertiesManager propertiesManager;
    @Autowired
    private TrackMilestoneValidator trackMilestoneValidator;
    @Autowired
    private TrackMilestoneRepository trackMilestoneRepository;

    public TrackMilestoneResponse create(final TrackMilestoneRequest trackMilestoneRequest) {
        trackMilestoneValidator.validate(trackMilestoneRequest, false);
        for (TrackMilestone trackMilestone : trackMilestoneRequest.getTrackMilestones()) {
            trackMilestone.setId(commonUtils.getUUID());
            trackMilestone.setAuditDetails(workOrderUtils.setAuditDetails(trackMilestoneRequest.getRequestInfo(), false));
            for (TrackMilestoneActivity trackMilestoneActivity : trackMilestone.getTrackMilestoneActivities()) {
                trackMilestoneActivity.setId(commonUtils.getUUID());
                trackMilestoneActivity.setAuditDetails(workOrderUtils.setAuditDetails(trackMilestoneRequest.getRequestInfo(), false));
                trackMilestoneActivity.setTrackMilestone(trackMilestone.getId());
            }
        }

        kafkaTemplate.send(propertiesManager.getWorksTrackMilestoneSaveOrUpdateValidatedTopic(), trackMilestoneRequest);

        TrackMilestoneResponse trackMilestoneResponse = new TrackMilestoneResponse();
        trackMilestoneResponse.setTrackMilestones(trackMilestoneRequest.getTrackMilestones());
        trackMilestoneResponse.setResponseInfo(workOrderUtils.getResponseInfo(trackMilestoneRequest.getRequestInfo()));
        return trackMilestoneResponse;
    }

    public TrackMilestoneResponse search(final TrackMilestoneSearchContract trackMilestoneSearchContract, RequestInfo requestInfo) {
        TrackMilestoneResponse trackMilestoneResponse = new TrackMilestoneResponse();
        trackMilestoneResponse.setTrackMilestones(trackMilestoneRepository.search(trackMilestoneSearchContract, requestInfo));
        trackMilestoneResponse.setResponseInfo(workOrderUtils.getResponseInfo(requestInfo));
        return trackMilestoneResponse;
    }

    public TrackMilestoneResponse update(final TrackMilestoneRequest trackMilestoneRequest) {
        trackMilestoneValidator.validate(trackMilestoneRequest, true);
        for (TrackMilestone trackMilestone : trackMilestoneRequest.getTrackMilestones()) {
            trackMilestone.setAuditDetails(workOrderUtils.setAuditDetails(trackMilestoneRequest.getRequestInfo(), true));
            for (TrackMilestoneActivity trackMilestoneActivity : trackMilestone.getTrackMilestoneActivities()) {
                if (trackMilestoneActivity.getId() != null && !trackMilestoneActivity.getId().isEmpty()) {
                    trackMilestoneActivity.setAuditDetails(workOrderUtils.setAuditDetails(trackMilestoneRequest.getRequestInfo(), true));
                } else {
                    trackMilestoneActivity.setId(commonUtils.getUUID());
                    trackMilestoneActivity.setAuditDetails(workOrderUtils.setAuditDetails(trackMilestoneRequest.getRequestInfo(), false));
                    trackMilestoneActivity.setTrackMilestone(trackMilestone.getId());
                }
            }
        }

        kafkaTemplate.send(propertiesManager.getWorksTrackMilestoneSaveOrUpdateValidatedTopic(), trackMilestoneRequest);

        TrackMilestoneResponse trackMilestoneResponse = new TrackMilestoneResponse();
        trackMilestoneResponse.setTrackMilestones(trackMilestoneRequest.getTrackMilestones());
        trackMilestoneResponse.setResponseInfo(workOrderUtils.getResponseInfo(trackMilestoneRequest.getRequestInfo()));
        return trackMilestoneResponse;
    }
}
