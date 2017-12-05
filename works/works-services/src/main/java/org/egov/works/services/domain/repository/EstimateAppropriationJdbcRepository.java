package org.egov.works.services.domain.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.services.web.contract.EstimateAppropriation;
import org.egov.works.services.web.contract.EstimateAppropriationSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class EstimateAppropriationJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egw_estimate_appropriation";

    public List<EstimateAppropriation> search(EstimateAppropriationSearchContract estimateAppropriationSearchContract) {
        String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

        Map<String, Object> paramValues = new HashMap<>();
        StringBuilder params = new StringBuilder();

        if (estimateAppropriationSearchContract.getSortBy() != null
                && !estimateAppropriationSearchContract.getSortBy().isEmpty()) {
            validateSortByOrder(estimateAppropriationSearchContract.getSortBy());
            validateEntityFieldName(estimateAppropriationSearchContract.getSortBy(), EstimateAppropriation.class);
        }

        String orderBy = "order by objectNumber";
        if (estimateAppropriationSearchContract.getSortBy() != null
                && !estimateAppropriationSearchContract.getSortBy().isEmpty()) {
            orderBy = "order by " + estimateAppropriationSearchContract.getSortBy();
        }

        searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

        searchQuery = searchQuery.replace(":selectfields", " * ");

        if (estimateAppropriationSearchContract.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", estimateAppropriationSearchContract.getTenantId());
        }
        if (estimateAppropriationSearchContract.getBudgetReferanceNumbers() != null) {
            addAnd(params);
            params.append("budgetrefnumber in (:budgetrefnumbers)");
            paramValues.put("budgetrefnumbers", estimateAppropriationSearchContract.getBudgetReferanceNumbers());
        }

        if (estimateAppropriationSearchContract.getObjectNumber() != null) {
            addAnd(params);
            params.append("objectNumber =:objectnumber");
            paramValues.put("objectnumber", estimateAppropriationSearchContract.getObjectNumber());
        }

        if (estimateAppropriationSearchContract.getAbstractEstimateNumbers() != null) {
            addAnd(params);
            params.append("objectNumber =:objectnumber and objectType =:objecttype");
            paramValues.put("objectnumber", estimateAppropriationSearchContract.getAbstractEstimateNumbers());
            paramValues.put("objecttype", estimateAppropriationSearchContract.getObjectType());

        }

        if (estimateAppropriationSearchContract.getWorkOrderNumbers() != null) {
            addAnd(params);
            params.append("objectNumber =:objectnumber and objectType =:objecttype");
            paramValues.put("objectnumber", estimateAppropriationSearchContract.getWorkOrderNumbers());
            paramValues.put("objecttype", estimateAppropriationSearchContract.getObjectType());
        }

        if (estimateAppropriationSearchContract.getDetailedEstimateNumbers() != null) {
            if (params.length() > 0) {
                params.append(" and ");
            }
            params.append("objectNumber =:objectnumber and objectType =:objecttype");
            paramValues.put("objectnumber", estimateAppropriationSearchContract.getDetailedEstimateNumbers());
            paramValues.put("objecttype", estimateAppropriationSearchContract.getObjectType());
        }

        if (estimateAppropriationSearchContract.getObjectType() != null) {
            addAnd(params);
            params.append("objectType =:objecttype");
            paramValues.put("objecttype", estimateAppropriationSearchContract.getObjectType());
        }

        if (estimateAppropriationSearchContract.getIds() != null) {
            addAnd(params);
            params.append("id in(:ids) ");
            paramValues.put("ids", estimateAppropriationSearchContract.getIds());
        }

        params.append(" and deleted = false");
        if (params.length() > 0) {

            searchQuery = searchQuery.replace(":condition", " where " + params.toString());

        } else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        BeanPropertyRowMapper row = new BeanPropertyRowMapper(EstimateAppropriation.class);

        return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
    }

}
