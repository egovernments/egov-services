package org.egov.works.estimate.persistence.repository;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.web.contract.EstimateAppropriation;
import org.egov.works.estimate.web.contract.EstimateAppropriationSearchContract;
import org.egov.works.estimate.web.contract.ProjectCode;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EstimateAppropriationJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egw_estimate_appropriation";

	public List<EstimateAppropriation> search(EstimateAppropriationSearchContract estimateAppropriationSearchContract) {
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (estimateAppropriationSearchContract.getSortBy() != null
				&& !estimateAppropriationSearchContract.getSortBy().isEmpty()) {
			validateSortByOrder(estimateAppropriationSearchContract.getSortBy());
			validateEntityFieldName(estimateAppropriationSearchContract.getSortBy(), EstimateAppropriation.class);
		}

		String orderBy = "order by id";
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

		if (estimateAppropriationSearchContract.getObjectType() != null) {
			addAnd(params);
			params.append("objectType =:objecttype");
			paramValues.put("objecttype", estimateAppropriationSearchContract.getObjectType());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(ProjectCode.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
	}

}
