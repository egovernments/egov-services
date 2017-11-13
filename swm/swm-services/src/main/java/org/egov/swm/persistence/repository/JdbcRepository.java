package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.Pagination;
import org.egov.tracer.model.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public abstract class JdbcRepository {

	private static final Logger LOG = LoggerFactory.getLogger(JdbcRepository.class);

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Transactional
	public void delete(String tableName, String tenantId, String fieldName, String fieldValue) {

		String delQuery = "delete from " + tableName + " where tenantId = '" + tenantId + "' and " + fieldName + " = '"
				+ fieldValue + "'";

		jdbcTemplate.execute(delQuery);
	}

	public Pagination<?> getPagination(String searchQuery, Pagination<?> page, Map<String, Object> paramValues) {

		String countQuery = "select count(*) from (" + searchQuery + ") as x";
		Long count = namedParameterJdbcTemplate.queryForObject(countQuery.toString(), paramValues, Long.class);
		Integer totalpages = (int) Math.ceil((double) count / page.getPageSize());
		page.setTotalPages(totalpages);
		page.setCurrentPage(page.getOffset());

		return page;
	}

	public void validateSortByOrder(final String sortBy) {

		List<String> sortByList = new ArrayList<String>();
		if (sortBy.contains(",")) {
			sortByList = Arrays.asList(sortBy.split(","));
		} else {
			sortByList = Arrays.asList(sortBy);
		}
		for (String s : sortByList) {
			if (s.contains(" ")
					&& (!s.toLowerCase().trim().endsWith("asc") && !s.toLowerCase().trim().endsWith("desc"))) {

				throw new CustomException(s.split(" ")[0],
						"Please send the proper sortBy order for the field " + s.split(" ")[0]);
			}
		}

	}

	public void validateEntityFieldName(String sortBy, final Class<?> object) {

		List<String> sortByList = new ArrayList<String>();
		if (sortBy.contains(",")) {
			sortByList = Arrays.asList(sortBy.split(","));
		} else {
			sortByList = Arrays.asList(sortBy);
		}
		Boolean isFieldExist = Boolean.FALSE;
		for (String s : sortByList) {
			for (int i = 0; i < object.getDeclaredFields().length; i++) {
				if (object.getDeclaredFields()[i].getName().equals(s.contains(" ") ? s.split(" ")[0] : s)) {
					isFieldExist = Boolean.TRUE;
					break;
				} else {
					isFieldExist = Boolean.FALSE;
				}
			}
			if (!isFieldExist) {
				throw new CustomException(s.contains(" ") ? s.split(" ")[0] : s, "Please send the proper Field Names ");

			}
		}

	}

	public Boolean uniqueCheck(String tableName, String tenantId, String fieldName, String fieldValue,
			String uniqueFieldName, String uniqueFieldValue) {

		LOG.info("Unique Checking for combination of fields tenantId & " + fieldValue);

		Map<String, Object> paramValues = new HashMap<>();

		StringBuffer uniqueQuery = new StringBuffer("select count(*) as count from " + tableName
				+ " where tenantId=:tenantId and " + fieldName + "=:fieldValue ");

		if (uniqueFieldValue != null) {
			uniqueQuery.append(" and " + uniqueFieldName + "!=:uniqueFieldValue");
			paramValues.put("uniqueFieldValue", uniqueFieldValue);
		}

		paramValues.put("tenantId", tenantId);
		paramValues.put("fieldValue", fieldValue);

		Long count = namedParameterJdbcTemplate.queryForObject(uniqueQuery.toString(), paramValues, Long.class);

		LOG.info("Record Count for combination of fields " + count);

		return count >= 1 ? false : true;

	}

	public void addAnd(StringBuffer params) {
		if (params.length() > 0) {
			params.append(" and ");
		}
	}
}
