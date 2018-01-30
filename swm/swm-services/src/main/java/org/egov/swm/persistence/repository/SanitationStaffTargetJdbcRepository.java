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
import org.egov.swm.domain.model.DumpingGround;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteCollectionPointMap;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.model.SanitationStaffTarget;
import org.egov.swm.domain.model.SanitationStaffTargetMap;
import org.egov.swm.domain.model.SanitationStaffTargetSearch;
import org.egov.swm.domain.model.SwmProcess;
import org.egov.swm.domain.service.DumpingGroundService;
import org.egov.swm.domain.service.RouteService;
import org.egov.swm.domain.service.SwmProcessService;
import org.egov.swm.persistence.entity.SanitationStaffTargetEntity;
import org.egov.swm.web.contract.Employee;
import org.egov.swm.web.contract.EmployeeResponse;
import org.egov.swm.web.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class SanitationStaffTargetJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_sanitationstafftarget";

    @Autowired
    public SanitationStaffTargetMapJdbcRepository sanitationStaffTargetMapJdbcRepository;

    @Autowired
    public CollectionPointJdbcRepository collectionPointJdbcRepository;

    @Autowired
    private DumpingGroundService dumpingGroundService;

    @Autowired
    private SwmProcessService swmProcessService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RouteService routeService;

    public Pagination<SanitationStaffTarget> search(final SanitationStaffTargetSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), SanitationStaffTargetSearch.class);
        }

        String orderBy = "order by targetNo";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getTargetNo() != null) {
            addAnd(params);
            params.append("targetNo in (:targetNo)");
            paramValues.put("targetNo", searchRequest.getTargetNo());
        }

        if (searchRequest.getTargetNos() != null) {
            addAnd(params);
            params.append("targetNo in (:targetNos)");
            paramValues.put("targetNos", new ArrayList<>(Arrays.asList(searchRequest.getTargetNos().split(","))));
        }
        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getTargetFrom() != null) {
            addAnd(params);
            params.append("targetFrom >=:targetFrom");
            paramValues.put("targetFrom", searchRequest.getTargetFrom());
        }

        if (searchRequest.getTargetTo() != null) {
            addAnd(params);
            params.append("targetTo <=:targetTo");
            paramValues.put("targetTo", searchRequest.getTargetTo());
        }

        if (searchRequest.getRouteCode() != null) {
            addAnd(params);
            params.append("route =:route");
            paramValues.put("route", searchRequest.getRouteCode());
        }

        if (searchRequest.getSwmProcessCode() != null) {
            addAnd(params);
            params.append("swmProcess =:swmProcess");
            paramValues.put("swmProcess", searchRequest.getSwmProcessCode());
        }

        if (searchRequest.getEmployeeCode() != null) {
            addAnd(params);
            params.append("employee =:employee");
            paramValues.put("employee", searchRequest.getEmployeeCode());
        }

        if (searchRequest.getDumpingGroundCode() != null) {
            addAnd(params);
            params.append("dumpingGround =:dumpingGround");
            paramValues.put("dumpingGround", searchRequest.getDumpingGroundCode());
        }

        Pagination<SanitationStaffTarget> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<SanitationStaffTarget>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(SanitationStaffTargetEntity.class);

        final List<SanitationStaffTarget> sanitationStaffTargetList = new ArrayList<>();

        final List<SanitationStaffTargetEntity> sanitationStaffTargetEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (final SanitationStaffTargetEntity sanitationStaffTargetEntity : sanitationStaffTargetEntities) {

            sanitationStaffTargetList.add(sanitationStaffTargetEntity.toDomain());

        }

        if (sanitationStaffTargetList != null && !sanitationStaffTargetList.isEmpty()) {

            populateCollectionPoints(sanitationStaffTargetList);

            populateDumpingGrounds(sanitationStaffTargetList);

            populateSwmProcesses(sanitationStaffTargetList);

            populateEmployees(sanitationStaffTargetList);

            populateRoutes(sanitationStaffTargetList);

            populateSelectedCollectionPoints(sanitationStaffTargetList);

        }
        page.setPagedData(sanitationStaffTargetList);

        return page;
    }

    private void populateSelectedCollectionPoints(List<SanitationStaffTarget> sanitationStaffTargetList) {

        Map<String, CollectionPoint> selectedCollectionPointMap;
        List<CollectionPoint> routeCollectionPoints;
        CollectionPoint cp;
        for (SanitationStaffTarget sst : sanitationStaffTargetList) {

            routeCollectionPoints = new ArrayList<>();
            selectedCollectionPointMap = new HashMap<String, CollectionPoint>();
            for (CollectionPoint scp : sst.getCollectionPoints()) {
                selectedCollectionPointMap.put(scp.getCode(), scp);
            }
            for (RouteCollectionPointMap rcpm : sst.getRoute().getCollectionPoints()) {

                if (rcpm.getCollectionPoint() != null) {
                    cp = rcpm.getCollectionPoint();
                    if (selectedCollectionPointMap.get(cp.getCode()) == null)
                        cp.setIsSelected(false);
                    else {
                        cp.setIsSelected(true);
                    }
                    routeCollectionPoints.add(cp);
                }
            }
            sst.setCollectionPoints(routeCollectionPoints);
        }

    }

    private void populateRoutes(List<SanitationStaffTarget> sanitationStaffTargetList) {

        StringBuffer routeCodes = new StringBuffer();
        Set<String> routeCodesSet = new HashSet<>();
        RouteSearch routeSearch = new RouteSearch();
        Pagination<Route> routes;

        for (SanitationStaffTarget sst : sanitationStaffTargetList) {

            if (sst.getRoute() != null && sst.getRoute().getCode() != null
                    && !sst.getRoute().getCode().isEmpty()) {

                routeCodesSet.add(sst.getRoute().getCode());

            }

        }

        List<String> routeCodeList = new ArrayList(routeCodesSet);

        for (String code : routeCodeList) {

            if (routeCodes.length() >= 1)
                routeCodes.append(",");

            routeCodes.append(code);

        }

        String tenantId = null;
        Map<String, Route> routeMap = new HashMap<>();

        if (sanitationStaffTargetList != null && !sanitationStaffTargetList.isEmpty())
            tenantId = sanitationStaffTargetList.get(0).getTenantId();

        routeSearch.setTenantId(tenantId);
        routeSearch.setCodes(routeCodes.toString());
        routes = routeService.search(routeSearch);

        if (routes != null && routes.getPagedData() != null)
            for (Route bd : routes.getPagedData()) {

                routeMap.put(bd.getCode(), bd);

            }

        for (SanitationStaffTarget sanitationStaffTarget : sanitationStaffTargetList) {

            if (sanitationStaffTarget.getRoute() != null && sanitationStaffTarget.getRoute().getCode() != null
                    && !sanitationStaffTarget.getRoute().getCode().isEmpty()) {

                sanitationStaffTarget.setRoute(routeMap.get(sanitationStaffTarget.getRoute().getCode()));
            }

        }

    }

    private void populateCollectionPoints(List<SanitationStaffTarget> sanitationStaffTargetList) {

        SanitationStaffTargetMap sstm;
        CollectionPointSearch cps;
        Pagination<CollectionPoint> collectionPointList;
        Map<String, CollectionPoint> collectionPointMap = new HashMap<>();
        Map<String, List<SanitationStaffTargetMap>> sanitationStaffTargetMap = new HashMap<>();
        Map<String, List<CollectionPoint>> collectionPointsMap = new HashMap<>();
        StringBuffer targetNos = new StringBuffer();
        StringBuffer collectionPointCodes = new StringBuffer();
        Set<String> collectionPointCodeSet = new HashSet<>();
        List<SanitationStaffTargetMap> targetCollectionPoints;
        String tenantId = null;

        if (sanitationStaffTargetList != null && !sanitationStaffTargetList.isEmpty()) {

            tenantId = sanitationStaffTargetList.get(0).getTenantId();

            for (SanitationStaffTarget sanitationStaffTarget : sanitationStaffTargetList) {

                if (targetNos.length() > 0)
                    targetNos.append(",");

                targetNos.append(sanitationStaffTarget.getTargetNo());

            }

            sstm = new SanitationStaffTargetMap();

            sstm.setTenantId(tenantId);
            sstm.setTargetNos(targetNos.toString());

            targetCollectionPoints = sanitationStaffTargetMapJdbcRepository.search(sstm);
            List<SanitationStaffTargetMap> mapList;
            for (SanitationStaffTargetMap map : targetCollectionPoints) {

                if (map.getCollectionPoint() != null && !map.getCollectionPoint().isEmpty()) {

                    collectionPointCodeSet.add(map.getCollectionPoint());
                }

                if (sanitationStaffTargetMap.get(map.getSanitationStaffTarget()) == null) {

                    sanitationStaffTargetMap.put(map.getSanitationStaffTarget(), Collections.singletonList(map));

                } else {

                    mapList = new ArrayList<>(sanitationStaffTargetMap.get(map.getSanitationStaffTarget()));

                    mapList.add(map);

                    sanitationStaffTargetMap.put(map.getSanitationStaffTarget(), mapList);

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

                List<CollectionPoint> cpList;

                for (SanitationStaffTargetMap map : targetCollectionPoints) {

                    if (collectionPointsMap.get(map.getSanitationStaffTarget()) == null) {

                        collectionPointsMap.put(map.getSanitationStaffTarget(),
                                Collections.singletonList(collectionPointMap.get(map.getCollectionPoint())));

                    } else {

                        cpList = new ArrayList<>(collectionPointsMap.get(map.getSanitationStaffTarget()));

                        cpList.add(collectionPointMap.get(map.getCollectionPoint()));

                        collectionPointsMap.put(map.getSanitationStaffTarget(), cpList);

                    }
                }

                for (SanitationStaffTarget sanitationStaffTarget : sanitationStaffTargetList) {

                    sanitationStaffTarget.setCollectionPoints(collectionPointsMap.get(sanitationStaffTarget.getTargetNo()));

                }
            }
        }

    }

    private void populateEmployees(List<SanitationStaffTarget> sanitationStaffTargetList) {

        StringBuffer employeeCodes = new StringBuffer();
        Set<String> employeeCodesSet = new HashSet<>();

        for (SanitationStaffTarget sst : sanitationStaffTargetList) {

            if (sst.getEmployee() != null && sst.getEmployee().getCode() != null
                    && !sst.getEmployee().getCode().isEmpty()) {

                employeeCodesSet.add(sst.getEmployee().getCode());

            }

        }

        List<String> employeeCodeList = new ArrayList(employeeCodesSet);

        for (String code : employeeCodeList) {

            if (employeeCodes.length() >= 1)
                employeeCodes.append(",");

            employeeCodes.append(code);

        }
        if (employeeCodes != null && employeeCodes.length() > 0) {

            String tenantId = null;
            Map<String, Employee> employeeMap = new HashMap<>();

            if (sanitationStaffTargetList != null && !sanitationStaffTargetList.isEmpty())
                tenantId = sanitationStaffTargetList.get(0).getTenantId();

            EmployeeResponse response = employeeRepository.getEmployeeByCodes(employeeCodes.toString(), tenantId,
                    new RequestInfo());

            if (response != null && response.getEmployees() != null)
                for (Employee e : response.getEmployees()) {

                    employeeMap.put(e.getCode(), e);

                }

            for (SanitationStaffTarget sanitationStaffTarget : sanitationStaffTargetList) {

                if (sanitationStaffTarget.getEmployee() != null && sanitationStaffTarget.getEmployee().getCode() != null
                        && !sanitationStaffTarget.getEmployee().getCode().isEmpty()) {

                    sanitationStaffTarget.setEmployee(employeeMap.get(sanitationStaffTarget.getEmployee().getCode()));
                }

            }

        }

    }

    private void populateSwmProcesses(List<SanitationStaffTarget> sanitationStaffTargetList) {
        Map<String, SwmProcess> swmProcessMap = new HashMap<>();
        String tenantId = null;

        if (sanitationStaffTargetList != null && !sanitationStaffTargetList.isEmpty())
            tenantId = sanitationStaffTargetList.get(0).getTenantId();

        List<SwmProcess> swmProcesses = swmProcessService.getAll(tenantId, new RequestInfo());

        for (SwmProcess sp : swmProcesses) {
            swmProcessMap.put(sp.getCode(), sp);
        }

        for (SanitationStaffTarget sanitationStaffTarget : sanitationStaffTargetList) {

            if (sanitationStaffTarget.getSwmProcess() != null && sanitationStaffTarget.getSwmProcess().getCode() != null
                    && !sanitationStaffTarget.getSwmProcess().getCode().isEmpty()) {

                sanitationStaffTarget.setSwmProcess(swmProcessMap.get(sanitationStaffTarget.getSwmProcess().getCode()));
            }

        }
    }

    private void populateDumpingGrounds(List<SanitationStaffTarget> sanitationStaffTargetList) {

        Map<String, DumpingGround> dumpingGroundMap = new HashMap<>();
        String tenantId = null;

        if (sanitationStaffTargetList != null && !sanitationStaffTargetList.isEmpty())
            tenantId = sanitationStaffTargetList.get(0).getTenantId();

        List<DumpingGround> dumpingGrounds = dumpingGroundService.getAll(tenantId, new RequestInfo());

        for (DumpingGround dg : dumpingGrounds) {
            dumpingGroundMap.put(dg.getCode(), dg);
        }

        for (SanitationStaffTarget sanitationStaffTarget : sanitationStaffTargetList) {

            if (sanitationStaffTarget.getDumpingGround() != null && sanitationStaffTarget.getDumpingGround().getCode() != null
                    && !sanitationStaffTarget.getDumpingGround().getCode().isEmpty()) {

                sanitationStaffTarget.setDumpingGround(dumpingGroundMap.get(sanitationStaffTarget.getDumpingGround().getCode()));
            }

        }
    }

}