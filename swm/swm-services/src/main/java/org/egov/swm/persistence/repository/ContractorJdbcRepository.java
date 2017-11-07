package org.egov.swm.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.Contractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ContractorJdbcRepository extends JdbcRepository {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public Boolean uniqueCheck(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {

		return uniqueCheck("egswm_contractor", tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

	public List<Contractor> search(Contractor searchRequest) {

		String searchQuery = "select * from egswm_contractor :condition ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getContractorNo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("contractorNo =:contractorNo");
			paramValues.put("contractorNo", searchRequest.getContractorNo());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(Contractor.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
	}

}