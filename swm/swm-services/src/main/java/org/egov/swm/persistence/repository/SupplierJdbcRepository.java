package org.egov.swm.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SupplierJdbcRepository extends JdbcRepository {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public Boolean uniqueCheck(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {

		return uniqueCheck("egswm_supplier", tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

	public List<Supplier> search(Supplier searchRequest) {

		String searchQuery = "select * from egswm_supplier :condition ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSupplierNo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("supplierNo =:supplierNo");
			paramValues.put("supplierNo", searchRequest.getSupplierNo());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(Supplier.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
	}

}