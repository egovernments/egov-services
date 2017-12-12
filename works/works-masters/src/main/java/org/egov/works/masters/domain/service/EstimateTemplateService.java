package org.egov.works.masters.domain.service;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.masters.config.PropertiesManager;
import org.egov.works.masters.domain.repository.EstimateTemplateRepository;
import org.egov.works.masters.domain.validator.EstimateTemplateValidator;
import org.egov.works.masters.utils.MasterUtils;
import org.egov.works.masters.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ramki on 31/10/17.
 */

@Service
public class EstimateTemplateService {

    @Autowired
    private EstimateTemplateRepository estimateTemplateRepository;

    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private MasterUtils masterUtils;

    @Autowired
    private EstimateTemplateValidator estimateTemplateValidator;


    @Transactional
    public ResponseEntity<?> create(EstimateTemplateRequest estimateTemplateRequest) {
        CommonUtils commonUtils = new CommonUtils();
        EstimateTemplateResponse response = new EstimateTemplateResponse();

        estimateTemplateValidator.validate(estimateTemplateRequest);
        estimateTemplateValidator.validateForExistance(estimateTemplateRequest);

        for (final EstimateTemplate estimateTemplate : estimateTemplateRequest.getEstimateTemplates()) {
            estimateTemplate.setId(commonUtils.getUUID());
            estimateTemplate.setAuditDetails(masterUtils.getAuditDetails(estimateTemplateRequest.getRequestInfo(), false));
            for (final EstimateTemplateActivities estimateTemplateActivities : estimateTemplate.getEstimateTemplateActivities()) {
                estimateTemplateActivities.setId(commonUtils.getUUID());
                estimateTemplateActivities.setAuditDetails(masterUtils.getAuditDetails(estimateTemplateRequest.getRequestInfo(), false));
                if (estimateTemplateActivities.getNonSOR() != null)
                    estimateTemplateActivities.getNonSOR().setId(commonUtils.getUUID());
                estimateTemplateActivities.setEstimateTemplate(estimateTemplate.getId());
            }
        }
        kafkaTemplate.send(propertiesManager.getWorksMasterEstimateTemplateSaveOrUpdateValidatedTopic(), estimateTemplateRequest);

        response.setEstimateTemplates(estimateTemplateRequest.getEstimateTemplates());
        response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(estimateTemplateRequest.getRequestInfo(), true));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> update(EstimateTemplateRequest estimateTemplateRequest) {
        EstimateTemplateResponse response = new EstimateTemplateResponse();

        estimateTemplateValidator.validate(estimateTemplateRequest);
        estimateTemplateValidator.validateForUpdate(estimateTemplateRequest);

        for (final EstimateTemplate estimateTemplate : estimateTemplateRequest.getEstimateTemplates()) {
            estimateTemplate.setAuditDetails(masterUtils.getAuditDetails(estimateTemplateRequest.getRequestInfo(), true));
            for (final EstimateTemplateActivities estimateTemplateActivities : estimateTemplate.getEstimateTemplateActivities()) {
                estimateTemplateActivities.setAuditDetails(masterUtils.getAuditDetails(estimateTemplateRequest.getRequestInfo(), true));
            }
        }

        kafkaTemplate.send(propertiesManager.getWorksMasterEstimateTemplateSaveOrUpdateValidatedTopic(), estimateTemplateRequest);

        response.setEstimateTemplates(estimateTemplateRequest.getEstimateTemplates());
        response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(estimateTemplateRequest.getRequestInfo(), true));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public List<EstimateTemplate> search(EstimateTemplateSearchCriteria estimateTemplateSearchCriteria) {
        return estimateTemplateRepository.getEstimateTemplateByCriteria(estimateTemplateSearchCriteria);
    }

    public EstimateTemplate getById(String id, String tenantId) {
        return estimateTemplateRepository.getbyId(id, tenantId);
    }

    public EstimateTemplate getByCode(String code, String tenantId, String id, Boolean IsUpdateUniqueCheck) {
        return estimateTemplateRepository.getByCode(code, tenantId, id, IsUpdateUniqueCheck);
    }
}
