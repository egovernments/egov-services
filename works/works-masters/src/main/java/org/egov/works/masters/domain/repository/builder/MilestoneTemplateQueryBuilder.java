package org.egov.works.masters.domain.repository.builder;

import org.egov.works.masters.web.contract.MilestoneTemplateSearchCriteria;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ramki on 15/12/17.
 */
@Service
public class MilestoneTemplateQueryBuilder {
    public static final String BASE_SEARCH_QUERY = "SELECT * FROM egw_milestonetemplate mt";
    public static final String GET_MILESTONETEMPLATE_ACTIVTIES_BY_MT = "select * from egw_milestonetemplateactivities where tenantid = :tenantId and deleted=false and milestonetemplate=:milestoneTemplate;";

    public String getSearchQuery(MilestoneTemplateSearchCriteria milestoneTemplateSearchCriteria, Map params) {
        StringBuilder selectQuery = new StringBuilder(BASE_SEARCH_QUERY);
        appendParams(milestoneTemplateSearchCriteria, params, selectQuery);
        appendLimitAndOffset(milestoneTemplateSearchCriteria, params, selectQuery);
        return selectQuery.toString();
    }

    public String getMTActivities(String milestoneTemplate, String tenantId, Map params) {
        params.put("milestoneTemplate", milestoneTemplate);
        params.put("tenantId", tenantId);
        return GET_MILESTONETEMPLATE_ACTIVTIES_BY_MT;
    }

    private void appendParams(MilestoneTemplateSearchCriteria milestoneTemplateSearchCriteria, Map params, StringBuilder selectQuery) {

        selectQuery.append(" where mt.id is not null and deleted=false");

        if (milestoneTemplateSearchCriteria.getTenantId() != null) {
            selectQuery.append(" and mt.tenantid = :tenantId");
            params.put("tenantId", milestoneTemplateSearchCriteria.getTenantId());
        }

        if (milestoneTemplateSearchCriteria.getIds() != null && !milestoneTemplateSearchCriteria.getIds().isEmpty()) {
            if (milestoneTemplateSearchCriteria.getIsUpdateUniqueCheck())
                selectQuery.append(" and mt.id not in (:mtIds)");
            else
                selectQuery.append(" and mt.id in (:mtIds)");
            params.put("mtIds", milestoneTemplateSearchCriteria.getIds());
        }

        if (milestoneTemplateSearchCriteria.getCodes() != null && !milestoneTemplateSearchCriteria.getCodes().isEmpty()) {
            selectQuery.append(" and mt.code in (:codes)");
            params.put("codes", milestoneTemplateSearchCriteria.getCodes());
        }

        if (milestoneTemplateSearchCriteria.getActive()!=null) {
            selectQuery.append(" and mt.active = :active");
            params.put("active", milestoneTemplateSearchCriteria.getActive());
        }

        if (milestoneTemplateSearchCriteria.getTypeOfWork() != null && !milestoneTemplateSearchCriteria.getTypeOfWork().isEmpty()) {
            selectQuery.append(" and mt.typeofwork in (:typeOfWork)");
            params.put("typeOfWork", milestoneTemplateSearchCriteria.getTypeOfWork());
        }

        if (milestoneTemplateSearchCriteria.getSubTypeOfWork() != null && !milestoneTemplateSearchCriteria.getSubTypeOfWork().isEmpty()) {
            selectQuery.append(" and mt.subtypeofwork in (:subTypeOfWork)");
            params.put("subTypeOfWork", milestoneTemplateSearchCriteria.getSubTypeOfWork());
        }
    }

    private StringBuilder appendLimitAndOffset(MilestoneTemplateSearchCriteria milestoneTemplateSearchCriteria,
                                               @SuppressWarnings("rawtypes") Map params, StringBuilder selectQuery) {

        selectQuery.append(" order by mt.id");
        selectQuery.append(" limit :pageSize");
        if (milestoneTemplateSearchCriteria.getPageSize() != null)
            params.put("pageSize", milestoneTemplateSearchCriteria.getPageSize());
        else
            params.put("pageSize", 500);

        selectQuery.append(" offset :pageNumber");

        if (milestoneTemplateSearchCriteria.getPageNumber() != null)
            params.put("pageNumber", milestoneTemplateSearchCriteria.getPageNumber());
        else
            params.put("pageNumber", 0);

        return selectQuery;
    }
}
