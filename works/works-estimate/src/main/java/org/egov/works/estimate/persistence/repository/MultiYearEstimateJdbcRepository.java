package org.egov.works.estimate.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.persistence.helper.MultiYearEstimateHelper;
import org.egov.works.estimate.web.contract.MultiYearEstimate;
import org.egov.works.estimate.web.contract.MultiYearEstimateSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class MultiYearEstimateJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egw_multiyear_estimate";

	public List<MultiYearEstimate> search(
			MultiYearEstimateSearchContract multiYearEstimateSearchContract) {
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (multiYearEstimateSearchContract.getSortBy() != null
				&& !multiYearEstimateSearchContract.getSortBy().isEmpty()) {
			validateSortByOrder(multiYearEstimateSearchContract.getSortBy());
			validateEntityFieldName(multiYearEstimateSearchContract.getSortBy(), MultiYearEstimateHelper.class);
		}

		StringBuilder orderBy = new StringBuilder("order by createdtime");
		if (multiYearEstimateSearchContract.getSortBy() != null
				&& !multiYearEstimateSearchContract.getSortBy().isEmpty()) {
		    orderBy.delete(0,orderBy.length()).append("order by ").append(multiYearEstimateSearchContract.getSortBy());
		}

		searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		if (multiYearEstimateSearchContract.getTenantId() != null) {
			addAnd(params);
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", multiYearEstimateSearchContract.getTenantId());
		}
		if (multiYearEstimateSearchContract.getIds() != null) {
			addAnd(params);
			params.append("id in(:ids) ");
			paramValues.put("ids", multiYearEstimateSearchContract.getIds());
		}

		if (multiYearEstimateSearchContract.getDetailedEstimateIds() != null) {
			addAnd(params);
			params.append("detailedEstimate in(:detailedEstimateIds) ");
			paramValues.put("detailedEstimateIds", multiYearEstimateSearchContract.getDetailedEstimateIds());
		}

        params.append(" and deleted = false");
		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(MultiYearEstimateHelper.class);

		List<MultiYearEstimateHelper> multiYearEstimateEntities = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, row);

		List<MultiYearEstimate> multiYearEstimates = new ArrayList<>();

		for (MultiYearEstimateHelper multiYearEstimateEntity : multiYearEstimateEntities) {
			multiYearEstimates.add(multiYearEstimateEntity.toDomain());
		}

		return multiYearEstimates;
	}

}
