package org.egov.swm.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_document";

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Transactional
	public void delete(String tenantId, String refCode) {
		delete(TABLE_NAME, tenantId, "refCode", refCode);
	}

	public List<Document> search(Document searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", searchRequest.getId());
		}

		if (searchRequest.getRefCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("refCode =:refCode");
			paramValues.put("refCode", searchRequest.getRefCode());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(Document.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
	}

}