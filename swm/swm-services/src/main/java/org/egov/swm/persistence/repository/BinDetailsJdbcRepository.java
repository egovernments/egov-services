package org.egov.swm.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.BinDetails;
import org.egov.swm.domain.model.BinDetailsSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BinDetailsJdbcRepository {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Transactional
	public void delete(String collectionPoint) {
		String delQuery = "delete from  egswm_bindetails where collectionPoint = '" + collectionPoint + "'";
		jdbcTemplate.execute(delQuery);
	}

	public List<BinDetails> search(BinDetailsSearch searchRequest) {

		String searchQuery = "select * from egswm_bindetails :condition ";

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

		if (searchRequest.getRfidAssigned() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("rfidAssigned =:rfidAssigned");
			paramValues.put("rfidAssigned", searchRequest.getRfidAssigned());
		}

		if (searchRequest.getAssetOrBinId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("assetOrBinId =:assetOrBinId");
			paramValues.put("assetOrBinId", searchRequest.getAssetOrBinId());
		}

		if (searchRequest.getCollectionPoint() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("collectionPoint =:collectionPoint");
			paramValues.put("collectionPoint", searchRequest.getCollectionPoint());
		}

		if (searchRequest.getRfid() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("rfid =:rfid");
			paramValues.put("rfid", searchRequest.getRfid());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(BinDetails.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
	}

}