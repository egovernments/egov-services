package org.egov.swm.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.ServicesOffered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicesOfferedJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_vendorservicedlocations";

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Transactional
	public void delete(String tenantId, String vendor) {
		delete(TABLE_NAME, tenantId, "vendor", vendor);
	}

	public List<ServicesOffered> search(ServicesOffered searchRequest) {

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

		if (searchRequest.getService() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("service =:service");
			paramValues.put("service", searchRequest.getService());
		}

		if (searchRequest.getVendor() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("vendor =:vendor");
			paramValues.put("vendor", searchRequest.getVendor());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(ServicesOffered.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
	}

}