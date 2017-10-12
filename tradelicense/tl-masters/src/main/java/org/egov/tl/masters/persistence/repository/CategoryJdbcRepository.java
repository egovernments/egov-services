package org.egov.tl.masters.persistence.repository;

import org.egov.tradelicense.persistence.repository.builder.CategoryQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CategoryJdbcRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public boolean validateIdExistance(Long id, Long parentId, String tenantId) {

		Long count = 0l;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = CategoryQueryBuilder.getQueryCategoryValidation(id, parentId, tenantId, parameters);
		count = namedParameterJdbcTemplate.queryForObject(query, parameters, Long.class);
		if (count.equals(0l) ) {
			return false;
		} else {
			return true;
		}

	}
	
	public boolean validateCodeExistance(String code, String parent, String tenantId) {

		Long count = 0l;
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = CategoryQueryBuilder.getQueryCategoryValidationWithCode(code, parent, tenantId, parameters);
		count = namedParameterJdbcTemplate.queryForObject(query, parameters, Long.class);
		if (count.equals(0l)) {
			return false;
		} else {
			return true;
		}

	}

}
