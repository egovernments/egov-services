package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.BinDetailsSearch;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointDetailsSearch;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.persistence.entity.CollectionPointEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class CollectionPointJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_collectionpoint";

	@Autowired
	public BinDetailsJdbcRepository binIdDetailsJdbcRepository;

	@Autowired
	public CollectionPointDetailsJdbcRepository collectionPointDetailsJdbcRepository;

	public Boolean uniqueCheck(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {

		return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

	public Pagination<CollectionPoint> search(CollectionPointSearch searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), CollectionPointSearch.class);
		}

		String orderBy = "order by name";
		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			orderBy = "order by " + searchRequest.getSortBy();
		}

		if (searchRequest.getCodes() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("code in (:codes)");
			paramValues.put("codes", new ArrayList<String>(Arrays.asList(searchRequest.getCodes().split(","))));
		}
		if (searchRequest.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("code =:code");
			paramValues.put("code", searchRequest.getCode());
		}

		if (searchRequest.getName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("name =:name");
			paramValues.put("name", searchRequest.getName());
		}

		if (searchRequest.getLocationCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("location =:location");
			paramValues.put("location", searchRequest.getLocationCode());
		}

		Pagination<CollectionPoint> page = new Pagination<>();
		if (searchRequest.getOffset() != null) {
			page.setOffset(searchRequest.getOffset());
		}
		if (searchRequest.getPageSize() != null) {
			page.setPageSize(searchRequest.getPageSize());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = (Pagination<CollectionPoint>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(CollectionPointEntity.class);

		List<CollectionPoint> collectionPointList = new ArrayList<>();

		List<CollectionPointEntity> collectionPointEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
				paramValues, row);
		CollectionPoint cp;
		BinDetailsSearch bds;
		CollectionPointDetailsSearch cpds;
		for (CollectionPointEntity collectionPointEntity : collectionPointEntities) {

			cp = collectionPointEntity.toDomain();

			bds = new BinDetailsSearch();
			bds.setCollectionPoint(cp.getCode());
			bds.setTenantId(cp.getTenantId());
			cp.setBinDetails(binIdDetailsJdbcRepository.search(bds));
			collectionPointList.add(cp);

			cpds = new CollectionPointDetailsSearch();
			cpds.setTenantId(cp.getTenantId());
			cpds.setCollectionPoint(cp.getCode());

			cp.setCollectionPointDetails(collectionPointDetailsJdbcRepository.search(cpds));
		}

		page.setTotalResults(collectionPointList.size());

		page.setPagedData(collectionPointList);

		return page;
	}

}