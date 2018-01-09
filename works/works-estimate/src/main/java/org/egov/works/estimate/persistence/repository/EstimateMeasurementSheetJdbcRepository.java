package org.egov.works.estimate.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.persistence.helper.EstimateMeasurementSheetHelper;
import org.egov.works.estimate.web.contract.EstimateMeasurementSheet;
import org.egov.works.estimate.web.contract.EstimateMeasurementSheetSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class EstimateMeasurementSheetJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egw_estimate_measurementsheet";

	public List<EstimateMeasurementSheet> search(
			EstimateMeasurementSheetSearchContract estimateMeasurementSheetSearchContract) {
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (estimateMeasurementSheetSearchContract.getSortBy() != null
				&& !estimateMeasurementSheetSearchContract.getSortBy().isEmpty()) {
			validateSortByOrder(estimateMeasurementSheetSearchContract.getSortBy());
			validateEntityFieldName(estimateMeasurementSheetSearchContract.getSortBy(), EstimateMeasurementSheetHelper.class);
		}

		StringBuilder orderBy = new StringBuilder("order by createdtime");
		if (estimateMeasurementSheetSearchContract.getSortBy() != null
				&& !estimateMeasurementSheetSearchContract.getSortBy().isEmpty()) {
		    orderBy.delete(0,orderBy.length()).append("order by ").append(estimateMeasurementSheetSearchContract.getSortBy());
		}

		searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		if (estimateMeasurementSheetSearchContract.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", estimateMeasurementSheetSearchContract.getTenantId());
		}
		if (estimateMeasurementSheetSearchContract.getIds() != null) {
			addAnd(params);
			params.append("id in(:ids) ");
			paramValues.put("ids", estimateMeasurementSheetSearchContract.getIds());
		}

		if (estimateMeasurementSheetSearchContract.getEstimateActivityIds() != null) {
			addAnd(params);
			params.append("estimateactivity in(:estimateActivityIds) ");
			paramValues.put("estimateActivityIds", estimateMeasurementSheetSearchContract.getEstimateActivityIds());
		}

        params.append(" and deleted = false");
		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(EstimateMeasurementSheetHelper.class);

		List<EstimateMeasurementSheetHelper> estimateMeasurementSheetEntities = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, row);

		List<EstimateMeasurementSheet> estimateMeasurementSheets = new ArrayList<>();

		for (EstimateMeasurementSheetHelper estimateMeasurementSheetEntity : estimateMeasurementSheetEntities) {
			estimateMeasurementSheets.add(estimateMeasurementSheetEntity.toDomain());
		}

		return estimateMeasurementSheets;
	}

}
