package org.egov.works.masters.domain.repository;

import org.egov.works.masters.domain.repository.builder.EstimateTemplateQueryBuilder;
import org.egov.works.masters.domain.repository.helper.EstimateTemplateActivitiesHelper;
import org.egov.works.masters.domain.repository.helper.EstimateTemplateHelper;
import org.egov.works.masters.domain.repository.helper.NonSORHelper;
import org.egov.works.masters.web.contract.EstimateTemplate;
import org.egov.works.masters.web.contract.EstimateTemplateActivities;
import org.egov.works.masters.web.contract.EstimateTemplateSearchCriteria;
import org.egov.works.masters.web.contract.NonSOR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramki on 6/11/17.
 */
@Service
public class EstimateTemplateRepository {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private EstimateTemplateQueryBuilder estimateTemplateQueryBuilder;

    public List<EstimateTemplate> getEstimateTemplateByCriteria(EstimateTemplateSearchCriteria estimateTemplateSearchCriteria){
        Map params = new HashMap();
        String queryStr = estimateTemplateQueryBuilder.getSearchQuery(estimateTemplateSearchCriteria, params);
        List<EstimateTemplateHelper> estimateTemplateHelpers = namedParameterJdbcTemplate.query(queryStr, params, new BeanPropertyRowMapper(EstimateTemplateHelper.class));
        List<EstimateTemplate> estimateTemplates = new ArrayList<>();

        EstimateTemplate estimateTemplate;

        for(EstimateTemplateHelper scheduleOfRateHelper : estimateTemplateHelpers) {
            estimateTemplate = scheduleOfRateHelper.toDomain();
            estimateTemplate.setEstimateTemplateActivities(prepareEstimateTemplateActivities(estimateTemplate.getCode(), estimateTemplate.getTenantId()));
            estimateTemplates.add(estimateTemplate);
        }
        return estimateTemplates;
    }

    private List<EstimateTemplateActivities> prepareEstimateTemplateActivities(String estimateTemplate, String tenantId){
        Map params = new HashMap();
        String queryStr = estimateTemplateQueryBuilder.getETActivities(estimateTemplate, tenantId, params);
        List<EstimateTemplateActivitiesHelper> estimateTemplateActivitiesHelpers = namedParameterJdbcTemplate.query(queryStr, params, new BeanPropertyRowMapper(EstimateTemplateActivitiesHelper.class));
        List<EstimateTemplateActivities> estimateTemplateActivities = new ArrayList<>();

        EstimateTemplateActivities estimateTemplateActivity;

        for(EstimateTemplateActivitiesHelper estimateTemplateActivitiesHelper : estimateTemplateActivitiesHelpers) {
            estimateTemplateActivity = estimateTemplateActivitiesHelper.toDomain();
            estimateTemplateActivity.setNonSOR(prepareNonSOR(estimateTemplateActivitiesHelper.getNonSOR(), estimateTemplateActivity.getTenantId()));
            estimateTemplateActivities.add(estimateTemplateActivity);
        }
        return estimateTemplateActivities;
    }

    private NonSOR prepareNonSOR(String nonSorId, String tenantId){
        Map params = new HashMap();
        String queryStr = estimateTemplateQueryBuilder.getNonSorRate(nonSorId, tenantId, params);
        List<NonSORHelper> nonSORHelpers = namedParameterJdbcTemplate.query(queryStr, params, new BeanPropertyRowMapper(NonSORHelper.class));
        return nonSORHelpers.get(0).toDomain();
    }
}
