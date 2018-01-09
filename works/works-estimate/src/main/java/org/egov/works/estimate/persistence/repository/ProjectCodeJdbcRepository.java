package org.egov.works.estimate.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.web.contract.ProjectCode;
import org.egov.works.estimate.web.contract.ProjectCodeSearchContract;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class ProjectCodeJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egw_projectcode";

	public List<ProjectCode> search(ProjectCodeSearchContract projectCodeSearchContract) {
		String searchQuery = "select :selectfields from :tablename :condition  :orderby   ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (projectCodeSearchContract.getSortBy() != null && !projectCodeSearchContract.getSortBy().isEmpty()) {
			validateSortByOrder(projectCodeSearchContract.getSortBy());
			validateEntityFieldName(projectCodeSearchContract.getSortBy(), ProjectCode.class);
		}

		StringBuilder orderBy = new StringBuilder("order by code");
		if (projectCodeSearchContract.getSortBy() != null && !projectCodeSearchContract.getSortBy().isEmpty()) {
		    orderBy.delete(0,orderBy.length()).append("order by ").append(projectCodeSearchContract.getSortBy());
		}

		searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		if (projectCodeSearchContract.getIds() != null) {
			addAnd(params);
			params.append("id in(:ids) ");
			paramValues.put("ids", projectCodeSearchContract.getIds());
		}

		if (projectCodeSearchContract.getTenantId() != null) {
			addAnd(params);
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", projectCodeSearchContract.getTenantId());
		}
		if (projectCodeSearchContract.getWorkIdentificationNumbers() != null
				&& projectCodeSearchContract.getWorkIdentificationNumbers().size() == 1) {
			addAnd(params);
			params.append("upper(code) in (:codes)");
			paramValues.put("codes", projectCodeSearchContract.getWorkIdentificationNumbers().get(0).toUpperCase());
		} else if (projectCodeSearchContract.getWorkIdentificationNumbers() != null) {
			params.append("code in (:codes)");
			paramValues.put("codes", projectCodeSearchContract.getWorkIdentificationNumbers());
		}

		if (projectCodeSearchContract.getCodes() != null && projectCodeSearchContract.getCodes().size() == 1) {
			addAnd(params);
			params.append("upper(code) in (:codes)");
			paramValues.put("codes", projectCodeSearchContract.getCodes().get(0).toUpperCase());
		} else if (projectCodeSearchContract.getCodes() != null) {
			params.append("code in (:codes)");
			paramValues.put("codes", projectCodeSearchContract.getCodes());
		}

		if (projectCodeSearchContract.getDetailedEstimateNumbers() != null && projectCodeSearchContract.getDetailedEstimateNumbers().size() == 1) {
			addAnd(params);
			params.append("code in (:detailedEstimateNumbers)");
			paramValues.put("detailedEstimateNumbers", projectCodeSearchContract.getDetailedEstimateNumbers().get(0).toUpperCase());
		} else if (projectCodeSearchContract.getDetailedEstimateNumbers() != null ) {
			addAnd(params);
			params.append("code in (:detailedEstimateNumbers)");
			paramValues.put("detailedEstimateNumbers", projectCodeSearchContract.getDetailedEstimateNumbers());

		}

		if (projectCodeSearchContract.getAbstractEstimateNumbers() != null && projectCodeSearchContract.getAbstractEstimateNumbers().size() == 1) {
			addAnd(params);
			params.append("code in (:abstractEstimateNumbers)");
			paramValues.put("abstractEstimateNumbers", projectCodeSearchContract.getAbstractEstimateNumbers().get(0).toUpperCase());
		} else if (projectCodeSearchContract.getAbstractEstimateNumbers() != null) {
			addAnd(params);
			params.append("code in (:abstractEstimateNumbers)");
			paramValues.put("abstractEstimateNumbers", projectCodeSearchContract.getAbstractEstimateNumbers());
		}


		if (projectCodeSearchContract.getStatuses() != null) {
			addAnd(params);
			params.append("status in (:statuses)");
			paramValues.put("statuses", projectCodeSearchContract.getStatuses());
		}

		if (projectCodeSearchContract.getActive() != null) {
			addAnd(params);
			params.append("active in (:active)");
			paramValues.put("active", projectCodeSearchContract.getActive());
		} else {
			addAnd(params);
			params.append("active in (:active)");
			paramValues.put("active", Boolean.TRUE);
		}

        params.append(" and deleted = false");
		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(ProjectCode.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
	}

}
