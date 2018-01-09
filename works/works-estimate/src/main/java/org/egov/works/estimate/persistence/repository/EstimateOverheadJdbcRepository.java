package org.egov.works.estimate.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.persistence.helper.EstimateOverheadHelper;
import org.egov.works.estimate.web.contract.EstimateOverhead;
import org.egov.works.estimate.web.contract.EstimateOverheadSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class EstimateOverheadJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_estimate_overheads";

    public List<EstimateOverhead> search(
            EstimateOverheadSearchContract estimateOverheadSearchContract) {
        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();

        if (estimateOverheadSearchContract.getSortBy() != null
                && !estimateOverheadSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(estimateOverheadSearchContract.getSortBy());
            validateEntityFieldName(estimateOverheadSearchContract.getSortBy(), EstimateOverheadHelper.class);
        }

        StringBuilder orderBy = new StringBuilder("order by createdtime");
        if (estimateOverheadSearchContract.getSortBy() != null
                && !estimateOverheadSearchContract.getSortBy().isEmpty()) {
            orderBy.delete(0,orderBy.length()).append("order by ").append(estimateOverheadSearchContract.getSortBy());
        }

        searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (estimateOverheadSearchContract.getTenantId() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", estimateOverheadSearchContract.getTenantId());
        }
        if (estimateOverheadSearchContract.getIds() != null) {
            addAnd(params);
            params.append("id in(:ids) ");
            paramValues.put("ids", estimateOverheadSearchContract.getIds());
        }

        if (estimateOverheadSearchContract.getDetailedEstimateIds() != null) {
            addAnd(params);
            params.append("detailedEstimate in(:detailedEstimateIds) ");
            paramValues.put("detailedEstimateIds", estimateOverheadSearchContract.getDetailedEstimateIds());
        }

        params.append(" and deleted = false");
        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(EstimateOverheadHelper.class);

        List<EstimateOverheadHelper> estimateOverheadEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        List<EstimateOverhead> estimateOverheads = new ArrayList<>();

        for (EstimateOverheadHelper estimateOverheadEntity : estimateOverheadEntities) {
            estimateOverheads.add(estimateOverheadEntity.toDomain());
        }

        return estimateOverheads;
    }

}
