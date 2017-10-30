package org.egov.works.estimate.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.persistence.repository.JdbcRepository;
import org.egov.works.estimate.web.contract.ProjectCodeSearchContract;
import org.egov.works.estimate.web.model.ProjectCode;
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

		String orderBy = "order by code";
		if (projectCodeSearchContract.getSortBy() != null && !projectCodeSearchContract.getSortBy().isEmpty()) {
			orderBy = "order by " + projectCodeSearchContract.getSortBy();
		}

		searchQuery = searchQuery.replace(":tablename", TABLE_NAME);

		searchQuery = searchQuery.replace(":selectfields", " * ");

		if (projectCodeSearchContract.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", projectCodeSearchContract.getTenantId());
		}
		if (projectCodeSearchContract.getWorkIdentificationNumbers() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("code in (:code)");
			paramValues.put("code", projectCodeSearchContract.getWorkIdentificationNumbers());
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
