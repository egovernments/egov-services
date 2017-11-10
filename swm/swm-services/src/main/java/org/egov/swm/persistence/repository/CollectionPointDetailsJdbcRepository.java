package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.CollectionPointDetails;
import org.egov.swm.domain.model.CollectionPointDetailsSearch;
import org.egov.swm.persistence.entity.CollectionPointDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CollectionPointDetailsJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_collectionpointdetails";
	
	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Transactional
	public void delete(String tenantId, String collectionPoint) {
		delete(TABLE_NAME, tenantId, "collectionPoint", collectionPoint);
	}

	public List<CollectionPointDetails> search(CollectionPointDetailsSearch searchRequest) {

		String searchQuery = "select * from "+TABLE_NAME+" :condition ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), CollectionPointDetailsSearch.class);
		}

		if (searchRequest.getIds() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id in (:ids)");
			paramValues.put("ids", new ArrayList<String>(Arrays.asList(searchRequest.getIds().split(","))));
		}
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

		if (searchRequest.getCollectionPoint() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("collectionPoint =:collectionPoint");
			paramValues.put("collectionPoint", searchRequest.getCollectionPoint());
		}

		if (searchRequest.getCollectionTypeCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("collectionType =:collectionType");
			paramValues.put("collectionType", searchRequest.getCollectionTypeCode());
		}

		if (searchRequest.getGarbageEstimate() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("garbageEstimate =:garbageEstimate");
			paramValues.put("garbageEstimate", searchRequest.getGarbageEstimate());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(CollectionPointDetailsEntity.class);

		List<CollectionPointDetails> collectionPointDetailsList = new ArrayList<>();

		List<CollectionPointDetailsEntity> collectionPointDetailsEntities = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, row);

		for (CollectionPointDetailsEntity collectionPointDetailsEntity : collectionPointDetailsEntities) {
			collectionPointDetailsList.add(collectionPointDetailsEntity.toDomain());
		}

		return collectionPointDetailsList;
	}

}