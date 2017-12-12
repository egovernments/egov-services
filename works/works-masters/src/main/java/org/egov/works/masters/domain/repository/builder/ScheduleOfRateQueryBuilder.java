package org.egov.works.masters.domain.repository.builder;

import org.egov.works.masters.web.contract.ScheduleOfRateSearchCriteria;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ramki on 4/11/17.
 */
@Service
public class ScheduleOfRateQueryBuilder {
    public static final String BASE_SEARCH_QUERY = "SELECT * FROM egw_scheduleofrate sor";
    public static final String SORRATE_SEARCH_EXTENTION = ", egw_sorrate sorrate";
    public static final String MARKETRATE_SEARCH_EXTENTION = ", egw_marketrate marketrate";
    public static final String GETSORRATE_BY_SCHEDULEOFRATE = "select * from egw_sorrate where tenantid = :tenantId and deleted=false and scheduleofrate=:scheduleOfRate;";
    public static final String GETMARKETRATE_BY_SCHEDULEOFRATE = "select * from egw_marketrate where tenantid = :tenantId and deleted=false and scheduleofrate=:scheduleOfRate;";

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

        selectQuery.append(" where sor.id is not null and deleted=false");

        if (isSorRate) {
            selectQuery.append(" and sorrate.scheduleofrate=sor.code and :validSORRateDate between sorrate.fromdate and sorrate.todate");
            params.put("validSORRateDate", scheduleOfRateSearchCriteria.getValidSORRateDate());
        }
        if (isMarketRate) {
            selectQuery.append(" and marketrate.scheduleofrate=sor.code and :validMarketRateDate between marketrate.fromdate and marketrate.todate");
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

        if (scheduleOfRateSearchCriteria.getSorCodes() != null && !scheduleOfRateSearchCriteria.getSorCodes().isEmpty() && scheduleOfRateSearchCriteria.getSorCodes().size() == 1) {
            selectQuery.append(" and lower(sor.code) like :sorCodes ");
            params.put("sorCodes", '%' + scheduleOfRateSearchCriteria.getSorCodes().get(0).toLowerCase() + '%');
        } else if (scheduleOfRateSearchCriteria.getSorCodes() != null) {
            selectQuery.append(" and sor.code in (:sorCodes)");
            params.put("sorCodes", scheduleOfRateSearchCriteria.getSorCodes());
        }

        if (scheduleOfRateSearchCriteria.getScheduleCategoryCodes() != null && !scheduleOfRateSearchCriteria.getScheduleCategoryCodes().isEmpty() && scheduleOfRateSearchCriteria.getScheduleCategoryCodes().size() == 1) {
            selectQuery.append(" and lower(sor.schedulecategory) like :scheduleCategoryCodes ");
            params.put("scheduleCategoryCodes", '%' + scheduleOfRateSearchCriteria.getScheduleCategoryCodes().get(0).toLowerCase() + '%');
        } else if (scheduleOfRateSearchCriteria.getScheduleCategoryCodes() != null) {
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
