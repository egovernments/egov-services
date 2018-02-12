package org.egov.swm.persistence.repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

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
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VehicleScheduleJdbcRepository extends JdbcRepository {

    public static final String TABLE_NAME = "egswm_vehicleschedule";

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
        final DateFormat validationDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        validationDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        if (searchRequest.getFromTripSheet() != null && searchRequest.getFromTripSheet()) {

            if (searchRequest.getScheduledFrom() != null) {
                addAnd(params);
                params.append(
                        "to_char((to_timestamp(scheduledfrom/1000) AT TIME ZONE 'Asia/Kolkata')::date,'yyyy-mm-dd') <=:scheduledFrom");
                paramValues.put("scheduledFrom", validationDateFormat.format(searchRequest.getScheduledFrom()));
            }

            if (searchRequest.getScheduledTo() != null) {
                addAnd(params);
                params.append(
                        "to_char((to_timestamp(scheduledto/1000) AT TIME ZONE 'Asia/Kolkata')::date,'yyyy-mm-dd') >=:scheduledTo");
                paramValues.put("scheduledTo", validationDateFormat.format(searchRequest.getScheduledTo()));
            }
        } else {
            if (searchRequest.getScheduledFrom() != null) {
                addAnd(params);
                params.append(
                        "to_char((to_timestamp(scheduledfrom/1000) AT TIME ZONE 'Asia/Kolkata')::date,'yyyy-mm-dd') >=:scheduledFrom");
                paramValues.put("scheduledFrom", validationDateFormat.format(searchRequest.getScheduledFrom()));
            }

            if (searchRequest.getScheduledTo() != null) {
                addAnd(params);
                params.append(
                        "to_char((to_timestamp(scheduledto/1000) AT TIME ZONE 'Asia/Kolkata')::date,'yyyy-mm-dd') <=:scheduledTo");
                paramValues.put("scheduledTo", validationDateFormat.format(searchRequest.getScheduledTo()));
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

        List<VehicleScheduleEntity> vehicleScheduleEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
                paramValues, row);

        if (searchRequest.getFromTripSheet() != null && searchRequest.getFromTripSheet()) {
            if (vehicleScheduleEntities == null || vehicleScheduleEntities.isEmpty())
                throw new CustomException("Route",
                        "Route not found for the selected Vehicle for the given period");

            vehicleScheduleEntities = returnDistinct(vehicleScheduleEntities);
        }
        for (final VehicleScheduleEntity vehicleScheduleEntity : vehicleScheduleEntities)
            vehicleScheduleList.add(vehicleScheduleEntity.toDomain());
        if (vehicleScheduleList != null && !vehicleScheduleList.isEmpty()) {

            populateVehicles(vehicleScheduleList);

            populateRoutes(vehicleScheduleList);

        }

        page.setPagedData(vehicleScheduleList);

        return page;
    }

    private List<VehicleScheduleEntity> returnDistinct(final List<VehicleScheduleEntity> vehicleScheduleList) {

        final List<VehicleScheduleEntity> responseList = new ArrayList<>();
        final Map<String, VehicleScheduleEntity> distinctRouteMap = new HashMap<>();

        for (final VehicleScheduleEntity entity : vehicleScheduleList)
            distinctRouteMap.put(entity.getRoute(), entity);

        for (final String key : distinctRouteMap.keySet())
            responseList.add(distinctRouteMap.get(key));

        return responseList;

    }

    private void populateVehicles(final List<VehicleSchedule> vehicleScheduleList) {

        VehicleSearch vehicleSearch;
        Pagination<Vehicle> vehicles;
        final StringBuffer vehicleNos = new StringBuffer();
        final Set<String> vehicleNoSet = new HashSet<>();

        for (final VehicleSchedule vfd : vehicleScheduleList)
            if (vfd.getVehicle() != null && vfd.getVehicle().getRegNumber() != null
                    && !vfd.getVehicle().getRegNumber().isEmpty())
                vehicleNoSet.add(vfd.getVehicle().getRegNumber());

        final List<String> vehicleNoList = new ArrayList(vehicleNoSet);

        for (final String vehicleNo : vehicleNoList) {

            if (vehicleNos.length() >= 1)
                vehicleNos.append(",");

            vehicleNos.append(vehicleNo);

        }
        if (vehicleNos != null && vehicleNos.length() > 0) {
            String tenantId = null;
            final Map<String, Vehicle> vehicleMap = new HashMap<>();

            if (vehicleScheduleList != null && !vehicleScheduleList.isEmpty())
                tenantId = vehicleScheduleList.get(0).getTenantId();

            vehicleSearch = new VehicleSearch();
            vehicleSearch.setTenantId(tenantId);
            vehicleSearch.setRegNumbers(vehicleNos.toString());

            vehicles = vehicleService.search(vehicleSearch);

            if (vehicles != null && vehicles.getPagedData() != null)
                for (final Vehicle v : vehicles.getPagedData())
                    vehicleMap.put(v.getRegNumber(), v);

            for (final VehicleSchedule vfd : vehicleScheduleList)
                if (vfd.getVehicle() != null && vfd.getVehicle().getRegNumber() != null
                        && !vfd.getVehicle().getRegNumber().isEmpty())
                    vfd.setVehicle(vehicleMap.get(vfd.getVehicle().getRegNumber()));
        }

    }

    private void populateRoutes(final List<VehicleSchedule> vehicleScheduleList) {

        final StringBuffer routeCodes = new StringBuffer();
        final Set<String> routeCodesSet = new HashSet<>();
        final RouteSearch routeSearch = new RouteSearch();
        Pagination<Route> routes;

        for (final VehicleSchedule sst : vehicleScheduleList)
            if (sst.getRoute() != null && sst.getRoute().getCode() != null
                    && !sst.getRoute().getCode().isEmpty())
                routeCodesSet.add(sst.getRoute().getCode());

        final List<String> routeCodeList = new ArrayList(routeCodesSet);

        for (final String code : routeCodeList) {

            if (routeCodes.length() >= 1)
                routeCodes.append(",");

            routeCodes.append(code);

        }

        if (routeCodes != null && routeCodes.length() > 0) {

            String tenantId = null;
            final Map<String, Route> routeMap = new HashMap<>();

            if (vehicleScheduleList != null && !vehicleScheduleList.isEmpty())
                tenantId = vehicleScheduleList.get(0).getTenantId();

            routeSearch.setTenantId(tenantId);
            routeSearch.setCodes(routeCodes.toString());
            routes = routeService.search(routeSearch);

            if (routes != null && routes.getPagedData() != null)
                for (final Route bd : routes.getPagedData())
                    routeMap.put(bd.getCode(), bd);

            for (final VehicleSchedule vehicleSchedule : vehicleScheduleList)
                if (vehicleSchedule.getRoute() != null && vehicleSchedule.getRoute().getCode() != null
                        && !vehicleSchedule.getRoute().getCode().isEmpty())
                    vehicleSchedule.setRoute(routeMap.get(vehicleSchedule.getRoute().getCode()));
        }

    }

}