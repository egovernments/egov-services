package org.egov.works.estimate.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.persistence.helper.AbstractEstimateDetailsHelper;
import org.egov.works.estimate.web.contract.AbstractEstimateDetails;
import org.egov.works.estimate.web.contract.AbstractEstimateDetailsSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class AbstractEstimateDetailsJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_abstractestimate_details";

    public List<AbstractEstimateDetails> search(
            AbstractEstimateDetailsSearchContract abstractEstimateDetailsSearchContract) {
        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuffer params = new StringBuffer();

        if (abstractEstimateDetailsSearchContract.getSortBy() != null
                && !abstractEstimateDetailsSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(abstractEstimateDetailsSearchContract.getSortBy());
            validateEntityFieldName(abstractEstimateDetailsSearchContract.getSortBy(), AbstractEstimateDetails.class);
        }

        StringBuilder orderBy = new StringBuilder("order by createdtime");
        if (abstractEstimateDetailsSearchContract.getSortBy() != null
                && !abstractEstimateDetailsSearchContract.getSortBy().isEmpty()) {
            orderBy.delete(0,orderBy.length()).append("order by ").append(abstractEstimateDetailsSearchContract.getSortBy());
        }

        searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (abstractEstimateDetailsSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", abstractEstimateDetailsSearchContract.getTenantId());
        }
        if (abstractEstimateDetailsSearchContract.getIds() != null) {
            addAnd(params);
            params.append("id in(:ids) ");
            paramValues.put("ids", abstractEstimateDetailsSearchContract.getIds());
        }

        if (abstractEstimateDetailsSearchContract.getAbstractEstimateIds() != null) {
            addAnd(params);
            params.append("abstractEstimate in(:estimateIds) ");
            paramValues.put("estimateIds", abstractEstimateDetailsSearchContract.getAbstractEstimateIds());
        }

        params.append(" and deleted = false");
        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy.toString());

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(AbstractEstimateDetailsHelper.class);

        List<AbstractEstimateDetailsHelper> abstractEstimateDetailsEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        List<AbstractEstimateDetails> abstractEstimateDetails = new ArrayList<>();

        for (AbstractEstimateDetailsHelper abstractEstimateDetailsEntity : abstractEstimateDetailsEntities) {
            abstractEstimateDetails.add(abstractEstimateDetailsEntity.toDomain());
        }

        return abstractEstimateDetails;
    }

}
