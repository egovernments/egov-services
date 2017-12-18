package org.egov.works.masters.domain.repository.builder;

import org.egov.works.masters.web.contract.EstimateTemplateSearchCriteria;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ramki on 4/11/17.
 */
@Service
public class EstimateTemplateQueryBuilder {
    public static final String BASE_SEARCH_QUERY = "SELECT * FROM egw_estimatetemplate et";
    public static final String GET_ESTIMATETEMPLATE_ACTIVTIES_BY_ET = "select * from egw_estimatetemplateactivities where tenantid = :tenantId and deleted=false and estimatetemplate=:estimateTemplate;";
    public static final String GET_NONSORRATE_BY_ID = "select * from egw_nonsor where tenantid = :tenantId and deleted=false and id=:nonSorId;";

    public String getSearchQuery(EstimateTemplateSearchCriteria estimateTemplateSearchCriteria, Map params) {
        StringBuilder selectQuery = new StringBuilder(BASE_SEARCH_QUERY);
        appendParams(estimateTemplateSearchCriteria, params, selectQuery);
        appendLimitAndOffset(estimateTemplateSearchCriteria, params, selectQuery);
        return selectQuery.toString();
    }

    public String getETActivities(String estimateTemplate, String tenantId, Map params) {
        params.put("estimateTemplate", estimateTemplate);
        params.put("tenantId", tenantId);
        return GET_ESTIMATETEMPLATE_ACTIVTIES_BY_ET;
    }

    public String getNonSorRate(String nonSorId, String tenantId, Map params) {
        params.put("nonSorId", nonSorId);
        params.put("tenantId", tenantId);
        return GET_NONSORRATE_BY_ID;
    }

    private void appendParams(EstimateTemplateSearchCriteria estimateTemplateSearchCriteria, Map params, StringBuilder selectQuery) {

        selectQuery.append(" where et.id is not null and deleted=false");

        if (estimateTemplateSearchCriteria.getTenantId() != null) {
            selectQuery.append(" and et.tenantid = :tenantId");
            params.put("tenantId", estimateTemplateSearchCriteria.getTenantId());
        }

        if (estimateTemplateSearchCriteria.getIds() != null && !estimateTemplateSearchCriteria.getIds().isEmpty()) {
            if (estimateTemplateSearchCriteria.getIsUpdateUniqueCheck())
                selectQuery.append(" and et.id not in (:etIds)");
            else
                selectQuery.append(" and et.id in (:etIds)");
            params.put("etIds", estimateTemplateSearchCriteria.getIds());
        }

        if (estimateTemplateSearchCriteria.getCodes() != null && !estimateTemplateSearchCriteria.getCodes().isEmpty()) {
            selectQuery.append(" and et.code in (:codes)");
            params.put("codes", estimateTemplateSearchCriteria.getCodes());
        }

        if (estimateTemplateSearchCriteria.getTypeOfWork() != null && !estimateTemplateSearchCriteria.getTypeOfWork().isEmpty()) {
            selectQuery.append(" and et.typeofwork in (:typeOfWork)");
            params.put("typeOfWork", estimateTemplateSearchCriteria.getTypeOfWork());
        }

        if (estimateTemplateSearchCriteria.getSubTypeOfWork() != null && !estimateTemplateSearchCriteria.getSubTypeOfWork().isEmpty()) {
            selectQuery.append(" and et.subtypeofwork in (:subTypeOfWork)");
            params.put("subTypeOfWork", estimateTemplateSearchCriteria.getSubTypeOfWork());
        }
    }

    private StringBuilder appendLimitAndOffset(EstimateTemplateSearchCriteria estimateTemplateSearchCriteria,
                                               @SuppressWarnings("rawtypes") Map params, StringBuilder selectQuery) {

        selectQuery.append(" order by et.id");
        selectQuery.append(" limit :pageSize");
        if (estimateTemplateSearchCriteria.getPageSize() != null)
            params.put("pageSize", estimateTemplateSearchCriteria.getPageSize());
        else
            params.put("pageSize", 500);

        selectQuery.append(" offset :pageNumber");

        if (estimateTemplateSearchCriteria.getPageNumber() != null)
            params.put("pageNumber", estimateTemplateSearchCriteria.getPageNumber());
        else
            params.put("pageNumber", 0);

        return selectQuery;
    }
}
