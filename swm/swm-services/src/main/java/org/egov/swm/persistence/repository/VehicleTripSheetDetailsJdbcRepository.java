package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.RouteSearch;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.model.VehicleTripSheetDetails;
import org.egov.swm.domain.model.VehicleTripSheetDetailsSearch;
import org.egov.swm.domain.service.RouteService;
import org.egov.swm.domain.service.VehicleService;
import org.egov.swm.persistence.entity.VehicleTripSheetDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VehicleTripSheetDetailsJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_vehicletripsheetdetails";

    @Autowired
    private RouteService routeService;

    @Autowired
    private VehicleService vehicleService;

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<VehicleTripSheetDetails> search(final VehicleTripSheetDetailsSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), VehicleTripSheetDetailsSearch.class);
        }

        String orderBy = "order by tripNo";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getTripNos() != null) {
            addAnd(params);
            params.append("tripNo in(:tripNos) ");
            paramValues.put("tripNos", new ArrayList<>(Arrays.asList(searchRequest.getTripNos().split(","))));
        }

        if (searchRequest.getTripNo() != null) {
            addAnd(params);
            params.append("tripNo =:tripNo");
            paramValues.put("tripNo", searchRequest.getTripNo());
        }

        if (searchRequest.getRegNumber() != null) {
            addAnd(params);
            params.append("vehicle =:vehicle");
            paramValues.put("vehicle", searchRequest.getRegNumber());
        }

        if (searchRequest.getRouteCode() != null) {

            addAnd(params);
            params.append("route =:route");
            paramValues.put("route", searchRequest.getRouteCode());
        }

        if (searchRequest.getTripStartDate() != null) {
            addAnd(params);
            params.append("tripStartDate >=:tripStartDate");
            paramValues.put("tripStartDate", searchRequest.getTripStartDate());
        }

        if (searchRequest.getTripEndDate() != null) {
            addAnd(params);
            params.append("tripEndDate <=:tripEndDate");
            paramValues.put("tripEndDate", searchRequest.getTripEndDate());
        }

        Pagination<VehicleTripSheetDetails> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination

        <VehicleTripSheetDetails>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(VehicleTripSheetDetailsEntity.class);

        final List<VehicleTripSheetDetails> vehicleTripSheetDetailsList = new ArrayList<>();

        final List<VehicleTripSheetDetailsEntity> vehicleTripSheetDetailsEntities = namedParameterJdbcTemplate
                .query(searchQuery.toString(), paramValues, row);

        for (final VehicleTripSheetDetailsEntity vehicleTripSheetDetailsEntity : vehicleTripSheetDetailsEntities) {

            vehicleTripSheetDetailsList.add(vehicleTripSheetDetailsEntity.toDomain());
        }

        if (vehicleTripSheetDetailsList != null && !vehicleTripSheetDetailsList.isEmpty()) {
            populateRoutes(vehicleTripSheetDetailsList);

            populateVehicles(vehicleTripSheetDetailsList);
        }

        page.setPagedData(vehicleTripSheetDetailsList);

        return page;
    }

    private void populateRoutes(List<VehicleTripSheetDetails> vehicleTripSheetDetailsList) {

        StringBuffer routeCodes = new StringBuffer();
        Set<String> routeCodesSet = new HashSet<>();
        RouteSearch routeSearch = new RouteSearch();
        Pagination<Route> routes;

        for (VehicleTripSheetDetails sst : vehicleTripSheetDetailsList) {

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
        if (routeCodes != null && routeCodes.length() > 0) {

            String tenantId = null;
            Map<String, Route> routeMap = new HashMap<>();

            if (vehicleTripSheetDetailsList != null && !vehicleTripSheetDetailsList.isEmpty())
                tenantId = vehicleTripSheetDetailsList.get(0).getTenantId();

            routeSearch.setTenantId(tenantId);
            routeSearch.setCodes(routeCodes.toString());
            routes = routeService.search(routeSearch);

            if (routes != null && routes.getPagedData() != null)
                for (Route bd : routes.getPagedData()) {

                    routeMap.put(bd.getCode(), bd);

                }

            for (VehicleTripSheetDetails vehicleTripSheetDetails : vehicleTripSheetDetailsList) {

                if (vehicleTripSheetDetails.getRoute() != null && vehicleTripSheetDetails.getRoute().getCode() != null
                        && !vehicleTripSheetDetails.getRoute().getCode().isEmpty()) {

                    vehicleTripSheetDetails.setRoute(routeMap.get(vehicleTripSheetDetails.getRoute().getCode()));
                }

            }
        }

    }

    private void populateVehicles(List<VehicleTripSheetDetails> vehicleTripSheetDetailsList) {

        VehicleSearch vehicleSearch;
        Pagination<Vehicle> vehicles;
        StringBuffer vehicleNos = new StringBuffer();
        Set<String> vehicleNoSet = new HashSet<>();

        for (VehicleTripSheetDetails vfd : vehicleTripSheetDetailsList) {

            if (vfd.getVehicle() != null && vfd.getVehicle().getRegNumber() != null
                    && !vfd.getVehicle().getRegNumber().isEmpty()) {

                vehicleNoSet.add(vfd.getVehicle().getRegNumber());

            }

        }

        List<String> vehicleNoList = new ArrayList(vehicleNoSet);

        for (String vehicleNo : vehicleNoList) {

            if (vehicleNos.length() >= 1)
                vehicleNos.append(",");

            vehicleNos.append(vehicleNo);

        }
        if (vehicleNos != null && vehicleNos.length() > 0) {
            String tenantId = null;
            Map<String, Vehicle> vehicleMap = new HashMap<>();

            if (vehicleTripSheetDetailsList != null && !vehicleTripSheetDetailsList.isEmpty())
                tenantId = vehicleTripSheetDetailsList.get(0).getTenantId();

            vehicleSearch = new VehicleSearch();
            vehicleSearch.setTenantId(tenantId);
            vehicleSearch.setRegNumbers(vehicleNos.toString());

            vehicles = vehicleService.search(vehicleSearch);

            if (vehicles != null && vehicles.getPagedData() != null)
                for (Vehicle v : vehicles.getPagedData()) {

                    vehicleMap.put(v.getRegNumber(), v);

                }

            for (VehicleTripSheetDetails vfd : vehicleTripSheetDetailsList) {

                if (vfd.getVehicle() != null && vfd.getVehicle().getRegNumber() != null
                        && !vfd.getVehicle().getRegNumber().isEmpty()) {

                    vfd.setVehicle(vehicleMap.get(vfd.getVehicle().getRegNumber()));
                }

            }
        }

    }

}