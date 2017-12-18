package org.egov.works.masters.domain.service;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.masters.config.PropertiesManager;
import org.egov.works.masters.domain.repository.MilestoneTemplateRepository;
import org.egov.works.masters.domain.validator.MilestoneTemplateValidator;
import org.egov.works.masters.utils.MasterUtils;
import org.egov.works.masters.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ramki on 15/12/17.
 */

@Service
public class MilestoneTemplateService {

    @Autowired
    private MilestoneTemplateRepository milestoneTemplateRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private MasterUtils masterUtils;

    @Autowired
    private MilestoneTemplateValidator milestoneTemplateValidator;


    @Transactional
    public ResponseEntity<?> create(MilestoneTemplateRequest milestoneTemplateRequest) {
        CommonUtils commonUtils = new CommonUtils();
        MilestoneTemplateResponse response = new MilestoneTemplateResponse();

        milestoneTemplateValidator.validate(milestoneTemplateRequest);
        milestoneTemplateValidator.validateForExistance(milestoneTemplateRequest);

        for (final MilestoneTemplate milestoneTemplate : milestoneTemplateRequest.getMilestoneTemplates()) {
            milestoneTemplate.setId(commonUtils.getUUID());
            milestoneTemplate.setAuditDetails(masterUtils.getAuditDetails(milestoneTemplateRequest.getRequestInfo(), false));
            for (final MilestoneTemplateActivities milestoneTemplateActivities : milestoneTemplate.getMilestoneTemplateActivities()) {
                milestoneTemplateActivities.setId(commonUtils.getUUID());
                milestoneTemplateActivities.setAuditDetails(masterUtils.getAuditDetails(milestoneTemplateRequest.getRequestInfo(), false));
                milestoneTemplateActivities.setMilestoneTemplate(milestoneTemplate.getId());
            }
        }
        kafkaTemplate.send(propertiesManager.getWorksMasterMilestoneTemplateSaveOrUpdateValidatedTopic(), milestoneTemplateRequest);

        response.setMilestoneTemplates(milestoneTemplateRequest.getMilestoneTemplates());
        response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(milestoneTemplateRequest.getRequestInfo(), true));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> update(MilestoneTemplateRequest milestoneTemplateRequest) {
        MilestoneTemplateResponse response = new MilestoneTemplateResponse();

        milestoneTemplateValidator.validate(milestoneTemplateRequest);
        milestoneTemplateValidator.validateForUpdate(milestoneTemplateRequest);

        for (final MilestoneTemplate milestoneTemplate : milestoneTemplateRequest.getMilestoneTemplates()) {
            milestoneTemplate.setAuditDetails(masterUtils.getAuditDetails(milestoneTemplateRequest.getRequestInfo(), true));
            for (final MilestoneTemplateActivities milestoneTemplateActivities : milestoneTemplate.getMilestoneTemplateActivities()) {
                milestoneTemplateActivities.setAuditDetails(masterUtils.getAuditDetails(milestoneTemplateRequest.getRequestInfo(), true));
            }
        }

        kafkaTemplate.send(propertiesManager.getWorksMasterMilestoneTemplateSaveOrUpdateValidatedTopic(), milestoneTemplateRequest);

        response.setMilestoneTemplates(milestoneTemplateRequest.getMilestoneTemplates());
        response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(milestoneTemplateRequest.getRequestInfo(), true));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public List<MilestoneTemplate> search(MilestoneTemplateSearchCriteria milestoneTemplateSearchCriteria) {
        return milestoneTemplateRepository.getMilestoneTemplateByCriteria(milestoneTemplateSearchCriteria);
    }

    public MilestoneTemplate getById(String id, String tenantId) {
        return milestoneTemplateRepository.getbyId(id, tenantId);
    }

    public MilestoneTemplate getByCode(String code, String tenantId, String id, Boolean IsUpdateUniqueCheck) {
        return milestoneTemplateRepository.getByCode(code, tenantId, id, IsUpdateUniqueCheck);
    }
}
