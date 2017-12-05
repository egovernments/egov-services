package org.egov.works.workorder.domain.repository;

import org.egov.works.common.persistence.repository.JdbcRepository;
import org.egov.works.workorder.persistence.helper.AssetsForLOAHelper;
import org.egov.works.workorder.web.contract.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AssetsLOARepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_assetsforloa loaassets";


    public List<AssetsForLOA> searchLoaAssets(final AssetsForLoaSearchContract assetsForLoaSearchCriteria) {

        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        String tableName = TABLE_NAME;

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (assetsForLoaSearchCriteria.getSortBy() != null
                && !assetsForLoaSearchCriteria.getSortBy().isEmpty()) {
            validateSortByOrder(assetsForLoaSearchCriteria.getSortBy());
            validateEntityFieldName(assetsForLoaSearchCriteria.getSortBy(), AssetsForLOA.class);
        }

        String orderBy = "order by loaassets.id";
        if (assetsForLoaSearchCriteria.getSortBy() != null
                && !assetsForLoaSearchCriteria.getSortBy().isEmpty()) {
            orderBy = "order by loaassets." + assetsForLoaSearchCriteria.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", tableName);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (assetsForLoaSearchCriteria.getTenantId() != null) {
            addAnd(params);
            params.append("loaassets.tenantId =:tenantId");
            paramValues.put("tenantId", assetsForLoaSearchCriteria.getTenantId());
        }
        if (assetsForLoaSearchCriteria.getIds() != null) {
            addAnd(params);
            params.append("loaassets.id in(:ids) ");
            paramValues.put("ids", assetsForLoaSearchCriteria.getIds());
        }


        if (assetsForLoaSearchCriteria.getLetterOfAcceptanceEstimateIds() != null && !assetsForLoaSearchCriteria.getLetterOfAcceptanceEstimateIds().isEmpty()) {
            addAnd(params);
            params.append("loaassets.letterofacceptanceestimate in(:letterofacceptanceestimate)");
            paramValues.put("letterofacceptanceestimate", assetsForLoaSearchCriteria.getLetterOfAcceptanceEstimateIds());
        }

        params.append(" and loaassets.deleted = false");

        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(AssetsForLOAHelper.class);

        List<AssetsForLOAHelper> assetsForLoaList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
        List<AssetsForLOA> loaAssets = new ArrayList<>();
        for (AssetsForLOAHelper assetsForLOAHelper : assetsForLoaList) {
            AssetsForLOA assetsForLOA = assetsForLOAHelper.toDomain();
            loaAssets.add(assetsForLOA);

        }
        return loaAssets;
    }
}
