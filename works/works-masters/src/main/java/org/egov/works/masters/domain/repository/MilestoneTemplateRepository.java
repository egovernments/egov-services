package org.egov.works.masters.domain.repository;

import org.egov.works.masters.domain.repository.builder.MilestoneTemplateQueryBuilder;
import org.egov.works.masters.domain.repository.helper.MilestoneTemplateActivitiesHelper;
import org.egov.works.masters.domain.repository.helper.MilestoneTemplateHelper;
import org.egov.works.masters.domain.repository.helper.NonSORHelper;
import org.egov.works.masters.web.contract.MilestoneTemplate;
import org.egov.works.masters.web.contract.MilestoneTemplateActivities;
import org.egov.works.masters.web.contract.MilestoneTemplateSearchCriteria;
import org.egov.works.masters.web.contract.NonSOR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ramki on 15/12/17.
 */
@Service
public class MilestoneTemplateRepository {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private MilestoneTemplateQueryBuilder milestoneTemplateQueryBuilder;

    public List<MilestoneTemplate> getMilestoneTemplateByCriteria(MilestoneTemplateSearchCriteria milestoneTemplateSearchCriteria) {
        Map params = new HashMap();
        String queryStr = milestoneTemplateQueryBuilder.getSearchQuery(milestoneTemplateSearchCriteria, params);
        List<MilestoneTemplateHelper> milestoneTemplateHelpers = namedParameterJdbcTemplate.query(queryStr, params, new BeanPropertyRowMapper(MilestoneTemplateHelper.class));
        List<MilestoneTemplate> milestoneTemplates = new ArrayList<>();

        MilestoneTemplate milestoneTemplate;

        for (MilestoneTemplateHelper milestoneTemplateHelper : milestoneTemplateHelpers) {
            milestoneTemplate = milestoneTemplateHelper.toDomain();
            milestoneTemplate.setMilestoneTemplateActivities(prepareMilestoneTemplateActivities(milestoneTemplate.getId(), milestoneTemplate.getTenantId()));
            milestoneTemplates.add(milestoneTemplate);
        }
        return milestoneTemplates;
    }

    private List<MilestoneTemplateActivities> prepareMilestoneTemplateActivities(String milestoneTemplate, String tenantId) {
        Map params = new HashMap();
        String queryStr = milestoneTemplateQueryBuilder.getMTActivities(milestoneTemplate, tenantId, params);
        List<MilestoneTemplateActivitiesHelper> milestoneTemplateActivitiesHelpers = namedParameterJdbcTemplate.query(queryStr, params, new BeanPropertyRowMapper(MilestoneTemplateActivitiesHelper.class));
        List<MilestoneTemplateActivities> milestoneTemplateActivities = new ArrayList<>();

        MilestoneTemplateActivities milestoneTemplateActivity;

        for (MilestoneTemplateActivitiesHelper milestoneTemplateActivitiesHelper : milestoneTemplateActivitiesHelpers) {
            milestoneTemplateActivity = milestoneTemplateActivitiesHelper.toDomain();
            milestoneTemplateActivities.add(milestoneTemplateActivity);
        }
        return milestoneTemplateActivities;
    }

    public MilestoneTemplate getbyId(String id, String tenantId) {
        MilestoneTemplateSearchCriteria milestoneTemplateSearchCriteria = new MilestoneTemplateSearchCriteria();
        List<MilestoneTemplate> milestoneTemplates;
        List<String> ids = new ArrayList<>();
        ids.add(id);
        milestoneTemplateSearchCriteria.setIds(ids);
        milestoneTemplateSearchCriteria.setTenantId(tenantId);
        milestoneTemplates = getMilestoneTemplateByCriteria(milestoneTemplateSearchCriteria);
        return milestoneTemplates.isEmpty() ? null : milestoneTemplates.get(0);
    }

    public MilestoneTemplate getByCode(String code, String tenantId, String mtId, Boolean IsUpdateUniqueCheck) {
        MilestoneTemplateSearchCriteria milestoneTemplateSearchCriteria = new MilestoneTemplateSearchCriteria();
        List<MilestoneTemplate> milestoneTemplates;
        if (mtId != null && !mtId.isEmpty()) {
            List<String> ids = new ArrayList<>();
            ids.add(mtId);
            milestoneTemplateSearchCriteria.setIds(ids);
        }
        milestoneTemplateSearchCriteria.setCodes(Arrays.asList(code));
        milestoneTemplateSearchCriteria.setTenantId(tenantId);
        milestoneTemplateSearchCriteria.setIsUpdateUniqueCheck(IsUpdateUniqueCheck);
        milestoneTemplates = getMilestoneTemplateByCriteria(milestoneTemplateSearchCriteria);
        return milestoneTemplates.isEmpty() ? null : milestoneTemplates.get(0);
    }
}
