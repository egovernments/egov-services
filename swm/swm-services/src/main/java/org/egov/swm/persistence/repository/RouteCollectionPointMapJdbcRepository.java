package org.egov.swm.persistence.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.RouteCollectionPointMap;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteCollectionPointMapJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_routecollectionpointmap";

	@Transactional
	public void delete(String tenantId, String route) {
		delete(TABLE_NAME, tenantId, "refCode", route);
	}

	public List<RouteCollectionPointMap> search(RouteCollectionPointMap searchRequest) {

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

		if (searchRequest.getRoute() != null && searchRequest.getRoute() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("route =:route");
			paramValues.put("route", searchRequest.getRoute());
		}

		if (searchRequest.getCollectionPoint() != null && searchRequest.getCollectionPoint() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("collectionPoint =:collectionPoint");
			paramValues.put("collectionPoint", searchRequest.getCollectionPoint());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(RouteCollectionPointMap.class);

		return namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

	}

}