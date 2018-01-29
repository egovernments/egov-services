package org.egov.works.masters.domain.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.egov.works.masters.web.contract.ScheduleOfRateSearchCriteria;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ramki on 4/11/17.
 */
@Service
public class ScheduleOfRateQueryBuilder {
    public static final String BASE_SEARCH_QUERY = "SELECT sor.* FROM egw_scheduleofrate sor";
    public static final String SORRATE_SEARCH_EXTENTION = ", egw_sorrate sorrate";
    public static final String MARKETRATE_SEARCH_EXTENTION = ", egw_marketrate marketrate";
    public static final String GETSORRATE_BY_SCHEDULEOFRATE = "select * from egw_sorrate sor where tenantid = :tenantId and sor.deleted=false and sor.scheduleofrate=:scheduleOfRate;";
    public static final String GETMARKETRATE_BY_SCHEDULEOFRATE = "select * from egw_marketrate mr where tenantid = :tenantId and mr.deleted=false and mr.scheduleofrate=:scheduleOfRate;";

    public String getSearchQuery(ScheduleOfRateSearchCriteria scheduleOfRateSearchCriteria, Map params) {
        StringBuilder selectQuery = new StringBuilder(BASE_SEARCH_QUERY);
        appendParams(scheduleOfRateSearchCriteria, params, selectQuery);
        appendLimitAndOffset(scheduleOfRateSearchCriteria, params, selectQuery);
        return selectQuery.toString();
    }

    public String getSORRates(String scheduleOfRate, String tenantId, Map params) {
        params.put("scheduleOfRate", scheduleOfRate);
        params.put("tenantId", tenantId);
        return GETSORRATE_BY_SCHEDULEOFRATE;
    }

    public String getMarketRates(String scheduleOfRate, String tenantId, Map params) {
        params.put("scheduleOfRate", scheduleOfRate);
        params.put("tenantId", tenantId);
        return GETMARKETRATE_BY_SCHEDULEOFRATE;
    }

    private void appendParams(ScheduleOfRateSearchCriteria scheduleOfRateSearchCriteria, Map params, StringBuilder selectQuery) {
        Boolean isSorRate = Boolean.FALSE;
        Boolean isMarketRate = Boolean.FALSE;

        if (scheduleOfRateSearchCriteria.getValidSORRateDate() != null) {
            selectQuery.append(SORRATE_SEARCH_EXTENTION);
            isSorRate = Boolean.TRUE;
        }
        if (scheduleOfRateSearchCriteria.getValidMarketRateDate() != null) {
            selectQuery.append(MARKETRATE_SEARCH_EXTENTION);
            isMarketRate = Boolean.TRUE;
        }

        selectQuery.append(" where sor.id is not null and sor.deleted=false");

        if (isSorRate) {
            selectQuery.append(" and sorrate.scheduleofrate=sor.id and :validSORRateDate between sorrate.fromdate and sorrate.todate and sorrate.deleted=false");
            params.put("validSORRateDate", scheduleOfRateSearchCriteria.getValidSORRateDate());
        }
        if (isMarketRate) {
            selectQuery.append(" and marketrate.scheduleofrate=sor.id and :validMarketRateDate between marketrate.fromdate and marketrate.todate and marketrate.deleted=false");
            params.put("validMarketRateDate", scheduleOfRateSearchCriteria.getValidMarketRateDate());
        }

        if (scheduleOfRateSearchCriteria.getTenantId() != null) {
            selectQuery.append(" and sor.tenantid = :tenantId");
            params.put("tenantId", scheduleOfRateSearchCriteria.getTenantId());
        }

        if (scheduleOfRateSearchCriteria.getIds() != null && !scheduleOfRateSearchCriteria.getIds().isEmpty()) {
            if (scheduleOfRateSearchCriteria.getIsUpdateUniqueCheck())
                selectQuery.append(" and sor.id not in (:sorIds)");
            else
                selectQuery.append(" and sor.id in (:sorIds)");
            params.put("sorIds", scheduleOfRateSearchCriteria.getIds());
        }

        if (StringUtils.isNotBlank(scheduleOfRateSearchCriteria.getSorCodeLike())) {
            selectQuery.append(" and lower(sor.code) like :sorCodeLike ");
            params.put("sorCodeLike", '%' + scheduleOfRateSearchCriteria.getSorCodeLike().toLowerCase() + '%');
        } 
        if (scheduleOfRateSearchCriteria.getSorCodes() != null && !scheduleOfRateSearchCriteria.getSorCodes().isEmpty()) {
            selectQuery.append(" and sor.code in (:sorCodes)");
            params.put("sorCodes", scheduleOfRateSearchCriteria.getSorCodes());
        }

        if (StringUtils.isNotBlank(scheduleOfRateSearchCriteria.getScheduleCategoryCodeLike())) {
            selectQuery.append(" and lower(sor.schedulecategory) like :scheduleCategoryCodeLike ");
            params.put("scheduleCategoryCodeLike", '%' + scheduleOfRateSearchCriteria.getScheduleCategoryCodeLike().toLowerCase() + '%');
        }
        if (scheduleOfRateSearchCriteria.getScheduleCategoryCodes() != null && !scheduleOfRateSearchCriteria.getScheduleCategoryCodes().isEmpty()) {
            selectQuery.append(" and sor.schedulecategory in (:scheduleCategoryCodes)");
            params.put("scheduleCategoryCodes", scheduleOfRateSearchCriteria.getScheduleCategoryCodes());
        }
    }

    private StringBuilder appendLimitAndOffset(ScheduleOfRateSearchCriteria scheduleOfRateSearchCriteria,
                                               @SuppressWarnings("rawtypes") Map params, StringBuilder selectQuery) {

        selectQuery.append(" order by sor.id");
        selectQuery.append(" limit :pageSize");
        if (scheduleOfRateSearchCriteria.getPageSize() != null)
            params.put("pageSize", scheduleOfRateSearchCriteria.getPageSize());
        else
            params.put("pageSize", 500);

        selectQuery.append(" offset :pageNumber");

        if (scheduleOfRateSearchCriteria.getPageNumber() != null)
            params.put("pageNumber", scheduleOfRateSearchCriteria.getPageNumber());
        else
            params.put("pageNumber", 0);

        return selectQuery;
    }
}
