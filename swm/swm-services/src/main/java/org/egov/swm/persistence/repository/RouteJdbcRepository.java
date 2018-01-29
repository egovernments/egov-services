package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.CollectionPoint;
import org.egov.swm.domain.model.CollectionPointSearch;
import org.egov.swm.domain.model.CollectionType;
import org.egov.swm.domain.model.DumpingGround;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteCollectionPointMap;
import org.egov.swm.domain.model.RouteCollectionPointMapSearch;
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

        if (searchRequest.getTotalDistance() != null) {
            addAnd(params);
            params.append("totalDistance = :totalDistance");
            paramValues.put("totalDistance", searchRequest.getTotalDistance());
        }

        if (searchRequest.getTotalGarbageEstimate() != null) {
            addAnd(params);
            params.append("totalGarbageEstimate = :totalGarbageEstimate");
            paramValues.put("totalGarbageEstimate", searchRequest.getTotalGarbageEstimate());
        }

        if (searchRequest.getCollectionTypeCode() != null) {
            addAnd(params);
            params.append("collectionType =:collectionType");
            paramValues.put("collectionType", searchRequest.getCollectionTypeCode());
        }

        if (searchRequest.getCollectionPointCode() != null) {

            RouteCollectionPointMapSearch routeCollectionPointMap = new RouteCollectionPointMapSearch();
            routeCollectionPointMap.setCollectionPointCode(searchRequest.getCollectionPointCode());
            routeCollectionPointMap.setTenantId(searchRequest.getTenantId());

            List<RouteCollectionPointMap> routeCollectionPointMaps = routeCollectionPointMapJdbcRepository
                    .search(routeCollectionPointMap);
            List<String> routeCodes = routeCollectionPointMaps.stream()
                    .map(RouteCollectionPointMap::getRoute)
                    .collect(Collectors.toList());
            if (routeCodes != null && !routeCodes.isEmpty()) {
                addAnd(params);
                params.append("code in (:routecodes) ");
                paramValues.put("routecodes", routeCodes);
            } else {
                addAnd(params);
                params.append("code = '' ");
            }

        }

        if (searchRequest.getDumpingGroundCode() != null) {

            RouteCollectionPointMapSearch routeCollectionPointMap = new RouteCollectionPointMapSearch();
            routeCollectionPointMap.setDumpingGroundCode(searchRequest.getDumpingGroundCode());
            routeCollectionPointMap.setTenantId(searchRequest.getTenantId());

            List<RouteCollectionPointMap> routeCollectionPointMaps = routeCollectionPointMapJdbcRepository
                    .search(routeCollectionPointMap);
            List<String> routeCodes = routeCollectionPointMaps.stream()
                    .map(RouteCollectionPointMap::getRoute)
                    .collect(Collectors.toList());
            if (routeCodes != null && !routeCodes.isEmpty()) {
                addAnd(params);
                params.append("code in (:routecodes) ");
                paramValues.put("routecodes", routeCodes);
            } else {
                addAnd(params);
                params.append("code = '' ");
            }

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
        StringBuffer routeCodes = new StringBuffer();

        for (final RouteEntity routeEntity : routeEntities) {

            routeList.add(routeEntity.toDomain());

            if (routeCodes.length() >= 1)
                routeCodes.append(",");

            routeCodes.append(routeEntity.getCode());
        }

        if (routeList != null && !routeList.isEmpty()) {

            populateCollectionTypes(routeList);

            populateRouteCollectionPointMaps(routeList, routeCodes.toString());

            populateTotalNoOfStops(routeList);

        }

        page.setPagedData(routeList);

        return page;
    }

    private void populateTotalNoOfStops(List<Route> routeList) {

        for (Route route : routeList) {

            if (route.getCollectionPoints() != null && route.getCollectionPoints().size() > 2) {

                route.setTotalNoOfStops(route.getCollectionPoints().size() - 2);
            }
        }

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

    private void populateRouteCollectionPointMaps(List<Route> routeList, String routeCodes) {
        Map<String, List<RouteCollectionPointMap>> routeCollectionPointMap = new HashMap<>();
        Map<String, DumpingGround> dumpingGroundMap = new HashMap<>();
        Map<String, CollectionPoint> collectionPointMap = new HashMap<>();
        CollectionPointSearch cps;
        String tenantId = null;
        RouteCollectionPointMapSearch rcpms;
        StringBuffer collectionPointCodes = new StringBuffer();
        Set<String> collectionPointCodeSet = new HashSet<>();
        Pagination<CollectionPoint> collectionPointList;

        if (routeList != null && !routeList.isEmpty())
            tenantId = routeList.get(0).getTenantId();

        rcpms = new RouteCollectionPointMapSearch();

        rcpms.setTenantId(tenantId);
        rcpms.setRoutes(routeCodes);

        List<RouteCollectionPointMap> routeCollectionPoints = routeCollectionPointMapJdbcRepository.search(rcpms);

        if (routeCollectionPoints != null) {
            for (RouteCollectionPointMap map : routeCollectionPoints) {
                if (map.getCollectionPoint() != null && map.getCollectionPoint().getCode() != null
                        && !map.getCollectionPoint().getCode().isEmpty()) {

                    collectionPointCodeSet.add(map.getCollectionPoint().getCode());
                }
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
        }

        List<DumpingGround> dumpingGrounds = dumpingGroundService.getAll(tenantId, new RequestInfo());
        if (dumpingGrounds != null)
            for (DumpingGround dg : dumpingGrounds) {
                dumpingGroundMap.put(dg.getCode(), dg);
            }
        List<RouteCollectionPointMap> rcpmList;
        if (routeCollectionPoints != null)
            for (RouteCollectionPointMap rcp : routeCollectionPoints) {

                if (rcp.getDumpingGround() != null && rcp.getDumpingGround().getCode() != null
                        && !rcp.getDumpingGround().getCode().isEmpty()) {

                    rcp.setDumpingGround(dumpingGroundMap.get(rcp.getDumpingGround().getCode()));
                }

                if (rcp.getCollectionPoint() != null && rcp.getCollectionPoint().getCode() != null
                        && !rcp.getCollectionPoint().getCode().isEmpty()) {

                    rcp.setCollectionPoint(collectionPointMap.get(rcp.getCollectionPoint().getCode()));
                }

                if (routeCollectionPointMap.get(rcp.getRoute()) == null) {

                    routeCollectionPointMap.put(rcp.getRoute(), Collections.singletonList(rcp));

                } else {

                    rcpmList = new ArrayList<>(routeCollectionPointMap.get(rcp.getRoute()));

                    rcpmList.add(rcp);

                    routeCollectionPointMap.put(rcp.getRoute(), rcpmList);

                }
            }

        for (Route route : routeList) {

            route.setCollectionPoints(routeCollectionPointMap.get(route.getCode()));

        }

    }

}
