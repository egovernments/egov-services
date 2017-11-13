package org.egov.swm.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.BinDetails;
import org.egov.swm.domain.model.BinDetailsSearch;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BinDetailsJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_bindetails";

	public Boolean uniqueCheck(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {

		return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

	@Transactional
	public void delete(String tenantId, String collectionPoint) {
		delete(TABLE_NAME, tenantId, "collectionPoint", collectionPoint);
	}

	public List<BinDetails> search(BinDetailsSearch searchRequest) {

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

		if (searchRequest.getLongitude() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("longitude =:longitude");
			paramValues.put("longitude", searchRequest.getLongitude());
		}

		if (searchRequest.getLatitude() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("latitude =:latitude");
			paramValues.put("latitude", searchRequest.getLatitude());
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