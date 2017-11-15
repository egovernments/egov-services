package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.constants.Constants;
import org.egov.swm.domain.model.CollectionDetails;
import org.egov.swm.domain.model.CollectionDetailsSearch;
import org.egov.swm.domain.model.CollectionType;
import org.egov.swm.persistence.entity.CollectionDetailsEntity;
import org.egov.swm.web.repository.MdmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONArray;

@Service
public class CollectionDetailsJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_collectiondetails";

	@Autowired
	private MdmsRepository mdmsRepository;

	@Transactional
	public void delete(String tenantId, String sourceSegregation) {
		delete(TABLE_NAME, tenantId, "sourceSegregation", sourceSegregation);
	}

	public List<CollectionDetails> search(CollectionDetailsSearch searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getTenantId() != null) {
			addAnd(params);
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getSourceSegregationCode() != null) {
			addAnd(params);
			params.append("sourceSegregation =:sourceSegregation");
			paramValues.put("sourceSegregation", searchRequest.getSourceSegregationCode());
		}

		if (searchRequest.getCollectionTypeCode() != null) {
			addAnd(params);
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

		JSONArray responseJSONArray = null;
		ObjectMapper mapper = new ObjectMapper();
		CollectionDetails collectionDetails;

		for (CollectionDetailsEntity cde : entityList) {

			collectionDetails = cde.toDomain();

			if (collectionDetails.getCollectionType() != null
					&& collectionDetails.getCollectionType().getCode() != null) {

				responseJSONArray = mdmsRepository.getByCriteria(collectionDetails.getTenantId(), Constants.MODULE_CODE,
						Constants.COLLECTIONTYPE_MASTER_NAME, "code", collectionDetails.getCollectionType().getCode(),
						new RequestInfo());

				if (responseJSONArray != null && responseJSONArray.size() > 0)
					collectionDetails
							.setCollectionType(mapper.convertValue(responseJSONArray.get(0), CollectionType.class));

			}
			resultList.add(collectionDetails);
		}

		return resultList;
	}

}