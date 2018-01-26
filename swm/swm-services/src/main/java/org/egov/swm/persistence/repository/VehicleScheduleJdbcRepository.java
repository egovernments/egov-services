package org.egov.swm.persistence.repository;

import java.sql.Date;
import java.text.SimpleDateFormat;
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
import org.egov.swm.domain.model.VehicleSchedule;
import org.egov.swm.domain.model.VehicleScheduleSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.service.RouteService;
import org.egov.swm.domain.service.VehicleService;
import org.egov.swm.persistence.entity.VehicleScheduleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VehicleScheduleJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_vehicleschedule";
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private RouteService routeService;

    public Boolean uniqueCheck(final String tenantId, final String fieldName, final String fieldValue,
            final String uniqueFieldName,
            final String uniqueFieldValue) {

        return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
    }

    public Pagination<VehicleSchedule> search(final VehicleScheduleSearch searchRequest) {

        String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

        final Map<String, Object> paramValues = new HashMap<>();
        final StringBuffer params = new StringBuffer();

        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            validateSortByOrder(searchRequest.getSortBy());
            validateEntityFieldName(searchRequest.getSortBy(), VehicleScheduleSearch.class);
        }

        String orderBy = "order by transactionNo";
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty())
            orderBy = "order by " + searchRequest.getSortBy();

        if (searchRequest.getTransactionNo() != null) {
            addAnd(params);
            params.append("transactionNo in (:transactionNo)");
            paramValues.put("transactionNo", searchRequest.getTransactionNo());
        }

        if (searchRequest.getTransactionNos() != null) {
            addAnd(params);
            params.append("transactionNo in (:transactionNos)");
            paramValues.put("transactionNos",
                    new ArrayList<>(Arrays.asList(searchRequest.getTransactionNos().split(","))));
        }
        if (searchRequest.getTenantId() != null) {
            addAnd(params);
            params.append("tenantId =:tenantId");
            paramValues.put("tenantId", searchRequest.getTenantId());
        }

        if (searchRequest.getRouteCode() != null) {
            addAnd(params);
            params.append("route =:route");
            paramValues.put("route", searchRequest.getRouteCode());
        }

        if (searchRequest.getRegNumber() != null) {
            addAnd(params);
            params.append("vehicle =:vehicle");
            paramValues.put("vehicle", searchRequest.getRegNumber());
        }

        if (searchRequest.getFromTripSheet() != null && searchRequest.getFromTripSheet()) {

            if (searchRequest.getScheduledFrom() != null) {
                addAnd(params);
                params.append(
                        "to_char((to_timestamp(scheduledfrom/1000) AT TIME ZONE 'Asia/Kolkata')::date,'yyyy-mm-dd') <=:scheduledFrom");
                paramValues.put("scheduledFrom", sdf.format(new Date(searchRequest.getScheduledFrom())));
            }

            if (searchRequest.getScheduledTo() != null) {
                addAnd(params);
                params.append(
                        "to_char((to_timestamp(scheduledto/1000) AT TIME ZONE 'Asia/Kolkata')::date,'yyyy-mm-dd') >=:scheduledTo");
                paramValues.put("scheduledTo", sdf.format(new Date(searchRequest.getScheduledTo())));
            }
        } else {
            if (searchRequest.getScheduledFrom() != null) {
                addAnd(params);
                params.append(
                        "to_char((to_timestamp(scheduledfrom/1000) AT TIME ZONE 'Asia/Kolkata')::date,'yyyy-mm-dd') >=:scheduledFrom");
                paramValues.put("scheduledFrom", sdf.format(new Date(searchRequest.getScheduledFrom())));
            }

            if (searchRequest.getScheduledTo() != null) {
                addAnd(params);
                params.append(
                        "to_char((to_timestamp(scheduledto/1000) AT TIME ZONE 'Asia/Kolkata')::date,'yyyy-mm-dd') <=:scheduledTo");
                paramValues.put("scheduledTo", sdf.format(new Date(searchRequest.getScheduledTo())));
            }
        }

        Pagination<VehicleSchedule> page = new Pagination<>();
        if (searchRequest.getOffset() != null)
            page.setOffset(searchRequest.getOffset());
        if (searchRequest.getPageSize() != null)
            page.setPageSize(searchRequest.getPageSize());

        if (params.length() > 0)
            searchQuery = searchQuery.replace(":condition", " where " + params.toString());
        else

            searchQuery = searchQuery.replace(":condition", "");

        searchQuery = searchQuery.replace(":orderby", orderBy);

        page = (Pagination<VehicleSchedule>) getPagination(searchQuery, page, paramValues);
        searchQuery = searchQuery + " :pagination";

        searchQuery = searchQuery.replace(":pagination",
                "limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

        final BeanPropertyRowMapper row = new BeanPropertyRowMapper(VehicleScheduleEntity.class);

        final List<VehicleSchedule> vehicleScheduleList = new ArrayList<>();

        final List<VehicleScheduleEntity> vehicleScheduleEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
                paramValues, row);

        for (final VehicleScheduleEntity vehicleScheduleEntity : vehicleScheduleEntities) {

            vehicleScheduleList.add(vehicleScheduleEntity.toDomain());

        }
        if (vehicleScheduleList != null && !vehicleScheduleList.isEmpty()) {

            populateVehicles(vehicleScheduleList);

            populateRoutes(vehicleScheduleList);
        }

        page.setPagedData(vehicleScheduleList);

        return page;
    }

    private void populateVehicles(List<VehicleSchedule> vehicleScheduleList) {

        VehicleSearch vehicleSearch;
        Pagination<Vehicle> vehicles;
        StringBuffer vehicleNos = new StringBuffer();
        Set<String> vehicleNoSet = new HashSet<>();

        for (VehicleSchedule vfd : vehicleScheduleList) {

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

            if (vehicleScheduleList != null && !vehicleScheduleList.isEmpty())
                tenantId = vehicleScheduleList.get(0).getTenantId();

            vehicleSearch = new VehicleSearch();
            vehicleSearch.setTenantId(tenantId);
            vehicleSearch.setRegNumbers(vehicleNos.toString());

            vehicles = vehicleService.search(vehicleSearch);

            if (vehicles != null && vehicles.getPagedData() != null)
                for (Vehicle v : vehicles.getPagedData()) {

                    vehicleMap.put(v.getRegNumber(), v);

                }

            for (VehicleSchedule vfd : vehicleScheduleList) {

                if (vfd.getVehicle() != null && vfd.getVehicle().getRegNumber() != null
                        && !vfd.getVehicle().getRegNumber().isEmpty()) {

                    vfd.setVehicle(vehicleMap.get(vfd.getVehicle().getRegNumber()));
                }

            }
        }

    }

    private void populateRoutes(List<VehicleSchedule> vehicleScheduleList) {

        StringBuffer routeCodes = new StringBuffer();
        Set<String> routeCodesSet = new HashSet<>();
        RouteSearch routeSearch = new RouteSearch();
        Pagination<Route> routes;

        for (VehicleSchedule sst : vehicleScheduleList) {

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

            if (vehicleScheduleList != null && !vehicleScheduleList.isEmpty())
                tenantId = vehicleScheduleList.get(0).getTenantId();

            routeSearch.setTenantId(tenantId);
            routeSearch.setCodes(routeCodes.toString());
            routes = routeService.search(routeSearch);

            if (routes != null && routes.getPagedData() != null)
                for (Route bd : routes.getPagedData()) {

                    routeMap.put(bd.getCode(), bd);

                }

            for (VehicleSchedule vehicleSchedule : vehicleScheduleList) {

                if (vehicleSchedule.getRoute() != null && vehicleSchedule.getRoute().getCode() != null
                        && !vehicleSchedule.getRoute().getCode().isEmpty()) {

                    vehicleSchedule.setRoute(routeMap.get(vehicleSchedule.getRoute().getCode()));
                }

            }
        }

    }

}