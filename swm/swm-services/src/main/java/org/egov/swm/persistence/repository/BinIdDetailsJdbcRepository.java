package org.egov.swm.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.BinIdDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BinIdDetailsJdbcRepository {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Transactional
	public void delete(String collectionPointId) {
		String delQuery = "delete from  egswm_biniddetails where collectionPointId = '" + collectionPointId + "'";
		jdbcTemplate.execute(delQuery);
	}

	public List<BinIdDetails> search(BinIdDetails searchRequest) {

		String searchQuery = "select * from egswm_biniddetails :condition ";

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

		if (searchRequest.getCollectionPointId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("collectionPointId =:collectionPointId");
			paramValues.put("collectionPointId", searchRequest.getCollectionPointId());
		}

		if (searchRequest.getLatitude() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("latitude =:latitude");
			paramValues.put("latitude", searchRequest.getLatitude());
		}

		if (searchRequest.getLongitude() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("longitude =:longitude");
			paramValues.put("longitude", searchRequest.getLongitude());
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

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(BinIdDetails.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);
	}

}