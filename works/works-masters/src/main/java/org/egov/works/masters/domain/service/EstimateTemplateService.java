package org.egov.works.masters.domain.service;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.masters.config.PropertiesManager;
import org.egov.works.masters.domain.repository.EstimateTemplateRepository;
import org.egov.works.masters.utils.MasterUtils;
import org.egov.works.masters.web.contract.EstimateTemplate;
import org.egov.works.masters.web.contract.EstimateTemplateActivities;
import org.egov.works.masters.web.contract.EstimateTemplateRequest;
import org.egov.works.masters.web.contract.EstimateTemplateSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public List<EstimateTemplate> create(EstimateTemplateRequest estimateTemplateRequest) {
        CommonUtils commonUtils = new CommonUtils();
        for (final EstimateTemplate estimateTemplate : estimateTemplateRequest.getEstimateTemplates()) {
            estimateTemplate.setId(commonUtils.getUUID());
            estimateTemplate.setAuditDetails(masterUtils.getAuditDetails(estimateTemplateRequest.getRequestInfo(), false));
            for (final EstimateTemplateActivities estimateTemplateActivities : estimateTemplate.getEstimateTemplateActivities()) {
                estimateTemplateActivities.setId(commonUtils.getUUID());
                estimateTemplateActivities.setAuditDetails(masterUtils.getAuditDetails(estimateTemplateRequest.getRequestInfo(), false));
                estimateTemplateActivities.getNonSOR().setId(commonUtils.getUUID());
            }
        }
        kafkaTemplate.send(propertiesManager.getWorksMasterEstimateTemplateCreateValidatedTopic(), estimateTemplateRequest);
        return estimateTemplateRequest.getEstimateTemplates();
    }

    public List<EstimateTemplate> update(EstimateTemplateRequest estimateTemplateRequest) {
        kafkaTemplate.send(propertiesManager.getWorksMasterEstimateTemplateUpdateValidatedTopic(), estimateTemplateRequest);
        return estimateTemplateRequest.getEstimateTemplates();
    }

    public List<EstimateTemplate> search(EstimateTemplateSearchCriteria estimateTemplateSearchCriteria) {
        return estimateTemplateRepository.getEstimateTemplateByCriteria(estimateTemplateSearchCriteria);
    }

}
