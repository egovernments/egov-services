package org.egov.works.workorder.domain.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.persistence.helper.LoaActivityHelper;
import org.egov.works.workorder.web.contract.LOAActivity;
import org.egov.works.workorder.web.contract.LoaActivitySearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LoaActivityRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_loaactivity loaactivity";

    public List<LOAActivity> searchLoaActivity(final LoaActivitySearchContract loaActivitySearchCriteria) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (loaActivitySearchCriteria.getSortBy() != null
                && !loaActivitySearchCriteria.getSortBy().isEmpty()) {
            validateSortByOrder(loaActivitySearchCriteria.getSortBy());
            validateEntityFieldName(loaActivitySearchCriteria.getSortBy(), LoaActivityHelper.class);
        }

        String orderBy = "order by loaactivity.id";
        if (loaActivitySearchCriteria.getSortBy() != null
                && !loaActivitySearchCriteria.getSortBy().isEmpty()) {
            orderBy = "order by loaactivity." + loaActivitySearchCriteria.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (loaActivitySearchCriteria.getTenantId() != null) {
            addAnd(params);
            params.append("loaactivity.tenantId =:tenantId");
            paramValues.put("tenantId", loaActivitySearchCriteria.getTenantId());
        }
        if (loaActivitySearchCriteria.getIds() != null) {
            addAnd(params);
            params.append("loaactivity.id in(:ids) ");
            paramValues.put("ids", loaActivitySearchCriteria.getIds());
        }


        if (loaActivitySearchCriteria.getLetterOfAcceptanceEstimateIds() != null && !loaActivitySearchCriteria.getLetterOfAcceptanceEstimateIds().isEmpty()) {
            addAnd(params);
            params.append("loaactivity.letterofacceptanceestimate in(:letterofacceptanceestimate)");
            paramValues.put("letterofacceptanceestimate", loaActivitySearchCriteria.getLetterOfAcceptanceEstimateIds());
        }

        params.append(" and loaactivity.deleted = false");

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(LoaActivityHelper.class);

        List<LoaActivityHelper> assetsForLoaList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        List<LOAActivity> loaActivities = new ArrayList<>();
        for (LoaActivityHelper loaActivityHelper : assetsForLoaList) {
            LOAActivity loaActivity = loaActivityHelper.toDomain();
            loaActivities.add(loaActivity);

        }
        return loaActivities;
    }
}
