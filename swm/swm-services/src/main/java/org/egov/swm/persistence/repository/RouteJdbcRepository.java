package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.CollectionType;
import org.egov.swm.domain.model.DumpingGround;
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

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<Route> search(final RouteSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), RouteSearch.class);
        }

        String orderBy = "order by name";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getCodes() != null) {
            addAnd(params);
            params.append("code in (:codes)");
            paramValues.put("codes", new ArrayList<>(Arrays.asList(searchRequest.getCodes().split(","))));
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
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<Route>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(RouteEntity.class);

        final List<Route> routeList = new ArrayList<>();

        final List<RouteEntity> routeEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues, row);

        for (final RouteEntity routeEntity : routeEntities) {

            routeList.add(routeEntity.toDomain());
        }

        if (routeList != null && !routeList.isEmpty()) {

            populateCollectionPoints(routeList);

            populateCollectionTypes(routeList);

            populateDumpingGrounds(routeList);
        }

        page.setTotalResults(routeList.size());

        page.setPagedData(routeList);

        return page;
    }

    private void populateCollectionTypes(List<Route> routeList) {

        Map<String, CollectionType> collectionTypeMap = new HashMap<>();
        String tenantId = null;

        if (routeList != null && !routeList.isEmpty())
            tenantId = routeList.get(0).getTenantId();

        List<CollectionType> collectionTypes = collectionTypeService.getAll(tenantId, new RequestInfo());

        for (CollectionType ct : collectionTypes) {
            collectionTypeMap.put(ct.getCode(), ct);
        }

        for (Route route : routeList) {

            if (route.getCollectionType() != null && route.getCollectionType().getCode() != null
                    && !route.getCollectionType().getCode().isEmpty()) {

                route.setCollectionType(collectionTypeMap.get(route.getCollectionType().getCode()));
            }

        }
    }

    private void populateDumpingGrounds(List<Route> routeList) {

        Map<String, DumpingGround> dumpingGroundMap = new HashMap<>();
        String tenantId = null;

        if (routeList != null && !routeList.isEmpty())
            tenantId = routeList.get(0).getTenantId();

        List<DumpingGround> dumpingGrounds = dumpingGroundService.getAll(tenantId, new RequestInfo());

        for (DumpingGround dg : dumpingGrounds) {
            dumpingGroundMap.put(dg.getCode(), dg);
        }

        for (Route route : routeList) {

            if (route.getEndingDumpingGroundPoint() != null && route.getEndingDumpingGroundPoint().getCode() != null
                    && !route.getEndingDumpingGroundPoint().getCode().isEmpty()) {

                route.setEndingDumpingGroundPoint(dumpingGroundMap.get(route.getEndingDumpingGroundPoint().getCode()));
            }

        }
    }

    private void populateCollectionPoints(List<Route> routeList) {

        RouteCollectionPointMap rcpm;
        CollectionPointSearch cps;
        Pagination<CollectionPoint> collectionPointList;
        Map<String, CollectionPoint> collectionPointMap = new HashMap<>();
        Map<String, List<RouteCollectionPointMap>> routeCollectionPointMap = new HashMap<>();
        Map<String, List<CollectionPoint>> collectionPointsMap = new HashMap<>();
        StringBuffer routeCodes = new StringBuffer();
        StringBuffer collectionPointCodes = new StringBuffer();
        Set<String> collectionPointCodeSet = new HashSet<>();
        List<RouteCollectionPointMap> routeCollectionPoints;
        String tenantId = null;

        if (routeList != null && !routeList.isEmpty()) {

            tenantId = routeList.get(0).getTenantId();

            for (Route route : routeList) {

                if (routeCodes.length() > 0)
                    routeCodes.append(",");

                routeCodes.append(route.getCode());

                if (route.getStartingCollectionPoint() != null && route.getStartingCollectionPoint().getCode() != null
                        && !route.getStartingCollectionPoint().getCode().isEmpty()) {

                    collectionPointCodeSet.add(route.getStartingCollectionPoint().getCode());

                }

                if (route.getEndingCollectionPoint() != null && route.getEndingCollectionPoint().getCode() != null
                        && !route.getEndingCollectionPoint().getCode().isEmpty()) {

                    collectionPointCodeSet.add(route.getEndingCollectionPoint().getCode());
                }

            }

            rcpm = new RouteCollectionPointMap();

            rcpm.setTenantId(tenantId);
            rcpm.setRoutes(routeCodes.toString());

            routeCollectionPoints = routeCollectionPointMapJdbcRepository.search(rcpm);

            for (RouteCollectionPointMap map : routeCollectionPoints) {

                if (map.getCollectionPoint() != null && !map.getCollectionPoint().isEmpty()) {

                    collectionPointCodeSet.add(map.getCollectionPoint());
                }

                if (routeCollectionPointMap.get(map.getRoute()) == null) {

                    routeCollectionPointMap.put(map.getRoute(), Collections.singletonList(map));

                } else {

                    List<RouteCollectionPointMap> mapList = new ArrayList<>(routeCollectionPointMap.get(map.getRoute()));

                    mapList.add(map);

                    routeCollectionPointMap.put(map.getRoute(), mapList);

                }
            }

            List<String> cpcs = new ArrayList(collectionPointCodeSet);

            for (String code : cpcs) {

                if (collectionPointCodes.length() > 0)
                    collectionPointCodes.append(",");

                collectionPointCodes.append(code);

            }

            if (collectionPointCodes != null && collectionPointCodes.length() > 0) {

                cps = new CollectionPointSearch();
                cps.setTenantId(tenantId);
                cps.setCodes(collectionPointCodes.toString());

                collectionPointList = collectionPointJdbcRepository.search(cps);

                if (collectionPointList != null && collectionPointList.getPagedData() != null
                        && !collectionPointList.getPagedData().isEmpty()) {

                    for (CollectionPoint cp : collectionPointList.getPagedData()) {
                        collectionPointMap.put(cp.getCode(), cp);
                    }
                }

                for (RouteCollectionPointMap map : routeCollectionPoints) {

                    if (collectionPointsMap.get(map.getRoute()) == null) {

                        collectionPointsMap.put(map.getRoute(),
                                Collections.singletonList(collectionPointMap.get(map.getCollectionPoint())));

                    } else {

                        List<CollectionPoint> cpList = new ArrayList<>(collectionPointsMap.get(map.getRoute()));

                        cpList.add(collectionPointMap.get(map.getCollectionPoint()));

                        collectionPointsMap.put(map.getRoute(), cpList);

                    }
                }

                for (Route route : routeList) {

                    if (route.getStartingCollectionPoint() != null && route.getStartingCollectionPoint().getCode() != null
                            && !route.getStartingCollectionPoint().getCode().isEmpty()) {

                        route.setStartingCollectionPoint(collectionPointMap.get(route.getStartingCollectionPoint().getCode()));

                    }

                    if (route.getEndingCollectionPoint() != null && route.getEndingCollectionPoint().getCode() != null
                            && !route.getEndingCollectionPoint().getCode().isEmpty()) {

                        route.setEndingCollectionPoint(collectionPointMap.get(route.getEndingCollectionPoint().getCode()));
                    }

                    route.setCollectionPoints(collectionPointsMap.get(route.getCode()));

                }

            }

        }

    }

}