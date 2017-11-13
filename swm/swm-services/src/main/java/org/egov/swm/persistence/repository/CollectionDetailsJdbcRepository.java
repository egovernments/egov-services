package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.CollectionDetails;
import org.egov.swm.domain.model.CollectionDetailsSearch;
import org.egov.swm.persistence.entity.CollectionDetailsEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CollectionDetailsJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_collectiondetails";

	@Transactional
	public void delete(String tenantId, String sourceSegregation) {
		delete(TABLE_NAME, tenantId, "sourceSegregation", sourceSegregation);
	}

	public List<CollectionDetails> search(CollectionDetailsSearch searchRequest) {

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

		if (searchRequest.getSourceSegregationCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("sourceSegregation =:sourceSegregation");
			paramValues.put("sourceSegregation", searchRequest.getSourceSegregationCode());
		}

		if (searchRequest.getCollectionTypeCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("collectionType =:collectionType");
			paramValues.put("collectionType", searchRequest.getCollectionTypeCode());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(CollectionDetailsEntity.class);

		List<CollectionDetailsEntity> entityList = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
				row);

		List<CollectionDetails> resultList = new ArrayList<>();

		for (CollectionDetailsEntity cde : entityList) {
			resultList.add(cde.toDomain());
		}

		return resultList;
	}

}