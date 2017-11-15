package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteCollectionPointMap;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.service.CollectionTypeService;
import org.egov.swm.domain.service.DumpingGroundService;
import org.egov.swm.persistence.entity.RouteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class RouteJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_route";

	@Autowired
	public RouteCollectionPointMapJdbcRepository routeCollectionPointMapJdbcRepository;

	@Autowired
	public CollectionPointJdbcRepository collectionPointJdbcRepository;

	@Autowired
	private CollectionTypeService collectionTypeService;

	@Autowired
	private DumpingGroundService dumpingGroundService;

	public Boolean uniqueCheck(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {

		return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

	public Pagination<Route> search(RouteSearch searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), RouteSearch.class);
		}

		String orderBy = "order by name";
		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			orderBy = "order by " + searchRequest.getSortBy();
		}

		if (searchRequest.getCodes() != null) {
			addAnd(params);
			params.append("code in (:codes)");
			paramValues.put("codes", new ArrayList<String>(Arrays.asList(searchRequest.getCodes().split(","))));
		}

		if (searchRequest.getTenantId() != null) {
			addAnd(params);
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getCode() != null) {
			addAnd(params);
			params.append("code =:code");
			paramValues.put("code", searchRequest.getCode());
		}

		if (searchRequest.getName() != null) {
			addAnd(params);
			params.append("name =:name");
			paramValues.put("name", searchRequest.getName());
		}

		if (searchRequest.getCollectionTypeCode() != null) {
			addAnd(params);
			params.append("collectionType =:collectionType");
			paramValues.put("collectionType", searchRequest.getCollectionTypeCode());
		}

		if (searchRequest.getEndingCollectionPointCode() != null) {
			addAnd(params);
			params.append("endingCollectionPoint =:endingCollectionPoint");
			paramValues.put("endingCollectionPoint", searchRequest.getEndingCollectionPointCode());
		}

		if (searchRequest.getEndingDumpingGroundPointCode() != null) {
			addAnd(params);
			params.append("endingDumpingGroundPoint =:endingDumpingGroundPoint");
			paramValues.put("endingDumpingGroundPoint", searchRequest.getEndingDumpingGroundPointCode());
		}

		if (searchRequest.getDistance() != null) {
			addAnd(params);
			params.append("distance =:distance");
			paramValues.put("distance", searchRequest.getDistance());
		}

		if (searchRequest.getGarbageEstimate() != null) {
			addAnd(params);
			params.append("gabageEstimate =:garbageEstimate");
			paramValues.put("garbageEstimate", searchRequest.getGarbageEstimate());
		}

		Pagination<Route> page = new Pagination<>();
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

		page = (Pagination<Route>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(RouteEntity.class);

		List<Route> routeList = new ArrayList<>();

		List<RouteEntity> routeEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

		StringBuffer cpCodes = new StringBuffer();
		RouteCollectionPointMap sr;
		List<RouteCollectionPointMap> collectionPoints;
		Route route;
		CollectionPointSearch cps;
		Pagination<CollectionPoint> collectionPointList;
		for (RouteEntity routeEntity : routeEntities) {

			route = routeEntity.toDomain();

			if (route.getCollectionType() != null && route.getCollectionType().getCode() != null) {

				route.setCollectionType(collectionTypeService.getCollectionType(route.getTenantId(),
						route.getCollectionType().getCode(), new RequestInfo()));

			}

			if (route.getStartingCollectionPoint() != null && route.getStartingCollectionPoint().getCode() != null) {
				cps = new CollectionPointSearch();
				cps.setTenantId(route.getTenantId());
				cps.setCode(route.getStartingCollectionPoint().getCode());

				collectionPointList = collectionPointJdbcRepository.search(cps);

				if (collectionPointList != null && collectionPointList.getPagedData() != null
						&& !collectionPointList.getPagedData().isEmpty())
					route.setStartingCollectionPoint(collectionPointList.getPagedData().get(0));

			}

			if (route.getEndingCollectionPoint() != null && route.getEndingCollectionPoint().getCode() != null) {

				cps = new CollectionPointSearch();
				cps.setTenantId(route.getTenantId());
				cps.setCode(route.getEndingCollectionPoint().getCode());

				collectionPointList = collectionPointJdbcRepository.search(cps);

				if (collectionPointList != null && collectionPointList.getPagedData() != null
						&& !collectionPointList.getPagedData().isEmpty())
					route.setEndingCollectionPoint(collectionPointList.getPagedData().get(0));

			}

			if (route.getEndingDumpingGroundPoint() != null && route.getEndingDumpingGroundPoint().getCode() != null) {

				dumpingGroundService.getDumpingGround(route.getTenantId(),
						route.getEndingDumpingGroundPoint().getCode(), new RequestInfo());
			}

			sr = RouteCollectionPointMap.builder().route(routeEntity.getCode()).build();
			collectionPoints = routeCollectionPointMapJdbcRepository.search(sr);

			if (collectionPoints != null)
				for (RouteCollectionPointMap map : collectionPoints) {
					if (cpCodes.length() > 0)
						cpCodes.append(",");
					cpCodes.append(map.getCollectionPoint());
				}

			cps = new CollectionPointSearch();
			cps.setCodes(cpCodes.toString());
			collectionPointList = collectionPointJdbcRepository.search(cps);

			route.setCollectionPoints(collectionPointList.getPagedData());
			routeList.add(route);
		}

		page.setTotalResults(routeList.size());

		page.setPagedData(routeList);

		return page;
	}

}