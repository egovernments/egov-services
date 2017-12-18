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

import java.util.*;

/**
 * Created by ramki on 6/11/17.
 */
@Service
public class EstimateTemplateRepository {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private EstimateTemplateQueryBuilder estimateTemplateQueryBuilder;

    public List<EstimateTemplate> getEstimateTemplateByCriteria(EstimateTemplateSearchCriteria estimateTemplateSearchCriteria) {
        Map params = new HashMap();
        String queryStr = estimateTemplateQueryBuilder.getSearchQuery(estimateTemplateSearchCriteria, params);
        List<EstimateTemplateHelper> estimateTemplateHelpers = namedParameterJdbcTemplate.query(queryStr, params, new BeanPropertyRowMapper(EstimateTemplateHelper.class));
        List<EstimateTemplate> estimateTemplates = new ArrayList<>();

        EstimateTemplate estimateTemplate;

        for (EstimateTemplateHelper estimateTemplateHelper : estimateTemplateHelpers) {
            estimateTemplate = estimateTemplateHelper.toDomain();
            estimateTemplate.setEstimateTemplateActivities(prepareEstimateTemplateActivities(estimateTemplate.getId(), estimateTemplate.getTenantId()));
            estimateTemplates.add(estimateTemplate);
        }
        return estimateTemplates;
    }

    private List<EstimateTemplateActivities> prepareEstimateTemplateActivities(String estimateTemplate, String tenantId) {
        Map params = new HashMap();
        String queryStr = estimateTemplateQueryBuilder.getETActivities(estimateTemplate, tenantId, params);
        List<EstimateTemplateActivitiesHelper> estimateTemplateActivitiesHelpers = namedParameterJdbcTemplate.query(queryStr, params, new BeanPropertyRowMapper(EstimateTemplateActivitiesHelper.class));
        List<EstimateTemplateActivities> estimateTemplateActivities = new ArrayList<>();

        EstimateTemplateActivities estimateTemplateActivity;

        for (EstimateTemplateActivitiesHelper estimateTemplateActivitiesHelper : estimateTemplateActivitiesHelpers) {
            estimateTemplateActivity = estimateTemplateActivitiesHelper.toDomain();
            estimateTemplateActivity.setNonSOR(prepareNonSOR(estimateTemplateActivitiesHelper.getNonSOR(), estimateTemplateActivity.getTenantId()));
            estimateTemplateActivities.add(estimateTemplateActivity);
        }
        return estimateTemplateActivities;
    }

    private NonSOR prepareNonSOR(String nonSorId, String tenantId) {
        Map params = new HashMap();
        String queryStr = estimateTemplateQueryBuilder.getNonSorRate(nonSorId, tenantId, params);
        List<NonSORHelper> nonSORHelpers = namedParameterJdbcTemplate.query(queryStr, params, new BeanPropertyRowMapper(NonSORHelper.class));
        return nonSORHelpers.get(0).toDomain();
    }

    public EstimateTemplate getbyId(String id, String tenantId) {
        EstimateTemplateSearchCriteria estimateTemplateSearchCriteria = new EstimateTemplateSearchCriteria();
        List<EstimateTemplate> estimateTemplates;
        List<String> ids = new ArrayList<>();
        ids.add(id);
        estimateTemplateSearchCriteria.setIds(ids);
        estimateTemplateSearchCriteria.setTenantId(tenantId);
        estimateTemplates = getEstimateTemplateByCriteria(estimateTemplateSearchCriteria);
        return estimateTemplates.isEmpty() ? null : estimateTemplates.get(0);
    }

    public EstimateTemplate getByCode(String code, String tenantId, String etId, Boolean IsUpdateUniqueCheck) {
        EstimateTemplateSearchCriteria estimateTemplateSearchCriteria = new EstimateTemplateSearchCriteria();
        List<EstimateTemplate> estimateTemplates;
        if (etId != null && !etId.isEmpty()) {
            List<String> ids = new ArrayList<>();
            ids.add(etId);
            estimateTemplateSearchCriteria.setIds(ids);
        }
        estimateTemplateSearchCriteria.setCodes(Arrays.asList(code));
        estimateTemplateSearchCriteria.setTenantId(tenantId);
        estimateTemplateSearchCriteria.setIsUpdateUniqueCheck(IsUpdateUniqueCheck);
        estimateTemplates = getEstimateTemplateByCriteria(estimateTemplateSearchCriteria);
        return estimateTemplates.isEmpty() ? null : estimateTemplates.get(0);
    }
}
